package pl.mariaboniecka.nextholidayinfoapp.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDate;

@Data
public class NextHolidayResponse {
    private LocalDate date;
    @JsonProperty("name1")
    private String holiday1;
    @JsonProperty("name2")
    private String holiday2;
}
