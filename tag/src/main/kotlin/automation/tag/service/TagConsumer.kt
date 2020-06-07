package automation.tag.service

import org.slf4j.LoggerFactory
import org.springframework.cloud.aws.messaging.listener.annotation.SqsListener
import org.springframework.messaging.handler.annotation.Header
import org.springframework.stereotype.Service

@Service
class TagConsumer {

    private val log = LoggerFactory.getLogger(TagConsumer::class.java)

    @SqsListener("tag_produced")
    fun receiveTagProduced(message: String, @Header("SenderId") senderId: String?) {
        log.info("tag_produced: $message - $senderId")
    }

    @SqsListener("tag_canceled")
    fun receiveTagCanceled(message: String, @Header("SenderId") senderId: String?) {
        log.info("tag_canceled: $message - $senderId")
    }

}

