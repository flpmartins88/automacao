package automation.production

import automation.dto.TagProduced
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate
import org.springframework.stereotype.Service

@Service
class TagProducer(
    private val queueMessagingTemplate: QueueMessagingTemplate
) {

    fun markTagAsProduced(tag: TagProduced) {
        this.queueMessagingTemplate.convertAndSend("tag_produced", tag)
    }

}