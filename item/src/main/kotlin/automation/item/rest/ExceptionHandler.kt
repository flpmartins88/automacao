package automation.item.rest

import automation.item.domain.ItemNotFoundException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.support.WebExchangeBindException
import org.springframework.web.server.MethodNotAllowedException
import org.springframework.web.server.ServerWebInputException

@ControllerAdvice
class ExceptionHandler {

    private val log = LoggerFactory.getLogger(automation.item.rest.ExceptionHandler::class.java)

    @ExceptionHandler(ServerWebInputException::class)
    fun handleBaseException(exception: ServerWebInputException): ResponseEntity<Errors> {
        log.error("Erro de validação", exception)

        val message = when(exception.status) {
            HttpStatus.BAD_REQUEST -> "invalid body"
            else -> exception.status.reasonPhrase
        }

        val errors = Errors(listOf(
            Error(exception.status.value().toString(), message)
        ))

        return ResponseEntity(errors, HttpStatus.NOT_FOUND)

    }

    @ExceptionHandler(WebExchangeBindException::class)
    fun validationError(exception: WebExchangeBindException): ResponseEntity<Errors> {
        val errors = exception.bindingResult.fieldErrors.map { Error(it.field, it.defaultMessage) }
        return ResponseEntity(Errors(errors), HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(MethodNotAllowedException::class)
    fun methodNotAllowedException(exception: MethodNotAllowedException): ResponseEntity<Errors> {
        return ResponseEntity(Errors(listOf(Error(exception.httpMethod, exception.message))), HttpStatus.METHOD_NOT_ALLOWED)
    }

    @ExceptionHandler(ItemNotFoundException::class)
    fun itemNotFound(exception: ItemNotFoundException) =
        ResponseEntity.status(HttpStatus.NOT_FOUND).body("Item '${exception.id}' not found")
    
    @ExceptionHandler(Exception::class)
    fun handleAll(exception: Exception): ResponseEntity<Void> {
        log.error("Uncategorized error", exception)
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build<Void>()
    }

}

data class Error(val code: String?, val message: String?)
data class Errors(val errors: List<Error>)