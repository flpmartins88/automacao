package automation.item.infrastructure

import automation.events.item.ItemEvent
import automation.item.domain.Item
import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.support.SendResult
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class ItemCreatedProducer(
    @Value("\${events.item.created}")
    private val itemCreateTopicName: String,
    private val kafkaTemplate: KafkaTemplate<Long, ItemEvent>
) {

    fun send(item: Item): Mono<SendResult<Long, ItemEvent>> =
        item.toCreatedEvent()
            .let { kafkaTemplate.send(itemCreateTopicName, it.id, it) }
            .completable()
            .let { Mono.fromFuture { it } }

}

private fun Item.toCreatedEvent(): ItemEvent {
    return ItemEvent.newBuilder()
        .setId(this.id!!)
        .setName(this.name)
        .build()
}
