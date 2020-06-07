package automation.tag.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate
import org.springframework.stereotype.Service

@Service
class TagProducer {

    @Autowired
    private lateinit var queueMessagingTemplate: QueueMessagingTemplate

    fun send(queue: String, message: String) {
        this.queueMessagingTemplate.convertAndSend(queue, message)
    }

}