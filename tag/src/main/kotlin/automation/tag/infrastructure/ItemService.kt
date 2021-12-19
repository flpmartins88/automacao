package automation.tag.infrastructure

import automation.tag.infrastructure.client.ItemClient
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class ItemService(private val itemClient: ItemClient) {

    fun findItem(id: Long): Mono<Item> = itemClient.getItem(id)
        .onErrorMap { ItemNotFoundException(id, it) }

}

class ItemNotFoundException(id: Long, cause: Throwable? = null) :
    Exception("Item $id not found", cause)

data class Item(
    val id: Long,
    val name: String
)
