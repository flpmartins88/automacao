package automation.tag.rest

import automation.events.tag.TagEvent
import automation.tag.buildTagCreated
import automation.tag.domain.Tag
import automation.tag.domain.TagRepository
import automation.tag.infrastructure.Item
import automation.tag.infrastructure.client.ItemClient
import com.fasterxml.jackson.databind.ObjectMapper
import io.confluent.kafka.serializers.KafkaAvroDeserializer
import io.confluent.kafka.serializers.KafkaAvroDeserializerConfig
import org.apache.avro.specific.SpecificRecord
import org.apache.kafka.clients.consumer.Consumer
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
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
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
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
@AutoConfigureMockMvc
internal class TagControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var embeddedKafkaBroker: EmbeddedKafkaBroker

    @MockBean
    lateinit var itemClient: ItemClient

    @Autowired
    lateinit var tagRepository: TagRepository

    @Autowired
    lateinit var objectMapper: ObjectMapper

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
        embeddedKafkaBroker.consumeFromAnEmbeddedTopic(consumer, TAG_EVENTS_TOPIC)
    }

    @AfterAll
    fun finish() {
        consumer.close()
    }

    @Test
    fun create() {
        val item = Item(id = Random().nextLong(), name = "Any name")

        Mockito.`when`(itemClient.getItem(item.id)).thenReturn(item)

        val request = TagRequest(item = item.id, quantity = 1, numberOfTags = 10, group = null)

        mockMvc.post("/tags") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(request)
        }.andDo {
            print()
        }.andExpect {
            status { isCreated() }
            content { contentType(MediaType.APPLICATION_JSON) }
            content {
                jsonPath("$") {
                    isArray()
                    isNotEmpty()
                }
            }
        }

        val timeToWait = Duration.ofSeconds(2)

        // sometimes the test fails because get the records return the first one only
        // the others aren't in kafka
        // the third parameter will force consumer to wait the messages
        // but the assertion will never be false because the timeout will stop the test if was produced less them the expected
        val replies = KafkaTestUtils.getRecords(consumer, timeToWait, request.numberOfTags)

        assertEquals(request.numberOfTags, replies.count(), "Mensagens geradas")
    }

    @Test
    fun getAll() {
        mockMvc.get("/tags") {
            accept = MediaType.APPLICATION_JSON
        }
            .andDo { print() }
            .andExpect {
                status { isOk() }
                content { contentType(MediaType.APPLICATION_JSON) }
                content {
                    jsonPath("$") {
                        isArray()
                    }
                }
            }
    }

    @Test
    fun get() {
        val tag = tagRepository.save(Tag(
            item = automation.tag.domain.Item(id=Random().nextLong(), name="Qualquer Coisa"),
            quantity = 1,
            group = null
        ))

        mockMvc.get("/tags/${tag.id}") {
            accept = MediaType.APPLICATION_JSON
        }
            .andDo { print() }
            .andExpect {
                status { isOk() }
                content { contentType(MediaType.APPLICATION_JSON) }
                content {
                    jsonPath("$.id") { value(tag.id) }
                    jsonPath("$.item.id") { value(tag.item.id) }
                    jsonPath("$.item.name") { value(tag.item.name) }
                    jsonPath("$.quantity") { value(tag.quantity) }
                    jsonPath("$.group") { doesNotExist() }
                }
            }
    }

    @Test
    fun markAsProduced() {
        val tag = tagRepository.save(buildTagCreated()).let { tagRepository.findByIdOrNull(it.id!!)!! }
        val producedDate = ZonedDateTime.now().withZoneSameInstant(ZoneId.of("UTC"))

        val createdDateString = tag.created
            .withZoneSameInstant(ZoneId.of("UTC"))
            .format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)

        val producedDateString = producedDate.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)

        mockMvc.post("/tags/${tag.id}/produced") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(TagProducedRequest(producedDate))
        }
            .andDo { print() }
            .andExpect {
                status { isOk() }
                content { contentType(MediaType.APPLICATION_JSON) }
                content {
                    jsonPath("$.id") { value(tag.id) }
                    jsonPath("$.item.id") { value(tag.item.id) }
                    jsonPath("$.item.name") { value(tag.item.name) }
                    jsonPath("$.quantity") { value(tag.quantity) }
                    jsonPath("$.created") { value(createdDateString) }
                    jsonPath("$.produced") { value(producedDateString) }
                }
            }

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
