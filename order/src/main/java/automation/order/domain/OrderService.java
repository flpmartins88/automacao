package automation.order.domain;

import automation.order.dto.ItemDto;
import automation.order.dto.OrderDto;
import automation.order.infrastructure.web.clients.item.ItemClient;
import automation.order.infrastructure.web.clients.item.ItemResponse;
import feign.FeignException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final ItemClient itemClient;

    public OrderService(OrderRepository orderRepository, ItemClient itemClient) {
        this.orderRepository = orderRepository;
        this.itemClient = itemClient;
    }

    public Order create(OrderDto orderDto) throws ItemNotFoundException {

        var itemsCache = new HashMap<String, ItemResponse>();

        for (ItemDto item : orderDto.items()) {
            if (itemsCache.containsKey(item.id())) {
                continue;
            }

            itemsCache.put(item.id(), getItem(item.id()));
        }

        var items = orderDto.items().stream()
                .map(item -> buildItem(itemsCache.get(item.id()), item))
                .collect(Collectors.toList());

        return orderRepository.save(new Order(items));
    }

    private Item buildItem(ItemResponse itemData, ItemDto itemDto) {
        return new Item(itemDto.id(), itemData.name(), itemDto.quantity(), itemData.price());
    }

    private ItemResponse getItem(String id) throws ItemNotFoundException {
        try {
            return itemClient.get(id);
        } catch (FeignException.NotFound ex) {
            throw new ItemNotFoundException(id);
        }
    }

    public Order getByCode(String code) throws OrderNotFoundException {
        return orderRepository.findByCode(code)
                .orElseThrow(() -> new OrderNotFoundException(code));
    }
}
