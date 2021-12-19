package automation.tag.rest

import java.time.ZonedDateTime
import javax.validation.constraints.Max
import javax.validation.constraints.Min
import javax.validation.constraints.NotNull

class TagRequest(

    @NotNull
    var item: Long?,

    @Min(1)
    @Max(1000)
    var quantity: Int? = 1,

    var group: String?,

    @Min(1)
    var numberOfTags: Int = 1
)

class TagProducedRequest(
    @NotNull
    var dataProduced: ZonedDateTime?
)

class TagResponse(
    val id: Long,
    val item: ItemResponse,
    val quantity: Int,
    val created: ZonedDateTime,
    val processed: ZonedDateTime?,
    val canceled: ZonedDateTime?,

    val group: String?
)

data class ItemResponse(
    val id: Long,
    val name: String
)
