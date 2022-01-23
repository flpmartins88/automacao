package automation.order;

import automation.order.domain.OrderRepository;
import automation.order.infrastructure.web.clients.item.ItemResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

@SpringBootTest
@ActiveProfiles("test")
@ContextConfiguration(initializers = WiremockInitializer.class)
@AutoConfigureMockMvc
public class SpringBaseTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected OrderRepository orderRepository;

    @Autowired
    protected WireMockServer wiremock;

    protected final ItemResponse itemTeclado = new ItemResponse(
            UUID.randomUUID().toString(),
            "Teclado",
            10000L
    );

    protected final String nonExistentItem = UUID.randomUUID().toString();

    @BeforeEach
    public void setupMocks() throws JsonProcessingException {
        wiremock.stubFor(WireMock.get(WireMock.urlEqualTo("/items/" + itemTeclado.id()))
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withBody(objectMapper.writeValueAsString(itemTeclado))));

        wiremock.stubFor(WireMock.get(WireMock.urlEqualTo("/items/" + nonExistentItem))
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.NOT_FOUND.value())));
    }


}
