package automation.stock.domain;

public class ItemNotFoundException extends Exception {
    public ItemNotFoundException(String item) {
        super("Item '%s' was not found".formatted(item));
    }
}
