package automation.inventory.rest;

import automation.inventory.domain.ItemNotFoundException;
import automation.inventory.domain.InventoryService;
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
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    /**
     * Returns the quantity in stock from requested item
     * @param item Item's ID
     * @return A {@link ResponseEntity} containing an {@link ItemBalance}
     * @throws ItemNotFoundException Quando o item não é encontrado no banco de dados
     */
    @GetMapping("/items/{item}")
    public ResponseEntity<ItemBalance> getBalance(@PathVariable Long item) throws ItemNotFoundException {
        return inventoryService.getBalance(item)
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
