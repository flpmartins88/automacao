package automation.inventory.infrastructure;

import automation.events.item.ItemEvent;
import automation.inventory.BaseSpringTest;
import automation.inventory.domain.balance.BalanceRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

import static org.awaitility.Awaitility.await;

public class ItemCreatedConsumerTest extends BaseSpringTest {

    @Autowired
    private BalanceRepository balanceRepository;

    @Test
    void shouldConsumeEventOfNewItem() {
        var itemCreatedEvent = ItemEvent.newBuilder()
                .setId(UUID.randomUUID().toString())
                .setName("Pilha")
                .build();

        produce(itemCreatedTopic, itemCreatedEvent.getId(), itemCreatedEvent);

        await().until(
                () -> balanceRepository.findById(itemCreatedEvent.getId()).isPresent()
        );

        var balance = balanceRepository.findById(itemCreatedEvent.getId()).get();

        Assertions.assertEquals(0, balance.getQuantity(), "Invalid quantity for new item");
    }

}
