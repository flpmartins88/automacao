package automation.production

import automation.dto.TagProduced
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import java.time.ZonedDateTime

@SpringBootApplication
class ProductionApplication(
    private val tagProducer: TagProducer
) : ApplicationRunner {
    override fun run(args: ApplicationArguments?) {

        println(args?.optionNames)

        val tag = args?.getOptionValues("tag")?.get(0)

        if (tag == null) {
            println("tag is null")
            return
        }

        tagProducer.markTagAsProduced(TagProduced(tag, ZonedDateTime.now()))
    }

}

fun main(args: Array<String>) {
    runApplication<ProductionApplication>(*args)
}