package automation.order.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(
        name = "orders",
        indexes = @Index(name = "order_code_idx", columnList = "code", unique = true))
public class Order {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private final String code = UUID.randomUUID().toString();

    @ElementCollection
    private List<Item> items;

    protected Order() {}

    public Order(List<Item> items) {
        this.items = items;
    }

    public long getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public List<Item> getItems() {
        return items;
    }
}
