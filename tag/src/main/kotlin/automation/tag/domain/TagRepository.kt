package automation.tag.domain

import org.springframework.data.mongodb.repository.ReactiveMongoRepository

interface TagRepository : ReactiveMongoRepository<Tag, String>