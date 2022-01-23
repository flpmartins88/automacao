package automation.order.infrastructure.web.clients.item;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "item", url = "${application.clients.item}")
public interface ItemClient {

    @GetMapping("/items/{item}")
    ItemResponse get(@PathVariable String item);

}
