package automation.order;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.event.ContextClosedEvent;

public class WiremockInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        var server = new WireMockServer(new WireMockConfiguration().dynamicPort());
        server.start();

        applicationContext.getBeanFactory().registerSingleton("wiremockServer", server);

        applicationContext.addApplicationListener(listener -> {
            if (listener instanceof ContextClosedEvent)
                server.stop();
        });

        TestPropertyValues
                .of(String.format("wiremock_url=http://localhost:%d", server.port()))
                .applyTo(applicationContext);
    }

}
