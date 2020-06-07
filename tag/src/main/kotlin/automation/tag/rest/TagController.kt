package automation.tag.rest

import automation.tag.domain.Item
import automation.tag.domain.Tag
import automation.tag.domain.TagService
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import javax.validation.Valid

@RestController
@RequestMapping("/tags")
class TagController(private val tagService: TagService) {

    @PostMapping
    fun create(@Valid @RequestBody tagRequest: TagRequest): Mono<TagResponse> =
        tagService.create(tagRequest.item!!, tagRequest.quantity!!, tagRequest.group)
            .toResponse()

    @GetMapping
    fun getAll() = tagService.findAll().toResponse()

    @GetMapping("/{id}")
    fun get(@PathVariable id: Long): Mono<TagResponse> =
        tagService.find(id).toResponse()

    @PostMapping("/{id}/produced")
    fun markAsProduced(@PathVariable id: Long, @RequestBody tagProduced: TagProducedRequest): Mono<TagResponse> =
        tagService.markAsProduced(id, tagProduced).toResponse()
}

private fun Flux<Tag>.toResponse() = this.map { it.toResponse() }

private fun Mono<Tag>.toResponse() = this.map { it.toResponse() }

private fun Tag.toResponse() = TagResponse(
    this.id!!,
    this.item.toItemResponse(),
    this.quantity,
    this.created,
    this.produced,
    this.canceled,
    this.group
)

private fun Item.toItemResponse() =
    ItemResponse(this.id, this.name)