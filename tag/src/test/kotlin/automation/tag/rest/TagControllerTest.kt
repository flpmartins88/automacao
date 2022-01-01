package automation.tag.rest

import automation.events.tag.TagEvent
import automation.tag.buildTagCreated
import automation.tag.domain.Tag
import automation.tag.domain.TagRepository
import automation.tag.infrastructure.Item
import automation.tag.infrastructure.client.ItemClient
import io.confluent.kafka.serializers.KafkaAvroDeserializer
import io.confluent.kafka.serializers.KafkaAvroDeserializerConfig
import org.apache.avro.specific.SpecificRecord
import org.apache.kafka.clients.consumer.Consumer
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.junit.jupiter.api.*

import org.junit.jupiter.api.Assertions.*
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.MediaType
import org.springframework.kafka.core.DefaultKafkaConsumerFactory
import org.springframework.kafka.test.EmbeddedKafkaBroker
import org.springframework.kafka.test.context.EmbeddedKafka
import org.springframework.kafka.test.utils.KafkaTestUtils
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.reactive.server.WebTestClient
import reactor.core.publisher.Mono
import java.time.*
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.*

const val TAG_EVENTS_TOPIC = "tag_events"

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EmbeddedKafka(
    topics = [TAG_EVENTS_TOPIC],
    partitions = 1,
    controlledShutdown = false
)
@DirtiesContext
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureWebTestClient
internal class TagControllerTest {

    @Autowired
    private lateinit var webTestClient: WebTestClient

    @Autowired
    private lateinit var embeddedKafkaBroker: EmbeddedKafkaBroker

    @MockBean
    lateinit var itemClient: ItemClient

    @Autowired
    lateinit var tagRepository: TagRepository

    private lateinit var consumer: Consumer<SpecificRecord, SpecificRecord>

    @BeforeAll
    fun setUpConsumer() {
        val consumerProps = KafkaTestUtils.consumerProps(
            "test-local", "false",
            this.embeddedKafkaBroker
        )
        consumerProps[ConsumerConfig.AUTO_OFFSET_RESET_CONFIG] = "earliest"
        consumerProps[KafkaAvroDeserializerConfig.SCHEMA_REGISTRY_URL_CONFIG] = "mock://testUrl"
        consumerProps[KafkaAvroDeserializerConfig.SPECIFIC_AVRO_READER_CONFIG] = "true"
        consumerProps[ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG] =  KafkaAvroDeserializer::class.qualifiedName
        consumerProps[ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG] = KafkaAvroDeserializer::class.qualifiedName

        val consumerFactory = DefaultKafkaConsumerFactory<SpecificRecord, SpecificRecord>(consumerProps)
        consumer = consumerFactory.createConsumer()
        embeddedKafkaBroker.consumeFromAnEmbeddedTopic(consumer, "tag_events")
    }

    @AfterAll
    fun finish() {
        consumer.close()
    }

    @Test
    fun create() {
        val item = Item(id = Random().nextLong(), name = "Any name")

        Mockito.`when`(itemClient.getItem(item.id)).thenReturn(Mono.just(item))

        val request = TagRequest(item = item.id, quantity = 1, numberOfTags = 10, group = null)

        webTestClient.post()
            .uri("/tags")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .body(Mono.just(request), TagRequest::class.java)
            .exchange()
            .expectStatus().is2xxSuccessful
            .expectBodyList(TagResponse::class.java)

        val timeToWait = Duration.ofSeconds(2).toMillis()

        // sometimes the test fails because get the records return the first one only
        // the others aren't in kafka
        // the third parameter will force consumer to wait the messages
        // but the assertion will never be false because the timeout will stop the test if was produced less them the expected
        val replies = KafkaTestUtils.getRecords(consumer, timeToWait, request.numberOfTags)

        assertEquals(request.numberOfTags, replies.count(), "Mensagens geradas")
    }

    @Test
    fun getAll() {
        webTestClient.get()
            .uri("/tags")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().is2xxSuccessful
            .expectBodyList(TagResponse::class.java)
    }

    @Test
    fun get() {
        val tag = tagRepository.save(Tag(
            item = automation.tag.domain.Item(id=Random().nextLong(), name="Qualquer Coisa"),
            quantity = 1,
            group = null
        ))

        webTestClient.get()
            .uri("/tags/${tag.id}")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk
            .expectBody()
            .jsonPath("$.id").isEqualTo(tag.id!!)
            .jsonPath("$.item.id").isEqualTo(tag.item.id)
            .jsonPath("$.quantity").isEqualTo(tag.quantity)
            .jsonPath("$.group").doesNotExist()
    }

    @Test
    fun markAsProduced() {
        val tag = tagRepository.save(buildTagCreated()).let { tagRepository.findByIdOrNull(it.id!!)!! }
        val producedDate = ZonedDateTime.now().withZoneSameInstant(ZoneId.of("UTC"))

        val createdDateString = tag.created
            .withZoneSameInstant(ZoneId.of("UTC"))
            .format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)

        val producedDateString = producedDate.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)

        webTestClient.post()
            .uri("/tags/${tag.id}/produced")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .body(Mono.just(TagProducedRequest(producedDate)), TagProducedRequest::class.java)
            .exchange()
            .expectStatus().isOk
            .expectBody()
            .jsonPath("$.id").isEqualTo(tag.id!!)
            .jsonPath("$.item.id").isEqualTo(tag.item.id)
            .jsonPath("$.item.name").isEqualTo(tag.item.name)
            .jsonPath("$.quantity").isEqualTo(tag.quantity)
            .jsonPath("$.created").isEqualTo(createdDateString)
            .jsonPath("$.produced").isEqualTo(producedDateString)

        val record = KafkaTestUtils.getRecords(consumer)
            .records("tag_events")
            .first()
            .value() as TagEvent

        assertEquals(tag.item.id, record.item.id)
        assertEquals(tag.item.name, record.item.name)
        assertEquals(tag.quantity, record.quantity)
        assertDates(tag.created, record.created)
        assertDates(producedDate, record.produced)

    }
}

fun assertDates(expected: ZonedDateTime, actual: Instant) {
    val expectedFormatted = expected
        .withZoneSameInstant(ZoneOffset.UTC)
        .truncatedTo(ChronoUnit.MILLIS)
        .format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)

    val actualFormatted = actual
        .atOffset(ZoneOffset.UTC)
        .format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)

    assertEquals(expectedFormatted, actualFormatted)
}
