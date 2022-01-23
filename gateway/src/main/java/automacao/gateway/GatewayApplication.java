package automacao.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
public class GatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }
}

@Configuration
class RouteConfig {

    @Bean
    public RouteLocator tagRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(p -> p
                        .path("/tags")
                        .uri("http://localhost:8092"))
                .build();
    }

    @Bean
    public RouteLocator itemRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(p -> p.path("/items/**")
                        .uri("http://localhost:8091"))
                .build();
    }

}
