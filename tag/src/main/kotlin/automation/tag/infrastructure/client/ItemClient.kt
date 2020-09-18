package automation.tag.infrastructure.client

import automation.tag.infrastructure.Item
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

interface ItemClient {
    fun getItem(id: String): Mono<Item>
}

@Component
class ItemClientImpl : ItemClient {

    val webClient = WebClient.create("http://localhost:8091/items")

    override fun getItem(id: String) =
        webClient.get()
            .uri("/$id")
            .retrieve()
            .bodyToMono(Item::class.java)

}