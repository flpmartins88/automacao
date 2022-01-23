package automation.order.dto;

import automation.order.infrastructure.web.ItemRequest;

import java.util.List;

public record OrderDto(String customerId, List<ItemDto> items) {


}
