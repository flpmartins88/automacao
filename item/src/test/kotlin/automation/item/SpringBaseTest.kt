package automation.item

import automation.events.item.ItemEvent
import automation.item.domain.ItemRepository
import io.confluent.kafka.serializers.KafkaAvroDeserializer
import io.confluent.kafka.serializers.KafkaAvroDeserializerConfig
import org.apache.avro.specific.SpecificRecord
import org.apache.kafka.clients.consumer.Consumer
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.TestInstance
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

public const val ITEM_TOPIC = "item"

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
class SpringBaseTest {

    @Autowired
    protected lateinit var webTestClient: WebTestClient

    @Autowired
    protected lateinit var itemRepository: ItemRepository

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

    fun <T : SpecificRecord> getEventFromConsumer(topicName: String): T {
        return KafkaTestUtils.getSingleRecord(consumer, topicName)?.value() as T
    }

}
