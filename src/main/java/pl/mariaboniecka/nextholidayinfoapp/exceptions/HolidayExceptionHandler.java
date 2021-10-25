package pl.mariaboniecka.nextholidayinfoapp.exceptions;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.DateTimeException;
import java.time.Instant;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class HolidayExceptionHandler {

    @ExceptionHandler(CountryNotFoundException.class)
    public ResponseEntity<ErrorDescription> handleCountryNotFoundException(final CountryNotFoundException exception) {
        ErrorDescription errorDescription = new ErrorDescription();
        errorDescription.setErrorReason(exception.getMessage());
        errorDescription.setDate(Instant.now());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(errorDescription);
    }

    @ExceptionHandler(DateNotSupportedException.class)
    public ResponseEntity<ErrorDescription> handleDateNotSupportedException(final DateNotSupportedException exception) {
        ErrorDescription errorDescription = new ErrorDescription();
        errorDescription.setErrorReason(exception.getMessage());
        errorDescription.setDate(Instant.now());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(errorDescription);
    }

    @ExceptionHandler(DateTimeException.class)
    public ResponseEntity<ErrorDescription> handleDateTimeException(final DateTimeException exception) {
        ErrorDescription errorDescription = new ErrorDescription();
        errorDescription.setDate(Instant.now());
        errorDescription.setErrorReason(("Provided date has invalid format - use format yyyy-MM-dd"));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(errorDescription);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorDescription> handleMissingServletRequestParameterException(final MissingServletRequestParameterException exception) {
        ErrorDescription errorDescription = new ErrorDescription();
        errorDescription.setDate(Instant.now());
        errorDescription.setErrorReason("Missing parameter");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(errorDescription);
    }

}
