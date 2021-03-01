package automation.stock.domain.balance;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Version;

/**
 * Representa o saldo do item no estoque
 *
 * @author Felipe Martins
 */
@Entity
public class Balance {

    @Id
    private String item;
    private Integer quantity;

    // To JPA :)
    protected Balance() {}

    public Balance(String item, Integer quantity) {
        this.item = item;
        this.quantity = quantity;
    }

    @Version
    private long version;

    public String getItem() {
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
