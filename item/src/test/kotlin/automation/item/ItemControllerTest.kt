package automation.item

import automation.item.rest.ItemRequest
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.reactive.function.BodyInserters


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
class ItemControllerTest {

    @LocalServerPort
    private val port: Int? = null

    @Autowired
    private lateinit var webTestClient: WebTestClient

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





}