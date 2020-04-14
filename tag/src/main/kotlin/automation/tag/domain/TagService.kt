package automation.tag.domain

import automation.tag.service.ItemNotFoundException
import automation.tag.service.ItemService
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.switchIfEmpty
import automation.tag.service.Item as ItemVO

@Service
class TagService(
    private val itemService: ItemService,
    private val tagRepository: TagRepository
) {

    /**
     * Creates a new [Tag]
     *
     * @param item Item Id
     * @param quantity Item quantity
     * @param group Tag group if exists
     *
     * @return A new [Tag]
     */
    fun create(item: String, quantity: Int, group: String?): Mono<Tag> =
        itemService.findItem(item)
//            .switchIfEmpty { Mono.error(ItemNotFoundException(item)) }
            .createTag(quantity, group)
            .saveTag()

    fun findAll(): Flux<Tag> = tagRepository.findAll()

    fun find(id: String): Mono<Tag> =
        tagRepository.findById(id)

    private fun Mono<Tag>.saveTag() =
        this.flatMap { tagRepository.save(it) }

}

private fun Mono<ItemVO>.createTag(quantity: Int, group: String?) =
    this.map { it.createTag(quantity, group) }

private fun ItemVO.createTag(quantity: Int, group: String?) =
    Tag(item = this.toDomain(), quantity = quantity, group = group)

private fun ItemVO.toDomain() =
    Item(id = this.id, name = this.name)