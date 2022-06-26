package automation.inventory.rest;

import automation.inventory.domain.ItemNotFoundException;
import automation.inventory.domain.InventoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    @ResponseStatus(HttpStatus.OK)
    public ItemBalance getBalance(@PathVariable Long item) throws ItemNotFoundException {
        return inventoryService.getBalance(item)
                .map(ItemBalance::from)
                .orElseThrow(() -> new ItemNotFoundException(item));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public String save() {
        /* TODO
        1. Validar
        2. Salvar o movimento
        3. Atualizar o saldo
        Obs: talvez seja melhor postar uma mensagem pra evitar a concorrência e zoar os valores do banco
         */
        return "OK";
    }

}
