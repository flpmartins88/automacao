package automation.inventory.domain.balance;

import jakarta.persistence.*;

/**
 * Representa o saldo do item no estoque
 *
 * @author Felipe Martins
 */
@Entity
public class Balance {

    @Id
    private Long item;
    private Integer quantity;

    // To JPA :)
    protected Balance() {}

    public Balance(Long item, Integer quantity) {
        this.item = item;
        this.quantity = quantity;
    }

    @Version
    private long version;

    public Long getItem() {
        return this.item;
    }

    public Integer getQuantity() {
        return this.quantity;
    }

    public void add(Integer quantity) {
        this.quantity += quantity;
    }

    public void remove(Integer quantity) {
        this.quantity -= quantity;
    }
}
