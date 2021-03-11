package automation.inventory.domain;

public class MovementAlreadyProcessed extends Exception {

    private final String productionId;
    private final InventoryService.OperationType operationType;
    private final String itemId;
    private final Integer quantity;

    public MovementAlreadyProcessed(String productionId, InventoryService.OperationType operationType, String itemId, Integer quantity) {
        super("Production ID %s already processed. Details: Item: %s, OperationType: %s, Quantity: %d".formatted(productionId, itemId, operationType, quantity));

        this.productionId = productionId;
        this.operationType = operationType;
        this.itemId = itemId;
        this.quantity = quantity;
    }

}
