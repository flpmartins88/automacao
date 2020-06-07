package automation

import java.time.ZonedDateTime

// Passar o objeto inteiro ou apenas o status?
data class TagProduced(
    val id: String,
    val dateProduced: ZonedDateTime
)

enum class TagStatus {
    CREATED, PRODUCED, ANALYZED, SHIPPED, CANCELED
}
