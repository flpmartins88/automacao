package automation.order.infrastructure.web;

import automation.order.commons.ApiException;
import automation.order.domain.ItemNotFoundException;
import automation.order.domain.OrderNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ApplicationExceptionHandler {

    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<List<ErrorMap>> handleOrderNotFound(OrderNotFoundException exception) {
        return new Errors()
                .add(buildErrorMap(exception).add("order", exception.getOrderCode()))
                .toResponseEntity(exception.getStatus());
    }

    @ExceptionHandler(ItemNotFoundException.class)
    public ResponseEntity<List<ErrorMap>> handleItemNotFound(ItemNotFoundException exception) {
        return new Errors()
                .add(buildErrorMap(exception).add("item", exception.getItemId()))
                .toResponseEntity(exception.getStatus());
    }

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<List<ErrorMap>> handleApiException(ApiException exception) {
        return new Errors()
                .add(buildErrorMap(exception))
                .toResponseEntity(exception.getStatus());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<List<ErrorMap>> handleHttpMessageNotReadable(HttpMessageNotReadableException exception) {
        return new Errors()
                .add(new ErrorMap().add("error_code", "invalid_request_body"))
                .toResponseEntity(HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<ErrorMap>> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        var errorList = exception.getBindingResult().getFieldErrors().stream()
                .map(error -> new ErrorMap()
                        .add("error_code", "invalid_value")
                        .add("field", error.getField())
                        .add("message", error.getDefaultMessage()))
                .collect(Collectors.toList());

        return new Errors()
                .addAll(errorList)
                .toResponseEntity(HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleAll(Exception exception) {
        return ResponseEntity.internalServerError().body(exception.getMessage());
    }


    private ErrorMap buildErrorMap(ApiException exception) {
        return new ErrorMap().add("error_code", exception.getErrorCode());
    }
}

class Errors {
    private final List<ErrorMap> errors = new ArrayList<>();

    public Errors add(ErrorMap error) {
        errors.add(error);
        return this;
    }

    public ResponseEntity<List<ErrorMap>> toResponseEntity(HttpStatus httpStatus) {
        return ResponseEntity.status(httpStatus).body(errors);
    }

    public Errors addAll(List<ErrorMap> errors) {
        this.errors.addAll(errors);
        return this;
    }
}

class ErrorMap extends HashMap<String, Object> {
    public ErrorMap add(String key, Object value) {
        this.put(key, value);
        return this;
    }
}
