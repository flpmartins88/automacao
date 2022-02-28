package automation.tag.infrastructure

import automation.events.tag.Item
import automation.events.tag.Status
import automation.events.tag.TagEvent
import automation.tag.domain.Tag
import automation.tag.domain.TagStatus
import org.apache.avro.specific.SpecificRecord
import org.springframework.kafka.core.KafkaTemplate

import org.springframework.stereotype.Service

@Service
class TagNotificationProducer(private val kafkaTemplate: KafkaTemplate<String, SpecificRecord>) {

    fun notifyChanges(tag: Tag) {
        this.kafkaTemplate.send("tag_events", tag.toEvent())
    }

}

private fun Tag.toEvent(): TagEvent {
    val item = Item.newBuilder()
        .setId(this.item.id)
        .setName(this.item.name)
        .build()

    return TagEvent.newBuilder()
        .setId(this.id ?: 0)
        .setItem(item)
        .setStatus(this.getStatus().toTagStatusEvent())
        .setQuantity(this.quantity)
        .setGroup(this.group)
        .setCreated(this.created.toInstant())
        .setProduced(this.produced?.toInstant())
        .setAnalyzed(this.analyzed?.toInstant())
        .setShipped(this.shipped?.toInstant())
        .setCanceled(this.canceled?.toInstant())
        .build()
}

private fun TagStatus.toTagStatusEvent(): Status =
    Status.values().first { this.name == it.name }
