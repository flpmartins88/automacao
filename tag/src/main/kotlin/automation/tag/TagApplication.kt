package automation.tag

import automation.tag.domain.Item
import automation.tag.domain.Tag
import automation.tag.infrastructure.TagNotificationProducer
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class TagApplication(private val tagNotificationProducer: TagNotificationProducer): ApplicationRunner {
    override fun run(args: ApplicationArguments?) {
        tagNotificationProducer.notifyChanges(Tag(Item("1", "Teste"), 1))
    }

}

fun main(args: Array<String>) {
    runApplication<TagApplication>(*args)
}
