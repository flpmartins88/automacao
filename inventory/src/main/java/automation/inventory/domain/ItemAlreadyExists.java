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
    public ItemAlreadyExists(Long itemId) {
        super("Item with ID %d already exists".formatted(itemId));
    }

}
