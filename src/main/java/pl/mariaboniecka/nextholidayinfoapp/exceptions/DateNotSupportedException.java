package pl.mariaboniecka.nextholidayinfoapp.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
@Getter
public class DateNotSupportedException extends RuntimeException {
    public DateNotSupportedException(String message) {
        super(message);
    }
}
