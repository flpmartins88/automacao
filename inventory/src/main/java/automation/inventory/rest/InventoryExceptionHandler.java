package automation.inventory.rest;

import automation.inventory.domain.ItemNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Collections;
import java.util.List;

@ControllerAdvice
public class InventoryExceptionHandler {

    @ExceptionHandler(ItemNotFoundException.class)
    public ResponseEntity<List<Error>> itemNotFoundHandler(ItemNotFoundException exception) {
        return buildResponse(
                HttpStatus.NOT_FOUND,
                Collections.singletonList(new Error("NOT_FOUND", exception.getMessage()))
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<List<Error>> baseException(Exception exception) {
        return buildResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                Collections.singletonList(new Error("UNKNOWN_ERROR", exception.getMessage()))
        );
    }

    private ResponseEntity<List<Error>> buildResponse(HttpStatus status, List<Error> errors) {
        return ResponseEntity.status(status)
                .body(errors);
    }

    public record Error(String code, String description) {

    }

}
