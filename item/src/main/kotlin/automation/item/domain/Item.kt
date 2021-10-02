package automation.item.domain

import org.springframework.data.annotation.Id

class Item(
    var name: String,
    var price: Long
) {
    @Id
    var id: Long? = null
}
