package automation.tag.domain

import java.time.ZonedDateTime
import javax.persistence.*

@Entity
class Tag(

    @Embedded
    @AttributeOverrides(
        AttributeOverride(name = "id", column = Column(name = "item_id")),
        AttributeOverride(name = "name", column = Column(name = "item_name"))
    )
    val item: Item,

    val quantity: Int,
    @Column(name = "tag_group")
    val group: String? = null

) {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    val created: ZonedDateTime = ZonedDateTime.now()

    var produced: ZonedDateTime? = null
    var analyzed: ZonedDateTime? = null
    var shipped: ZonedDateTime? = null
    var canceled: ZonedDateTime? = null

    fun produce(dateProduced: ZonedDateTime) {
        this.getStatus().markAsProduced(this, dateProduced)
    }

    fun markAsAnalyzed(dateAnalyzed: ZonedDateTime) {
        this.getStatus().markAsAnalyzed(this, dateAnalyzed)
    }

    fun markAsShipped(dateShipped: ZonedDateTime) {
        this.getStatus().markAsShipped(this, dateShipped)
    }

    fun cancel(dateCanceled: ZonedDateTime) {
        this.getStatus().markAsCanceled(this, dateCanceled)
    }

    fun getStatus() =
        when {
            this.canceled != null -> TagStatus.CANCELED
            this.shipped != null -> TagStatus.SHIPPED
            this.analyzed != null -> TagStatus.ANALYZED
            this.produced != null -> TagStatus.PRODUCED
            else -> TagStatus.CREATED
        }

    override fun toString(): String {
        return "${this.javaClass.name}[id='$id']"
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Tag

        if (item != other.item) return false
        if (quantity != other.quantity) return false
        if (group != other.group) return false
        if (id != other.id) return false
        if (created != other.created) return false
        if (produced != other.produced) return false
        if (canceled != other.canceled) return false

        return true
    }
}

interface TagState {
    fun markAsProduced(tag: Tag, dateState: ZonedDateTime) {
        throw IllegalStateException("Cant change state. Actual state is ${tag.getStatus()}")
    }
    fun markAsAnalyzed(tag: Tag, dateState: ZonedDateTime) {
        throw IllegalStateException("Cant change state. Actual state is ${tag.getStatus()}")
    }
    fun markAsShipped(tag: Tag, dateState: ZonedDateTime) {
        throw IllegalStateException("Cant change state. Actual state is ${tag.getStatus()}")
    }
    fun markAsCanceled(tag: Tag, dateState: ZonedDateTime) {
        throw IllegalStateException("Cant change state. Actual state is ${tag.getStatus()}")
    }
}

enum class TagStatus : TagState {
    CREATED {
        override fun markAsProduced(tag: Tag, dateState: ZonedDateTime) {
            tag.produced = dateState
        }
        override fun markAsCanceled(tag: Tag, dateState: ZonedDateTime) {
            tag.canceled = dateState
        }
    },
    PRODUCED {
        override fun markAsAnalyzed(tag: Tag, dateState: ZonedDateTime) {
            tag.analyzed = dateState
        }

        override fun markAsShipped(tag: Tag, dateState: ZonedDateTime) {
            tag.shipped = dateState
        }

        override fun markAsCanceled(tag: Tag, dateState: ZonedDateTime) {
            tag.canceled = dateState
        }
    },
    ANALYZED {
        override fun markAsShipped(tag: Tag, dateState: ZonedDateTime) {
            tag.shipped = dateState
        }

        override fun markAsCanceled(tag: Tag, dateState: ZonedDateTime) {
            tag.canceled = dateState
        }
    },
    SHIPPED {
        override fun markAsCanceled(tag: Tag, dateState: ZonedDateTime) {
            tag.canceled = dateState
        }
    },
    CANCELED;
}

@Embeddable
data class Item(
    val id: String,
    val name: String
)