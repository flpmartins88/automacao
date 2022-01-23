package automation.inventory.domain;

public class ItemNotFoundException extends Exception {

    public ItemNotFoundException(Long item) {
        super("Item '%d' was not found".formatted(item));
    }

}
