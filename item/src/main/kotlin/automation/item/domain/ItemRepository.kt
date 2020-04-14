package automation.item.domain

import org.springframework.data.mongodb.repository.ReactiveMongoRepository

interface ItemRepository : ReactiveMongoRepository<Item, String>