package automation.tag.config

import org.springframework.context.annotation.Configuration

@Configuration
class SchedulerConfig {

    /*@Bean
    fun jdbcScheduler(@Value("\${spring.datasource.maximum-pool-size}") connectionPoolSize: Int) =
        Executors.newFixedThreadPool(connectionPoolSize)
            .let { Schedulers.fromExecutor(it) }*/

}
