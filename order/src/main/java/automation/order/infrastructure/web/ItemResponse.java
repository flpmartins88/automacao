package automation.order.infrastructure.web;

import automation.order.domain.Item;

public record ItemResponse(String id, String name, Integer quantity, Long price) {

    public static ItemResponse fromDomain(Item item) {
        return new ItemResponse(item.getId(), item.getName(), item.getQuantity(), item.getPrice());
    }
}
