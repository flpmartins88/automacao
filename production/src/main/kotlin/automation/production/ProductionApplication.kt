package automation.production

import feign.Logger
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import reactivefeign.spring.config.EnableReactiveFeignClients
import java.time.ZonedDateTime

@EnableReactiveFeignClients
@SpringBootApplication
class ProductionApplication(
    private val tagClient: TagClient
) : ApplicationRunner {
    override fun run(args: ApplicationArguments) {
        if (args.containsOption("tag")) {
            val tag = args.getOptionValues("tag")[0]
            println("Tag: $tag")

            tagClient.markAsProduced(tag, TagProducedRequest(ZonedDateTime.now()))
                .subscribe(::println) { error -> println("Error: $error") }
        }
    }

}

@Configuration
class Config {

    @Bean
    fun feignLoggerLevel(): Logger.Level {
        return Logger.Level.FULL
    }
}

fun main(args: Array<String>) {
    runApplication<ProductionApplication>(*args)
}