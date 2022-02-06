package automation.item.infrastructure

import automation.events.Type
import automation.events.item.ItemEvent
import automation.item.domain.Item
import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.support.SendResult
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono

/**
 * Produces events about items
 *
 * @author Felipe Martins
 */
@Component
class ItemProducer(
    @Value("\${events.item}")
    private val itemTopicName: String,
    private val kafkaTemplate: KafkaTemplate<Long, ItemEvent>
) {

    /**
     * Internal event sender
     *
     * @param item The ItemEvent that will be sent
     */
    private fun send(item: ItemEvent): Mono<SendResult<Long, ItemEvent>> =
        kafkaTemplate.send(itemTopicName, item.id, item)
            .completable()
            .toMono()

    /**
     * Send an event about an Item creation
     * @param item The item created
     */
    fun sendItemCreated(item: Item): Mono<SendResult<Long, ItemEvent>> =
        send(item.toEvent(Type.NEW))

    /**
     * Send an event abount an Item deleted
     * @param item The item deleted
     */
    fun sendItemDeleted(item: Item): Mono<SendResult<Long, ItemEvent>> =
        send(item.toEvent(Type.DELETED))

}

private fun Item.toEvent(operationType: Type) =
    ItemEvent.newBuilder()
        .setId(this.id!!)
        .setName(this.name)
        .setType(operationType)
        .build()

