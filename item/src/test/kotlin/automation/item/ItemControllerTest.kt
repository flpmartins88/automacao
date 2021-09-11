package automation.item

import automation.item.domain.Item
import automation.item.domain.ItemRepository
import automation.item.rest.ItemRequest
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.reactive.function.BodyInserters

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
            .expectStatus().isNoContent
            .expectBody().isEmpty
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

//        [
//            {"id":"8e85cca0-73f9-4af8-9df9-dedb38918b53","name":"Lápis","price":100},
//            {"id":"93bff30f-0214-4c6e-8327-57dbbdd6afae","name":"Caneta","price":200}
//        ]


    }

}
