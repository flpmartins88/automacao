package automation.tag.rest

import automation.tag.infrastructure.ItemNotFoundException
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class ExceptionHandler {

    @ExceptionHandler(ItemNotFoundException::class)
    fun handleItemNotFound(exception: ItemNotFoundException): ResponseEntity<Errors> {
        val errors = Errors(listOf(
            Error("validation_error", exception.message)
        ))

        return ResponseEntity.badRequest().body(errors)
    }


}

data class Error(val code: String?, val message: String?)
data class Errors(val errors: List<Error>)