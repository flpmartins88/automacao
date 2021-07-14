package automation.item.domain

import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

/**
 * Service to manipulate item domain
 *
 * @author Felipe Martins
 */
@Service
class ItemService(private val itemRepository: ItemRepository) {

    /**
     * Adds a new Item
     *
     * @param item Item Data
     * @return Same item
     */
    fun create(item: Item): Mono<Item> =
        itemRepository.save(item)

    /**
     * Sets new values in item
     *
     * @param id Item's id
     * @param item Item Data
     *
     * @return Current state of this item
     */
    fun update(id: String, item: Item): Mono<Item> {
        return itemRepository.findById(id)
            .setValues(item)
            .update()
            .switchIfEmpty(Mono.error(ItemNotFoundException(id)))
    }

    /**
     * Returns all items
     *
     * @return A Flux of [Item]
     */
    fun getAll(): Flux<Item> =
        itemRepository.findAll()

    /**
     * Return a single Item
     *
     * @param id Item's id
     *
     * @return An [Item]
     */
    fun get(id: String): Mono<Item> =
        itemRepository.findById(id)
            .switchIfEmpty(Mono.error(ItemNotFoundException(id)))

    private fun Mono<Item>.update() =
        this.flatMap { itemRepository.save(it) }

    private fun Mono<Item>.setValues(newValues: Item) =
        this.map {
            it.name = newValues.name
            it.price = newValues.price
            it
        }
}

