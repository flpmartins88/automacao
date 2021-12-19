package automation.tag

import automation.tag.infrastructure.Item
import java.util.*

fun buildItem(id: Long = Random().nextLong(), name: String = "Awesome Item") =
    Item(id=id, name=name)
