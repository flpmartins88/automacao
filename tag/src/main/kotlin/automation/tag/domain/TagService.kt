package automation.tag.domain

import automation.tag.rest.TagProducedRequest
import automation.tag.infrastructure.ItemService
import automation.tag.infrastructure.TagNotificationProducer
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toFlux
import java.time.ZonedDateTime
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
     * @param numberOfTags The number og tags to be generated
     *
     * @return A new [Tag]
     */
    fun create(item: String, quantity: Int, group: String?, numberOfTags: Int): Flux<Tag> =
        itemService.findItem(item)
            .createTags(quantity, group, numberOfTags)
            .saveTags()
            .flatMapMany { Flux.fromIterable(it) }
            .notifyChanges()

    fun findAll(): Flux<Tag> =
        tagRepository.findAll().toFlux()

    fun find(id: Long): Mono<Tag> = Mono.justOrEmpty(tagRepository.findByIdOrNull(id))

    /**
     * Marks a tag as PRODUCED and save infos about it
     *
     * @param id Tag's identification
     * @param tagProduced Infos about production
     *
     * @return The actual state of [Tag]
     */
    fun produceTag(id: Long, tagProduced: TagProducedRequest): Mono<Tag> =
        findTag(id)
            .map { tag -> tag.produce(tagProduced.dataProduced!!); tag }
//            .doOnNext { tag -> tag.produce(tagProduced.dataProduced!!) }
            .saveTag()
            .notifyChanges()

    fun analyzeTag(id: Long): Mono<Tag> {
        TODO("Not yet implemented")
    }

    /**
     * Marks a tag as CANCELED
     *
     * @param id Tag's identification
     * @param canceledDate When tag was canceled
     *
     * @return The actual state of [Tag]
     */
    fun cancelTag(id: Long, canceledDate: ZonedDateTime) =
        findTag(id)
            .doOnNext { tag -> tag.cancel(canceledDate) }
            .saveTag()
            .notifyChanges()


    private fun findTag(id: Long) =
        Mono.justOrEmpty(this.tagRepository.findByIdOrNull(id))
            .switchIfEmpty(Mono.error(TagNotFoundException(id)))


    private fun Mono<Tag>.saveTag() =
        this.map { tag -> tagRepository.save(tag) }

    private fun Flux<Tag>.notifyChanges() =
        this.doOnNext { tag -> tagNotificationProducer.notifyChanges(tag) }

    private fun Mono<Tag>.notifyChanges() =
        this.doOnNext { tag -> tagNotificationProducer.notifyChanges(tag) }



    private fun Mono<List<Tag>>.saveTags(): Mono<List<Tag>> =
        this.map { tagRepository.saveAll(it) }

}

private fun Mono<ItemVO>.createTags(quantity: Int, group: String?, numberOfTags: Int) =
    this.map { currentItem -> (0 until numberOfTags).map { currentItem.createTag(quantity, group) } }

private fun ItemVO.createTag(quantity: Int, group: String?) =
    Tag(item = this.toDomain(), quantity = quantity, group = group)

private fun ItemVO.toDomain() =
    Item(id = this.id, name = this.name)