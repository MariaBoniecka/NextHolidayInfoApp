package pl.mariaboniecka.nextholidayinfoapp.exceptions;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
public class ErrorDescription {

    private String errorReason;

    private Instant date;

    public ErrorDescription(String errorReason) {
        this.errorReason = errorReason;
    }

}
