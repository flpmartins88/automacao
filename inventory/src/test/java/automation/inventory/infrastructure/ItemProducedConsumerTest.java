package automation.inventory.infrastructure;

import automation.events.item.ItemEvent;
import automation.events.item.ItemProducedEvent;
import automation.inventory.BaseSpringTest;
import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;
import java.util.Random;
import java.util.UUID;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.*;

class ItemProducedConsumerTest extends BaseSpringTest {

    @Test
    public void shouldUpdateStock() {

        var itemEvent = ItemEvent.newBuilder()
                .setId(new Random().nextLong())
                .setName("Papel de Teste 001")
                .build();

        produce(itemCreatedTopic, itemEvent.getId(), itemEvent);

        await().until(() -> balanceRepository.findById(itemEvent.getId()).isPresent());

        var itemProducedEvent = ItemProducedEvent.newBuilder()
                .setItemId(itemEvent.getId())
                .setProductionId(UUID.randomUUID().toString())
                .setQuantity(10)
                .setEventDate(ZonedDateTime.now().toInstant())
                .build();

        produce(itemProducedTopic, itemProducedEvent.getItemId(), itemProducedEvent);

        await().until(() -> movementRepository.existsByProductionId(itemProducedEvent.getProductionId()));
        await().until(() -> balanceRepository.findById(itemEvent.getId()).get().getQuantity() > 0);

        var itemBalance = balanceRepository.findById(itemEvent.getId()).get();

        assertEquals(10, itemBalance.getQuantity());
    }

}
