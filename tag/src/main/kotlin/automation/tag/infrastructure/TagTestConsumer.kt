package automation.tag.infrastructure

import automation.events.tag.TagEvent
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.springframework.context.annotation.Profile
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.support.Acknowledgment
import org.springframework.stereotype.Service

/**
 * Essa classe serve apenas para ver o que está sendo produzido
 *
 * @author Felipe Martins
 */
@Service
@Profile("!test")
class TagTestConsumer {

    @KafkaListener(topics = ["tag_events"])
    fun consumeEvent(record: ConsumerRecord<String, TagEvent>, ack: Acknowledgment) {
        val event: TagEvent = record.value()
        println(event)
        ack.acknowledge()
    }

}