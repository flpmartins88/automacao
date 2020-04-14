package automation.tag

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class TagApplication

fun main(args: Array<String>) {
    runApplication<TagApplication>(*args)
}
