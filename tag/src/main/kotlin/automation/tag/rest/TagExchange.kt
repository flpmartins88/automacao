package automation.tag.rest

import java.time.ZonedDateTime
import javax.validation.constraints.Max
import javax.validation.constraints.Min
import javax.validation.constraints.NotNull

class TagRequest(

    @NotNull
    var item: String?,

    @Min(1)
    @Max(1000)
    var quantity: Int? = 1,

    var group: String?
)

class TagResponse(
    val id: String,
    val item: ItemResponse,
    val quantity: Int,
    val created: ZonedDateTime,
    val processed: ZonedDateTime?,
    val canceled: ZonedDateTime?,

    val group: String?
)

data class ItemResponse(
    val id: String,
    val name: String
)