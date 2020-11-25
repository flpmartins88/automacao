package automation.stock.domain;

import automation.stock.domain.movement.Movement;
import automation.stock.domain.movement.MovementRepository;
import automation.stock.domain.balance.Balance;
import automation.stock.domain.balance.BalanceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.Optional;

@Service
public class StockService {

    private final BalanceRepository balanceRepository;
    private final MovementRepository movementRepository;

    public StockService(BalanceRepository balanceRepository, MovementRepository movementRepository) {
        this.balanceRepository = balanceRepository;
        this.movementRepository = movementRepository;
    }

    public Optional<Balance> getBalance(String item) {
        return balanceRepository.findById(item);
    }

    @Transactional
    public void save(String productionId, String itemId, OperationType operationType, Integer quantity, ZonedDateTime productionDate) throws ItemNotFoundException, MovementAlreadyProcessed {

        if (movementRepository.existsByProductionId(productionId)) {
            throw new MovementAlreadyProcessed(productionId, operationType, itemId, quantity);
        }

        Balance balance = balanceRepository.findByItem(itemId)
                .orElseThrow(() -> new ItemNotFoundException(itemId));

        if (OperationType.IN == operationType) {
            balance.add(quantity);
        } else if (OperationType.OUT == operationType) {
            balance.remove(quantity);
        }

        movementRepository.save(new Movement(productionId, itemId, operationType.toMovementType(), quantity, ZonedDateTime.now(), productionDate));
    }

    /**
     * Add a new item
     * @param itemId Item's ID
     * @throws ItemAlreadyExists If that ID already exists
     */
    public void addNewItem(String itemId) throws ItemAlreadyExists {
        balanceRepository.findById(itemId)
                .orElseThrow(() -> new ItemAlreadyExists(itemId));

        balanceRepository.save(new Balance(itemId, 0));
    }

    public enum OperationType {
        IN, OUT;

        public Movement.MovementType toMovementType() {
            if (this == IN) return Movement.MovementType.IN;
            else return Movement.MovementType.OUT;
        }
    }
}
