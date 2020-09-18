package automation.tag.infrastructure

import automation.tag.infrastructure.client.ItemClient
import automation.tag.rest.TagRequest
import automation.tag.rest.TagResponse
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
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
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
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@EmbeddedKafka(
    topics = [TAG_EVENTS_TOPIC],
    partitions = 1,
    controlledShutdown = false
)
@DirtiesContext
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureWebTestClient
internal class TagNotificationProducerTest {

    @Autowired
    private lateinit var webTestClient: WebTestClient

    @Autowired
    private lateinit var embeddedKafkaBroker: EmbeddedKafkaBroker

    @MockBean
    lateinit var itemClient: ItemClient

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
    fun shouldSendEventsWhenTagIsCreated() {
        val item = Item(id = UUID.randomUUID().toString(), name = "Any name")

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

}