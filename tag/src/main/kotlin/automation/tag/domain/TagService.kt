package automation.tag.domain

import automation.tag.rest.TagProducedRequest
import automation.tag.service.ItemService
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toFlux
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
            .createTag(quantity, group)
            .saveTag()

    fun findAll(): Flux<Tag> =
        tagRepository.findAll().toFlux()

    fun find(id: Long): Mono<Tag> =
        Mono.justOrEmpty(tagRepository.findByIdOrNull(id))

    private fun Mono<Tag>.saveTag() =
        this.map { tagRepository.save(it) }

    fun markAsProduced(id: Long, tagProduced: TagProducedRequest): Mono<Tag> =
        Mono.justOrEmpty(this.tagRepository.findByIdOrNull(id))
            .switchIfEmpty(Mono.error(TagNotFoundException(id)))
            .doOnNext { tag -> tag.markAsProduced(tagProduced.dataProduced!!) }
            .map { tag -> tagRepository.save(tag) }

}

private fun Mono<ItemVO>.createTag(quantity: Int, group: String?) =
    this.map { it.createTag(quantity, group) }

private fun ItemVO.createTag(quantity: Int, group: String?) =
    Tag(item = this.toDomain(), quantity = quantity, group = group)

private fun ItemVO.toDomain() =
    Item(id = this.id, name = this.name)