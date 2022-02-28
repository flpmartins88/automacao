package automation.tag.rest

import automation.tag.domain.Item
import automation.tag.domain.Tag
import automation.tag.domain.TagService
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/tags", produces = [MediaType.APPLICATION_JSON_VALUE])
class TagController(private val tagService: TagService) {

    @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@Valid @RequestBody tagRequest: TagRequest): List<TagResponse> =
        tagService.create(tagRequest.item!!, tagRequest.quantity!!, tagRequest.group, tagRequest.numberOfTags)
            .toResponse()

    @GetMapping
    fun getAll() = tagService.findAll().toResponse()

    @GetMapping("/{id}")
    fun get(@PathVariable id: Long): TagResponse =
        tagService.find(id).toResponse()

    @PostMapping("/{id}/produced", consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun markAsProduced(@PathVariable id: Long, @RequestBody tagProduced: TagProducedRequest): TagResponse =
        tagService.produceTag(id, tagProduced).toResponse()

}

private fun List<Tag>.toResponse() = this.map { it.toResponse() }

private fun Tag.toResponse() = TagResponse(
    this.id!!,
    this.item.toResponse(),
    this.quantity,
    this.created,
    this.produced,
    this.canceled,
    this.group
)

private fun Item.toResponse() = ItemResponse(
    this.id, this.name
)
