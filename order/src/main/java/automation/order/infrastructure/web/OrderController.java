package automation.order.infrastructure.web;

import automation.order.domain.ItemNotFoundException;
import automation.order.domain.OrderNotFoundException;
import automation.order.domain.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public OrderResponse save(@Validated @RequestBody OrderRequest orderRequest) throws ItemNotFoundException {
        var order = orderService.create(orderRequest.toDto());
        return OrderResponse.fromDomain(order);
    }

    @GetMapping("/{code}")
    public OrderResponse get(@PathVariable String code) throws OrderNotFoundException {
        var order = orderService.getByCode(code);
        return OrderResponse.fromDomain(order);
    }

}

