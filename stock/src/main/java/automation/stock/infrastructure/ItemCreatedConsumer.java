package automation.stock.infrastructure;

import automation.events.item.ItemEvent;
import automation.stock.domain.ItemAlreadyExists;
import automation.stock.domain.StockService;
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

    private final StockService stockService;

    public ItemCreatedConsumer(StockService stockService) {
        this.stockService = stockService;
    }

    @Transactional
    @KafkaListener(topics = "item_created")
    public void consumeEvent(ConsumerRecord<String, ItemEvent> record, Acknowledgment ack) {
        try {
            stockService.addNewItem(record.value().getId());
            ack.acknowledge();
        } catch (ItemAlreadyExists ex) {
            log.warn("Error consuming item_created event for item id: " + record.value().getId() + ". Item already exists");
            ack.acknowledge();
        } catch (Exception ex) {
            log.error("Error consuming item_created event for item id: " + record.value().getId(), ex);
        }
    }

}
