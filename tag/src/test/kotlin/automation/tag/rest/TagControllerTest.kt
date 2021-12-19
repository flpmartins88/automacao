package automation.tag.rest

import automation.tag.domain.Tag
import automation.tag.domain.TagRepository
import automation.tag.infrastructure.Item
import automation.tag.infrastructure.client.ItemClient
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
import org.springframework.http.MediaType
import org.springframework.kafka.core.DefaultKafkaConsumerFactory
import org.springframework.kafka.test.EmbeddedKafkaBroker
import org.springframework.kafka.test.context.EmbeddedKafka
import org.springframework.kafka.test.utils.KafkaTestUtils
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.reactive.server.WebTestClient
import reactor.core.publisher.Mono
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

    private lateinit var consumer: Consumer<String, SpecificRecord>

    @BeforeAll
    fun setUpConsumer() {
        val consumerProps = KafkaTestUtils.consumerProps(
            "test-local", "false",
            this.embeddedKafkaBroker
        )
        consumerProps[ConsumerConfig.AUTO_OFFSET_RESET_CONFIG] = "earliest"

        val consumerFactory = DefaultKafkaConsumerFactory<String, SpecificRecord>(consumerProps)
        consumer = consumerFactory.createConsumer()
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

        embeddedKafkaBroker.consumeFromAnEmbeddedTopic(consumer, "tag_events")
        val replies = KafkaTestUtils.getRecords(consumer)

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
    }
}
