package automation.order.domain;

import automation.order.commons.ApiException;
import org.springframework.http.HttpStatus;

public class ItemNotFoundException extends ApiException {
    private String itemId;

    public ItemNotFoundException(String itemId) {
        super("ITEM_NOT_FOUND", HttpStatus.BAD_REQUEST);
        this.itemId = itemId;
    }

    public String getItemId() {
        return itemId;
    }
}
