package automation.order.domain;

import jakarta.persistence.Embeddable;

@Embeddable
public class Item {

    private String id;
    private String name;
    private Integer quantity;
    private Long price;

    protected Item() {}

    /**
     * Build an instance of item
     * @param id Item's ID
     * @param name Item's name
     * @param quantity Quantity ordered
     * @param price Price in cents
     */
    public Item(String id, String name, Integer quantity, Long price) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Long getPrice() {
        return price;
    }

    public Long getTotal() {
        return quantity * price;
    }
}
