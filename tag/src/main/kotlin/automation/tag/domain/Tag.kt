package automation.tag.domain

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.ZonedDateTime
import java.util.*

@Document
data class Tag(

    @Id
    val id: String = UUID.randomUUID().toString(),

    val item: Item,
    val quantity: Int,

    val created: ZonedDateTime = ZonedDateTime.now(),
    val processed: ZonedDateTime? = null,
    val canceled: ZonedDateTime? = null,

    val group: String? = null
)

data class Item(
    val id: String,
    val name: String
)