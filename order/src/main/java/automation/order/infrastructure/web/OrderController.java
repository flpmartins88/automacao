package automation.order.infrastructure.web;

import automation.order.domain.ItemNotFoundException;
import automation.order.domain.OrderNotFoundException;
import automation.order.domain.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<OrderResponse> save(@Validated @RequestBody OrderRequest orderRequest) throws ItemNotFoundException {
        var order = orderService.create(orderRequest.toDto());
        var orderResponse = OrderResponse.fromDomain(order);
        return ResponseEntity.created(URI.create("/orders/" + order.getCode())).body(orderResponse);
    }

    @GetMapping("/{code}")
    public ResponseEntity<OrderResponse> get(@PathVariable String code) throws OrderNotFoundException {
        var order = orderService.getByCode(code);
        var orderResponse = OrderResponse.fromDomain(order);
        return ResponseEntity.ok(orderResponse);
    }

}

