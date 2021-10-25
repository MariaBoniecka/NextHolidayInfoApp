package pl.mariaboniecka.nextholidayinfoapp.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter @Setter
public class Holiday {
    @JsonProperty("name")
    private String name;
    @JsonProperty("date") @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

}
