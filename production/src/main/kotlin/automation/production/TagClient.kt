package automation.production

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import java.time.ZonedDateTime
import javax.validation.constraints.NotNull

@FeignClient(name = "TagClient", url = "\${url.tag}")
interface TagClient {

    @PostMapping("/tags/{tag}/produced", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun markAsProduced(@PathVariable tag: String, @RequestBody tagData: TagProducedRequest): TagProducedResponse

}

class TagProducedRequest(
    @NotNull
    var dataProduced: ZonedDateTime?
)

class TagProducedResponse(
    val id: String,
    val dateProduced: ZonedDateTime
)
