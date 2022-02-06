package automation.item.rest

import automation.item.domain.Item
import automation.item.domain.ItemService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import javax.validation.Valid

@RestController
@RequestMapping("/items")
class ItemController(private val itemService: ItemService) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@RequestBody @Valid itemRequest: ItemRequest): Mono<ItemResponse> =
        itemService.create(itemRequest.toDomain())
            .toResponse()

    @PutMapping("/{id}")
    fun update(@PathVariable id: Long, @Valid @RequestBody itemRequest: ItemRequest): Mono<ItemResponse> {
        return itemService.update(id, itemRequest.toDomain())
            .toResponse()
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    fun delete(@PathVariable id: Long): Mono<ItemResponse> {
        return itemService.delete(id)
            .toResponse()
    }

    @GetMapping("/{id}")
    fun get(@PathVariable id: Long): Mono<ItemResponse> =
        itemService.get(id)
            .toResponse()

    @GetMapping
    fun getAll(): Flux<ItemResponse> =
        itemService.getAll()
            .toResponse()
}

private fun Flux<Item>.toResponse() =
    this.map { it.toResponse() }

private fun ItemRequest.toDomain() =
    Item(name = this.name!!, price = this.price!!)

private fun Item.toResponse() =
    ItemResponse(id = this.id!!, name = this.name, price = this.price)

private fun Mono<Item>.toResponse() =
    this.map { it.toResponse() }
