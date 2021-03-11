package automation.inventory.rest;

import automation.inventory.domain.balance.Balance;

/**
 * Used to data from stock about an Item
 *
 * @author Felipe Martins
 */
public class ItemBalance {

    private final String item;
    private final Integer quantity;

    /**
     * Build an instance using Item's ID and quantity
     * @param item Item's ID
     * @param quantity Item's quantity
     */
    private ItemBalance(String item, Integer quantity) {
        this.item = item;
        this.quantity = quantity;
    }

    /**
     *
     * @return Item's ID
     */
    public String getItem() {
        return item;
    }

    /**
     *
     * @return Item's quantity in stock
     */
    public Integer getQuantity() {
        return quantity;
    }

    /**
     * Build an {@link ItemBalance} from {@link Balance} entity
     * @param balance
     * @return A new instance of {@link ItemBalance}
     */
    public static ItemBalance from(Balance balance) {
        return new ItemBalance(
                balance.getItem(),
                balance.getQuantity()
        );
    }
}
