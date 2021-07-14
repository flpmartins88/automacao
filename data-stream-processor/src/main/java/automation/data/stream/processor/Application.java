package automation.data.stream.processor;

import org.apache.kafka.streams.kstream.KStream;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}

@Configuration
class StreamConfig {

    @Bean
    public Consumer<KStream<String, String>> process() {
        return input -> input.foreach(
                (key, value) -> System.out.println("Key: " + key + " Value: " + value)
        );
    }

}