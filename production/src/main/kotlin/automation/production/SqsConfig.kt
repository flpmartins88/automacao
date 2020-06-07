package automation.production

import com.amazonaws.client.builder.AwsClientBuilder
import com.amazonaws.services.sqs.AmazonSQSAsync
import com.amazonaws.services.sqs.AmazonSQSAsyncClientBuilder
import org.springframework.beans.factory.annotation.Value
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.context.annotation.Profile

@Configuration
class QueueConfig {

    @Bean
    fun queueMessagingTemplate(amazonSQSAsync: AmazonSQSAsync): QueueMessagingTemplate {
        return QueueMessagingTemplate(amazonSQSAsync)
    }

}

@Profile("local || docker")
@Configuration
class SqsConfig {

    @Value("\${aws.url}")
    private lateinit var awsUrl: String

    @Value("\${cloud.aws.region.static}")
    private lateinit var awsRegion: String

    @Bean
    @Primary
    fun amazonSqs(): AmazonSQSAsync {
        return AmazonSQSAsyncClientBuilder.standard()
            .withEndpointConfiguration(AwsClientBuilder.EndpointConfiguration(awsUrl, awsRegion))
            .build()
    }
}