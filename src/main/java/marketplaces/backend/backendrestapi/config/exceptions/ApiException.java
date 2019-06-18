package marketplaces.backend.backendrestapi.config.exceptions;

import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;

public class ApiException {
    private final ApiExceptionMessage message;
    private final HttpStatus httpStatus;
    private final ZonedDateTime timesTamp;


    public ApiException(ApiExceptionMessage message, HttpStatus httpStatus, ZonedDateTime timesTamp) {
        this.message = message;
        this.httpStatus = httpStatus;
        this.timesTamp = timesTamp;
    }

    public ApiExceptionMessage getMessage() {
        return message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public ZonedDateTime getTimesTamp() {
        return timesTamp;
    }


}
