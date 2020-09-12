package automation.tag.domain

import automation.tag.rest.TagProducedRequest
import automation.tag.infrastructure.ItemService
import automation.tag.infrastructure.TagNotificationProducer
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toFlux
import automation.tag.infrastructure.Item as ItemVO

@Service
class TagService(
    private val itemService: ItemService,
    private val tagRepository: TagRepository,
    private val tagNotificationProducer: TagNotificationProducer
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
    fun create(item: String, quantity: Int, group: String?, numberOfTags: Int): Flux<Tag> =
        itemService.findItem(item)
            .createTags(quantity, group, numberOfTags)
            .saveTags()
            .flatMapMany { Flux.fromIterable(it) }
            .doOnNext { tag -> tagNotificationProducer.notifyChanges(tag) }

    fun findAll(): Flux<Tag> =
        tagRepository.findAll().toFlux()

    fun find(id: Long): Mono<Tag> = Mono.justOrEmpty(tagRepository.findByIdOrNull(id))

    private fun Mono<List<Tag>>.saveTags(): Mono<List<Tag>> =
        this.map { tagRepository.saveAll(it) }

    fun markAsProduced(id: Long, tagProduced: TagProducedRequest): Mono<Tag> =
        Mono.justOrEmpty(this.tagRepository.findById(id))
            .switchIfEmpty(Mono.error(TagNotFoundException(id)))
            .doOnNext { tag -> tag.markAsProduced(tagProduced.dataProduced!!) }
            .map { tag -> tagRepository.save(tag) }

}

private fun Mono<ItemVO>.createTags(quantity: Int, group: String?, numberOfTags: Int) =
    this.map { currentItem -> (0 until numberOfTags).map { currentItem.createTag(quantity, group) } }

private fun ItemVO.createTag(quantity: Int, group: String?) =
    Tag(item = this.toDomain(), quantity = quantity, group = group)

private fun ItemVO.toDomain() =
    Item(id = this.id, name = this.name)