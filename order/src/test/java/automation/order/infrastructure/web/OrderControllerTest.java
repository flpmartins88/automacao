package automation.order.infrastructure.web;

import automation.order.SpringBaseTest;
import automation.order.domain.Item;
import automation.order.domain.Order;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class OrderControllerTest extends SpringBaseTest {

    @Test
    public void shouldReturnAnExistentOrder() throws Exception {
        var order = orderRepository.save(new Order(
                List.of(new Item(
                        UUID.randomUUID().toString(),
                        "Teclado",
                        10,
                        10000L))
        ));

        mockMvc.perform(get("/orders/" + order.getCode()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(order.getCode()))
                .andExpect(jsonPath("$.items").isArray())
                .andExpect(jsonPath("$.items[0].id").value(order.getItems().get(0).getId()))
                .andExpect(jsonPath("$.items[0].name").value(order.getItems().get(0).getName()))
                .andExpect(jsonPath("$.items[0].quantity").value(order.getItems().get(0).getQuantity()))
                .andExpect(jsonPath("$.items[0].price").value(order.getItems().get(0).getPrice()));
    }

    @Test
    public void shouldCreateNewOrder() throws Exception {
        var order = new OrderRequest(
                UUID.randomUUID().toString(),
                List.of(new ItemRequest(itemTeclado.id(), 1))
        );

        mockMvc.perform(post("/orders")
                        .content(objectMapper.writeValueAsString(order))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.code").isNotEmpty())
                .andExpect(jsonPath("$.items").isArray())
                .andExpect(jsonPath("$.items[0].id").value(order.getItems().get(0).getId()))
                .andExpect(jsonPath("$.items[0].name").value(itemTeclado.name()))
                .andExpect(jsonPath("$.items[0].quantity").value(order.getItems().get(0).getQuantity()))
                .andExpect(jsonPath("$.items[0].price").value(itemTeclado.price()));
    }

    @Test
    public void shouldReturnNotFoundWhenOrderDoesNotExists() throws Exception {
        var orderId = UUID.randomUUID().toString();

        mockMvc.perform(get("/orders/" + orderId))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].order").value(orderId))
                .andExpect(jsonPath("$[0].error_code").value("ORDER_NOT_FOUND"));
    }

    @Test
    public void shouldNotCreateWhenItemDoesNotExists() throws Exception {
        var order = new OrderRequest(
                UUID.randomUUID().toString(),
                List.of(new ItemRequest(nonExistentItem, 1))
        );

        mockMvc.perform(post("/orders").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(order)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].item").value(nonExistentItem))
                .andExpect(jsonPath("$[0].error_code").value("ITEM_NOT_FOUND"));
    }

    @Test
    public void shouldNotCreateWithoutCustomer() throws Exception {
        var order = new OrderRequest(null,
                List.of(new ItemRequest(itemTeclado.id(), 1)));

        mockMvc.perform(post("/orders").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(order)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].field").value("customerId"))
                .andExpect(jsonPath("$[0].error_code").value("invalid_value"))
                .andExpect(jsonPath("$[0].message").value("must not be null"));
    }

}
