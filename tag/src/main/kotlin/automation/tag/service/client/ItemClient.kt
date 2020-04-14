package automation.tag.service.client

import automation.tag.service.Item
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient

@Component
class ItemClient {

    val webClient = WebClient.create("http://localhost:8091/items")

    fun getItem(id: String) =
        webClient.get()
            .uri("/$id")
            .retrieve()
            .bodyToMono(Item::class.java)

}