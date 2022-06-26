package automation.item

import io.r2dbc.spi.ConnectionFactory
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource
import org.springframework.r2dbc.connection.init.CompositeDatabasePopulator
import org.springframework.r2dbc.connection.init.ConnectionFactoryInitializer
import org.springframework.r2dbc.connection.init.ResourceDatabasePopulator

@Configuration
class InitDatabaseConfig {

//    @Bean
//    fun initialize(connectionFactory: ConnectionFactory): ConnectionFactoryInitializer {
//        val populator = ResourceDatabasePopulator(
//            ClassPathResource("schema.sql")
//            //, ClassPathResource("data.sql")
//        )
//
//        return ConnectionFactoryInitializer().apply {
//            setConnectionFactory(connectionFactory)
//            setDatabasePopulator(populator)
//        }
//    }

}
