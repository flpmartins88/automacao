package automation.production

import org.slf4j.LoggerFactory
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients
import java.time.ZonedDateTime

@EnableFeignClients
@SpringBootApplication
class ProductionApplication(
    private val tagClient: TagClient
) : ApplicationRunner {

    private val logger = LoggerFactory.getLogger(javaClass)

    override fun run(args: ApplicationArguments) {
        if (args.containsOption("tag")) {
            val tag = args.getOptionValues("tag")[0]
            println("Tag: $tag")

            try {
                val response = tagClient.markAsProduced(tag, TagProducedRequest(ZonedDateTime.now()))
                logger.info("Tag $tag processed. Result: $response")
            } catch (e: Exception) {
                logger.error("Error processing tag: $tag", e)
            }

        }
    }

}

fun main(args: Array<String>) {
    runApplication<ProductionApplication>(*args)
}