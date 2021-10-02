package automation.item.domain

import org.springframework.data.repository.reactive.ReactiveCrudRepository
import java.util.*

interface ItemRepository : ReactiveCrudRepository<Item, Long>
