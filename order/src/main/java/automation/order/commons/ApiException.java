package automation.order.commons;

import org.springframework.http.HttpStatus;

public class ApiException extends Exception {

    private String errorCode;
    private HttpStatus status;

    public ApiException(String errorCode, HttpStatus status) {
        this.errorCode = errorCode;
        this.status = status;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public HttpStatus getStatus() {
        return status;
    }
}

