package automation.tag

import automation.tag.domain.Tag
import automation.tag.domain.Item
import java.time.ZonedDateTime
import java.util.*

fun buildItem(id: Long = Random().nextLong(), name: String = "Awesome Item") =
    Item(id=id, name=name)



fun buildTagCreated(item: Item = buildItem(), quantity: Int = 1) = Tag(item = item, quantity = quantity)
fun buildTagProduced(tag: Tag = buildTagCreated()) = tag.also { it.produce(ZonedDateTime.now()) }
