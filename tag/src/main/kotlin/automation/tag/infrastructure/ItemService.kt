package automation.tag.infrastructure

import automation.tag.infrastructure.client.ItemClient
import org.springframework.stereotype.Service

@Service
class ItemService(private val itemClient: ItemClient) {

    fun findItem(id: Long) =
        itemClient.getItem(id)
            .takeIf { it != null }
            ?: throw ItemNotFoundException(id)

}

class ItemNotFoundException(id: Long, cause: Throwable? = null) :
    Exception("Item $id not found", cause)

data class Item(
    val id: Long,
    val name: String
)
