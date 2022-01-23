package automation.inventory.domain.balance;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import javax.persistence.LockModeType;
import java.util.Optional;

/**
 *
 * @author Felipe Martins
 */
public interface BalanceRepository extends JpaRepository<Balance, String> {

    /**
     * Finds given entity. Same of {@link JpaRepository#findById(Object)}, but locks entity.
     * @param item Item's ID
     * @return An optional of {@link Balance}
     */
    // TODO talvez seja melhor lock pessimista, testar
    @Lock(value = LockModeType.OPTIMISTIC)
    Optional<Balance> findByItem(Long item);

}
