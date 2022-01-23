package automation.order.infrastructure.web;

import automation.order.dto.ItemDto;
import automation.order.dto.OrderDto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class OrderRequest {

    @NotNull
    private String customerId;

    @NotEmpty
    private List<ItemRequest> items;

    public OrderRequest() {
    }

    public OrderRequest(String customerId, List<ItemRequest> items) {
        this.customerId = customerId;
        this.items = items;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public List<ItemRequest> getItems() {
        return items;
    }

    public void setItems(List<ItemRequest> items) {
        this.items = items;
    }

    public OrderDto toDto() {
        return new OrderDto(
                this.customerId,
                this.items.stream().map(ItemRequest::toDto).collect(Collectors.toList()));
    }
}
