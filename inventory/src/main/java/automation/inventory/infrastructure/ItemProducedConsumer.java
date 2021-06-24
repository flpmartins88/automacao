package automation.inventory.infrastructure;

import automation.events.item.ItemProducedEvent;
import automation.inventory.domain.ItemNotFoundException;
import automation.inventory.domain.MovementAlreadyProcessed;
import automation.inventory.domain.InventoryService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

import java.time.*;

@Service
public class ItemProducedConsumer {

    private static final Logger log = LoggerFactory.getLogger(ItemProducedConsumer.class);

    private final InventoryService inventoryService;

    public ItemProducedConsumer(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @KafkaListener(topics = "${kafka.topics.item_produced}")
    public void consumeEvent(ConsumerRecord<String, ItemProducedEvent> record, Acknowledgment ack) {
        var productionId = record.value().getProductionId();
        var itemId = record.value().getItemId();
        var quantity = record.value().getQuantity();
        var productionDate = record.value().getEventDate().atZone(ZoneOffset.UTC);

        try {
            inventoryService.save(productionId, itemId, InventoryService.OperationType.IN, quantity, productionDate);
            ack.acknowledge();
        } catch (MovementAlreadyProcessed e) {
            log.error("Movement of production ID %s already processed. Ignoring.".formatted(productionId), e);
            ack.acknowledge();
        } catch (ItemNotFoundException e) {
            log.error("Error adding '%d' to item '%s'".formatted(quantity, itemId), e);
            ack.nack(1000);
        }
    }
}
