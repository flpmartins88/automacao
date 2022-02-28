package automation.tag.domain

import automation.tag.rest.TagProducedRequest
import automation.tag.infrastructure.ItemService
import automation.tag.infrastructure.TagNotificationProducer
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.time.ZonedDateTime
import automation.tag.infrastructure.Item as ItemVO

@Service
class TagService(
    private val itemService: ItemService,
    private val tagRepository: TagRepository,
    private val tagNotificationProducer: TagNotificationProducer,
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
    fun create(item: Long, quantity: Int, group: String?, numberOfTags: Int): List<Tag> =
        itemService.findItem(item)
            .createTags(quantity, group, numberOfTags)
            .saveTags()
            .notifyChanges()

    fun findAll(): List<Tag> =
        tagRepository.findAll()

    /**
     * Marks a tag as PRODUCED and save infos about it
     *
     * @param id Tag's identification
     * @param tagProduced Infos about production
     *
     * @return The actual state of [Tag]
     */
    fun produceTag(id: Long, tagProduced: TagProducedRequest): Tag =
        find(id)
            .apply { this.produce(tagProduced.dataProduced!!) }
            .saveTag()
            .notifyChanges()

    fun analyzeTag(id: Long): Tag {
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
        find(id)
            .apply { this.cancel(canceledDate) }
            .saveTag()
            .notifyChanges()

    fun find(id: Long): Tag =
        this.tagRepository.findByIdOrNull(id)
            ?: throw TagNotFoundException(id)

    private fun Tag.saveTag(): Tag =
        tagRepository.save(this)

    private fun List<Tag>.notifyChanges(): List<Tag> =
        this.onEach { tag-> tag.notifyChanges() }

    private fun Tag.notifyChanges() =
        this.apply { tagNotificationProducer.notifyChanges(this) }

    private fun List<Tag>.saveTags(): List<Tag> =
        tagRepository.saveAll(this)

}

private fun ItemVO.createTags(quantity: Int, group: String?, numberOfTags: Int) =
    (0 until numberOfTags).map { this.createTag(quantity, group) }

private fun ItemVO.createTag(quantity: Int, group: String?) =
    Tag(item = this.toDomain(), quantity = quantity, group = group)

private fun ItemVO.toDomain() =
    Item(id = this.id, name = this.name)
