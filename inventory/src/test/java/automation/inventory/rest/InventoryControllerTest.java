package automation.inventory.rest;

import automation.inventory.BaseSpringTest;
import automation.inventory.domain.balance.Balance;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.util.Random;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class InventoryControllerTest extends BaseSpringTest {

    @Test
    public void shouldGetAnExistingItem() throws Exception {

        var balance = balanceRepository.save(new Balance(new Random().nextLong(), 100));

        mockMvc.perform(get("/items/{item}", balance.getItem()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.item", Matchers.equalTo(balance.getItem())))
                .andExpect(jsonPath("$.quantity", Matchers.equalTo(balance.getQuantity())));

    }

    @Test
    public void shouldReturnErrorNotFoundWhenItemDoesNotExists() throws Exception {
        mockMvc.perform(get("/items/{item}", new Random().nextInt()))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

}
