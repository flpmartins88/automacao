package automation.stock;

import io.confluent.kafka.serializers.KafkaAvroSerializer;
import io.confluent.kafka.serializers.KafkaAvroSerializerConfig;
import org.apache.avro.specific.SpecificRecord;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EmbeddedKafka(
        topics = { "${kafka.topics.item_created}", "${kafka.topics.item_produced}" },
        partitions = 1
)
@DirtiesContext
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureTestDatabase
public class BaseSpringTest {

    private static final Logger log = LoggerFactory.getLogger(BaseSpringTest.class);

    @Autowired
    private EmbeddedKafkaBroker embeddedKafkaBroker;

    private Producer<String, SpecificRecord> producer;

    @Value("${kafka.topics.item_created}")
    protected String itemCreatedTopic;

    @Value("${kafka.topics.item_produced}")
    protected String itemProducedTopic;

    @BeforeAll
    void setUpKafka() {
        var producerProps = KafkaTestUtils.producerProps(this.embeddedKafkaBroker);
        producerProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class);
        producerProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class);

        producerProps.put(KafkaAvroSerializerConfig.SCHEMA_REGISTRY_URL_CONFIG, "mock://testUrl");

        this.producer = new DefaultKafkaProducerFactory<String, SpecificRecord>(producerProps)
                .createProducer();

    }

    @AfterAll
    void finish() {
        producer.close();
    }

    void produce(String topic, String recordKey, SpecificRecord recordValue) {
        producer.send(
                new ProducerRecord<>(topic, recordKey, recordValue),
                (metadata, exception) -> log.info(
                        "Send result: Topic: {} Partition: {} Offset: {}",
                        metadata.topic(),
                        metadata.partition(),
                        metadata.offset()
                )
        );
    }

}
