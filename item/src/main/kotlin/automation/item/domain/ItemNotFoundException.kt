package automation.item.domain

class ItemNotFoundException(val id: String) : Exception("Item $id not found")
