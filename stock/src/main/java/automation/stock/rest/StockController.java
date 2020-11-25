package automation.stock.rest;

import automation.stock.domain.ItemNotFoundException;
import automation.stock.domain.StockService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Felipe Martins
 */
@RestController
public class StockController {

    private final StockService stockService;

    public StockController(StockService stockService) {
        this.stockService = stockService;
    }

    /**
     * Returns the quantity in stock from requested item
     * @param item Item's ID
     * @return A {@link ResponseEntity} containing an {@link ItemBalance}
     * @throws ItemNotFoundException Quando o item não é encontrado no banco de dados
     */
    @GetMapping("/items/{item}")
    public ResponseEntity<ItemBalance> getBalance(@PathVariable String item) throws ItemNotFoundException {
        return stockService.getBalance(item)
                .map(ItemBalance::from)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ItemNotFoundException(item));
    }

    @PostMapping
    public ResponseEntity<String> save() {
        /* TODO
        1. Validar
        2. Salvar o movimento
        3. Atualizar o saldo
        Obs: talvez seja melhor postar uma mensagem pra evitar a concorrência e zoar os valores do banco
         */
        return ResponseEntity.ok("OK");
    }

}
