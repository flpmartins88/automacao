package automation.tag.infrastructure.client

import automation.tag.infrastructure.Item
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

/**
 *
 *
 * @author Felipe Martins
 */
interface ItemClient {

    /**
     * Gets data from item
     *
     * @param id Item's ID
     *
     * @return [Item] data
     */
    fun getItem(id: Long): Mono<Item>
}

@Component
class ItemClientImpl(
    @Value("\${services.item}")
    private val itemServiceUrl: String
) : ItemClient {

    val webClient: WebClient = WebClient.create(itemServiceUrl)

    override fun getItem(id: Long) =
        webClient.get()
            .uri("/$id")
            .retrieve()
            .bodyToMono(Item::class.java)

}
