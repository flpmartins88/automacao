package automation.item.domain

import org.springframework.data.repository.reactive.ReactiveCrudRepository

interface ItemRepository : ReactiveCrudRepository<Item, Long>
