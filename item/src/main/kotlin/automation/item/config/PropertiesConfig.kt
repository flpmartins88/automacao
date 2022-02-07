package automation.item.config

import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration

/*
 * Define properties in Java
 *
 * Annotation processor does not work with kotlin without Kapt (Kotlin annotation processor),
 * but it is in maintency mode only, sugested version are no compatible with spring annotation processor.
 */
@Configuration
@EnableConfigurationProperties(EventProperties::class)
class PropertiesConfig
