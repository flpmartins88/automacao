package automation.inventory.domain;

/**
 *
 *
 * @author Felipe Martins
 */
public class ItemAlreadyExists extends Exception {

    /**
     *
     * @param itemId Item's ID
     */
    public ItemAlreadyExists(String itemId) {
        super("Item with ID %s already exists".formatted(itemId));
    }

}
