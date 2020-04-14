package automation.tag.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.convert.converter.Converter
import org.springframework.data.mongodb.core.convert.MongoCustomConversions
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.util.*

@Configuration
class ConverterConfig {

    @Bean
    fun customConversions(): MongoCustomConversions {
        return MongoCustomConversions(listOf(
            ZonedDateTimeToDateConverter(),
            DateToZonedDateTimeConverter()
        ))
    }

}

class ZonedDateTimeToDateConverter : Converter<ZonedDateTime, Date> {
    override fun convert(source: ZonedDateTime): Date? {
        return Date.from(source.toInstant())
    }
}

class DateToZonedDateTimeConverter : Converter<Date, ZonedDateTime> {
    override fun convert(source: Date): ZonedDateTime? {
        return source.toInstant().atZone(ZoneId.systemDefault())
    }
}