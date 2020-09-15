package automation.production

import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ProductionApplication : ApplicationRunner {
    override fun run(args: ApplicationArguments) {
        println("Source: ")
        args.sourceArgs.forEach { println(it) }
        println("---------------")
        println("Option Names: ")
        args.optionNames.forEach { println(it) }
        println("---------------")
        println("Non Options: ")
        args.nonOptionArgs.forEach { println(it) }
        println("---------------")

        if (args.containsOption("tag")) {
            val tag = args.getOptionValues("tag")[0]
            println("Tag: $tag")
        }

    }

}

fun main(args: Array<String>) {
    runApplication<ProductionApplication>(*args)
}