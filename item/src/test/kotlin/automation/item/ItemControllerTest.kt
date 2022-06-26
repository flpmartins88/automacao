package automation.item

import automation.events.Type
import automation.events.item.ItemEvent
import automation.item.domain.Item
import automation.item.domain.ItemRepository
import automation.item.rest.ItemRequest
import io.confluent.kafka.serializers.KafkaAvroDeserializer
import io.confluent.kafka.serializers.KafkaAvroDeserializerConfig
import org.apache.avro.specific.SpecificRecord
import org.apache.kafka.clients.consumer.Consumer
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.junit.jupiter.api.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.r2dbc.AutoConfigureDataR2dbc
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.kafka.core.DefaultKafkaConsumerFactory
import org.springframework.kafka.test.EmbeddedKafkaBroker
import org.springframework.kafka.test.context.EmbeddedKafka
import org.springframework.kafka.test.utils.KafkaTestUtils
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.reactive.function.BodyInserters
import java.util.*

private const val ITEM_TOPIC = "item"

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureWebTestClient
@AutoConfigureDataR2dbc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@EmbeddedKafka(
    topics = [ITEM_TOPIC],
    partitions = 1,
    controlledShutdown = false
)
class ItemControllerTest {

    @Autowired
    private lateinit var webTestClient: WebTestClient

    @Autowired
    private lateinit var itemRepository: ItemRepository

    @Autowired
    private lateinit var embeddedKafkaBroker: EmbeddedKafkaBroker

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
        embeddedKafkaBroker.consumeFromAnEmbeddedTopic(consumer, ITEM_TOPIC)
    }

    @AfterAll
    fun finish() {
        consumer.close()
    }

//    @BeforeAll
//    fun setupDatabase() {
//        val itemTable = """
//                CREATE TABLE IF NOT EXISTS item (
//                    id    BIGINT       AUTO_INCREMENT PRIMARY KEY,
//                    code  VARCHAR(50)  NOT NULL,
//                    name  VARCHAR(200) NOT NULL,
//                    price BIGINT       NOT NULL
//                );
//            """.trimIndent()
//
//        connectionPool.create()
//            .flatMap { c -> c.createStatement(itemTable).execute().toMono() }
//            .block()
//    }

    @Test
    fun shouldCreateItem() {
        val itemRequest = ItemRequest(name = "Teclado", price = 2000)

        webTestClient.post().uri("/items")
            .body(BodyInserters.fromValue(itemRequest))
            .exchange()
            .expectStatus().isCreated
            .expectBody()
            .jsonPath("$.id").exists()
            .jsonPath("$.name").isEqualTo(itemRequest.name!!)
            .jsonPath("$.price").isEqualTo(itemRequest.price!!)

        val record = KafkaTestUtils.getSingleRecord(consumer, ITEM_TOPIC).value() as ItemEvent

        Assertions.assertEquals(itemRequest.name, record.name, "Item name")
        Assertions.assertEquals(Type.NEW, record.type, "Item type")
    }

    @Test
    fun `should delete item`() {
        val item = Item(name = "Teclado", price = 5000)
        itemRepository.save(item).subscribe()

        webTestClient.delete().uri { builder -> builder.path("/items/{id}").build(item.id) }
            .exchange()
            .expectStatus().isOk
            .expectBody()
            .jsonPath("$.id").isEqualTo(item.id!!)
            .jsonPath("$.name").isEqualTo(item.name)
            .jsonPath("$.price").isEqualTo(item.price)
    }

    @Test
    fun `should return not found when item does not exists`() {
        val itemId = Random().nextInt()

        webTestClient.delete().uri { builder -> builder.path("/items/{id}").build(itemId) }
            .exchange()
            .expectStatus().isNotFound

    }

    @Test
    fun `should return not found if deleted two times`() {
        val item = Item(name = "Teclado", price = 5000)
        itemRepository.save(item).subscribe()

        webTestClient.delete().uri { builder -> builder.path("/items/{id}").build(item.id) }
            .exchange()
            .expectStatus().isOk
            .expectBody()
            .jsonPath("$.id").isEqualTo(item.id!!)
            .jsonPath("$.name").isEqualTo(item.name)
            .jsonPath("$.price").isEqualTo(item.price)

        webTestClient.delete().uri { builder -> builder.path("/items/{id}").build(item.id) }
            .exchange()
            .expectStatus().isNotFound
    }

    @Test
    fun shouldGetAllItems() {
        val items = listOf(
            Item(name = "Caneta", price = 200),
            Item(name = "Lápis", price = 100)
        )

        itemRepository.saveAll(items)
            .subscribe()

        webTestClient.get().uri("/items")
            .exchange()
            .expectStatus().isOk
            .expectBody()
            .jsonPath("$").isArray

    }

}
