package automation.order.infrastructure.web;

import automation.order.domain.Order;

import java.util.List;
import java.util.stream.Collectors;

public record OrderResponse(String code, List<ItemResponse> items) {

    public static OrderResponse fromDomain(Order order) {
        return new OrderResponse(
                order.getCode(),
                order.getItems().stream()
                        .map(ItemResponse::fromDomain)
                        .collect(Collectors.toList())
        );
    }

}
