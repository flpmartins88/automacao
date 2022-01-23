package automation.order.domain;

import automation.order.commons.ApiException;
import org.springframework.http.HttpStatus;

public class OrderNotFoundException extends ApiException {

    private String orderCode;

    public OrderNotFoundException(String orderCode) {
        super("ORDER_NOT_FOUND", HttpStatus.NOT_FOUND);
        this.orderCode = orderCode;
    }

    public String getOrderCode() {
        return orderCode;
    }
}
