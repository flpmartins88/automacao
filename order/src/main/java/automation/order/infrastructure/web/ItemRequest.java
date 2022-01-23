package automation.order.infrastructure.web;

import automation.order.dto.ItemDto;

public class ItemRequest {

    private String id;
    private Integer quantity;

    public ItemRequest(String id, Integer quantity) {
        this.id = id;
        this.quantity = quantity;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public ItemDto toDto() {
        return new ItemDto(this.id, this.quantity);
    }
}
