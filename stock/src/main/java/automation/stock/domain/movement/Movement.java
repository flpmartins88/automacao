package automation.stock.domain.movement;

import javax.persistence.*;
import java.time.ZonedDateTime;

/**
 * Representa todos os movimentos no estoque que afeta o saldo final
 *
 * @author Felipe Martins
 */
@Entity
@Table(uniqueConstraints = { @UniqueConstraint(name = "unique_production", columnNames = {"production_id"} ) })
public class Movement {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String productionId;

    private String item;
    private ZonedDateTime date;
    private MovementType type;
    private Integer quantity;

    private ZonedDateTime productionDate;

    // JPA :)
    protected Movement() {}

    public Movement(String item, String productionId, MovementType movementType, Integer quantity, ZonedDateTime date, ZonedDateTime productionDate) {
        this.item = item;
        this.productionId = productionId;
        this.type = movementType;
        this.quantity = quantity;
        this.date = date;
        this.productionDate = productionDate;
    }

    public String getItem() {
        return item;
    }

    public ZonedDateTime getDate() {
        return date;
    }

    public MovementType getType() {
        return type;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public enum MovementType {
        IN, OUT
    }
}

