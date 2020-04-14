package automation.item.domain

import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class ItemService(
    private val itemRepository: ItemRepository
) {

    fun create(item: Item): Mono<Item> =
        itemRepository.save(item)

    fun update(id: String, item: Item): Mono<Item> {
        return itemRepository.findById(id)
            .setValues(item)
            .update(itemRepository)
            .switchIfEmpty(Mono.error(ItemNotFoundException(id)))
    }

    fun getAll(): Flux<Item> =
        itemRepository.findAll()

    fun get(id: String): Mono<Item> =
        itemRepository.findById(id)
            .switchIfEmpty(Mono.error(ItemNotFoundException(id)))

}

private fun Mono<Item>.update(itemRepository: ItemRepository) =
    this.flatMap { itemRepository.save(it) }

private fun Mono<Item>.setValues(newValues: Item) =
    this.map {
        it.name = newValues.name
        it.price = newValues.price
        it
    }