package automation.inventory.rest;

import automation.inventory.domain.balance.Balance;

/**
 * Used to data from stock about an Item
 *
 * @author Felipe Martins
 */
public record ItemBalance(Long item, Integer quantity) {

    /**
     * Build an {@link ItemBalance} from {@link Balance} entity
     *
     * @param balance An Item's balance
     * @return A new instance of {@link ItemBalance}
     */
    public static ItemBalance from(Balance balance) {
        return new ItemBalance(
                balance.getItem(),
                balance.getQuantity()
        );
    }
}
