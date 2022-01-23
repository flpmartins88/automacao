package automation.production.config

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.core.WireMockConfiguration
import org.springframework.boot.test.util.TestPropertyValues
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.context.event.ContextClosedEvent

class WiremockInitializer : ApplicationContextInitializer<ConfigurableApplicationContext> {

    override fun initialize(applicationContext: ConfigurableApplicationContext) {
        val server = WireMockServer(WireMockConfiguration().dynamicPort())
        server.start()

        applicationContext.beanFactory.registerSingleton("wiremockServer", server)

        applicationContext.addApplicationListener { listener ->
            if (listener is ContextClosedEvent) {
                server.stop()
            }
        }

        TestPropertyValues
            .of(mapOf(Pair("wiremock_url", "http://localhost:${server.port()}")))
            .applyTo(applicationContext)
    }

}
