package automation.item

import automation.item.domain.Item
import automation.item.domain.ItemRepository
import automation.item.rest.ItemRequest
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.reactive.function.BodyInserters
import java.util.*

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureWebTestClient
class ItemControllerTest {

    @Autowired
    private lateinit var webTestClient: WebTestClient

    @Autowired
    private lateinit var itemRepository: ItemRepository

    @Test
    fun shouldCreateItem() {
        val itemRequest = ItemRequest(name = "Teclado", price = 2000)

        webTestClient.post().uri("/items")
            .body(BodyInserters.fromValue(itemRequest))
            .exchange()
            .expectStatus().isCreated
            .expectBody()
            .jsonPath("$.id").exists()
            .jsonPath("$.name").isEqualTo(itemRequest.name!!)
            .jsonPath("$.price").isEqualTo(itemRequest.price!!)
    }

    @Test
    fun `should delete item`() {
        val item = Item(name = "Teclado", price = 5000)
        itemRepository.save(item).subscribe()

        webTestClient.delete().uri { builder -> builder.path("/items/{id}").build(item.id) }
            .exchange()
            .expectStatus().isOk
            .expectBody()
            .jsonPath("$.id").isEqualTo(item.id!!)
            .jsonPath("$.name").isEqualTo(item.name)
            .jsonPath("$.price").isEqualTo(item.price)
    }

    @Test
    fun `should return not found when item does not exists`() {
        val itemId = UUID.randomUUID().toString()

        webTestClient.delete().uri { builder -> builder.path("/items/{id}").build(itemId) }
            .exchange()
            .expectStatus().isNotFound

    }

    @Test
    fun `should return not found if deleted two times`() {
        val item = Item(name = "Teclado", price = 5000)
        itemRepository.save(item).subscribe()

        webTestClient.delete().uri { builder -> builder.path("/items/{id}").build(item.id) }
            .exchange()
            .expectStatus().isOk
            .expectBody()
            .jsonPath("$.id").isEqualTo(item.id!!)
            .jsonPath("$.name").isEqualTo(item.name)
            .jsonPath("$.price").isEqualTo(item.price)

        webTestClient.delete().uri { builder -> builder.path("/items/{id}").build(item.id) }
            .exchange()
            .expectStatus().isNotFound
    }

    @Test
    fun shouldGetAllItems() {
        val items = listOf(
            Item(name = "Caneta", price = 200),
            Item(name = "Lápis", price = 100)
        )

        itemRepository.saveAll(items)
            .subscribe()

        webTestClient.get().uri("/items")
            .exchange()
            .expectStatus().isOk
            .expectBody()
            .jsonPath("$").isArray

    }

}
