package automation.inventory.domain.movement;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MovementRepository extends JpaRepository<Movement, Long> {

    boolean existsByProductionId(String productionId);

}
