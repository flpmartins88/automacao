package automation.item.domain

class ItemNotFoundException(val id: Long) : Exception("Item $id not found")
