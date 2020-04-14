package automation.item.rest

import javax.validation.constraints.*

class ItemRequest(

    @field:NotBlank
    @field:Size(min = 3, max = 50)
    var name: String?,

    @field:NotNull
    @field:Min(value = 1)
    @field:Max(value = 99999999)
    var price: Long?

)