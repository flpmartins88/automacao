package automation.item.domain

import org.springframework.data.annotation.Id
import java.util.*

class Item(
    var name: String,
    var price: Long
) {
    @Id
    var id: Long? = null
        private set

    var code: String = UUID.randomUUID().toString()
        private set
}
