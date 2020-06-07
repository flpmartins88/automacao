package automation.tag.config

import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration
import com.amazonaws.services.sqs.AmazonSQSAsync
import com.amazonaws.services.sqs.AmazonSQSAsyncClientBuilder
import org.springframework.beans.factory.annotation.Value
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary

@Configuration
class SqsConfig {

    @Value("\${aws.url}")
    private lateinit var awsUrl: String

    @Value("\${cloud.aws.region.static}")
    private lateinit var awsRegion: String

    @Bean
    fun queueMessagingTemplate(amazonSQSAsync: AmazonSQSAsync?): QueueMessagingTemplate? {
        return QueueMessagingTemplate(amazonSQSAsync)
    }

    @Bean
    @Primary
    fun amazonSqs(): AmazonSQSAsync? {
        return AmazonSQSAsyncClientBuilder.standard()
            .withEndpointConfiguration(EndpointConfiguration(awsUrl, awsRegion))
            .build()
    }
}