package automation.inventory.infrastructure;

import automation.events.item.ItemEvent;
import automation.inventory.domain.ItemAlreadyExists;
import automation.inventory.domain.InventoryService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ItemCreatedConsumer {

    private static final Logger log = LoggerFactory.getLogger(ItemCreatedConsumer.class);

    private final InventoryService inventoryService;

    public ItemCreatedConsumer(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @Transactional
    @KafkaListener(topics = "${kafka.topics.item_created}")
    public void consumeEvent(ConsumerRecord<String, ItemEvent> record, Acknowledgment ack) {
        try {
            log.info("Adding new Item {}", record.value().getId());
            inventoryService.addNewItem(record.value().getId());
            ack.acknowledge();
            log.info("Item {} added", record.value().getId());
        } catch (ItemAlreadyExists ex) {
            log.warn("Error consuming item_created event for item id: " + record.value().getId() + ". Item already exists");
            ack.acknowledge();
        } catch (Exception ex) {
            log.error("Error consuming item_created event for item id: " + record.value().getId(), ex);
            ack.nack(1000);
        }
    }

}
