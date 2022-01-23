package automation.production

import automation.production.config.WiremockInitializer
import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.client.WireMock.aResponse
import org.junit.jupiter.api.BeforeAll
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import java.time.ZonedDateTime

@SpringBootTest
@ContextConfiguration(initializers = [WiremockInitializer::class])
class SpringBaseTest {

    @Autowired
    private lateinit var wiremockServer: WireMockServer

    @BeforeAll
    fun setupWiremock() {
        wiremockServer.stubFor(WireMock.get("/tags/1")
            .willReturn(aResponse().withHeader("Content-Type", "application/json")
                .withStatus(200)
                .withBody(
                    """
                        {
                            "id": 1,
                            "item": {
                                "id": 1,
                                "name": "Teclado"
                            }, 
                            "quantity": 1,
                            "created": "2021-01-01T10:00:00Z"
                        }
                    """.trimIndent()
                )))
    }

}