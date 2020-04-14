package automation.item.domain

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Document
data class Item(
    @Id
    val id: String = UUID.randomUUID().toString(),
    var name: String,
    var price: Long
)