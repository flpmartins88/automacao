package automation.tag.infrastructure

import automation.tag.infrastructure.client.ItemClient
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class ItemService(private val itemClient: ItemClient) {

    fun findItem(id: String): Mono<Item> = itemClient.getItem(id)
        .onErrorMap { ItemNotFoundException(id, it) }

}

class ItemNotFoundException(id: String, cause: Throwable? = null) :
    Exception("Item $id not found", cause)

data class Item(
    val id: String,
    val name: String
)
