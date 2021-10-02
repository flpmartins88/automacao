package automation.item.domain

import java.util.*

class ItemNotFoundException(val id: Long) : Exception("Item $id not found")
