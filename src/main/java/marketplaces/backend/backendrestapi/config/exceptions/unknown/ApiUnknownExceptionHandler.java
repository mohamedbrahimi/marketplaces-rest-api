package marketplaces.backend.backendrestapi.config.exceptions.unknown;

import marketplaces.backend.backendrestapi.config.exceptions.ApiException;
import marketplaces.backend.backendrestapi.config.exceptions.custom.ApiRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@ControllerAdvice
public class ApiUnknownExceptionHandler {
    @ExceptionHandler(value = {ApiRequestException.class})
    public ResponseEntity<Object> handleApiRequestException(ApiRequestException e) {
        // 1. Create payload containing exception details
        final HttpStatus badRequest = HttpStatus.BAD_REQUEST;
        final ApiException apiException = new ApiException(
              e.getApiExceptionMessage(),
              badRequest,
              ZonedDateTime.now(ZoneId.of("Z"))
      );
        // 2. Return response entity

        return new ResponseEntity<>(apiException, badRequest);
    }
}
