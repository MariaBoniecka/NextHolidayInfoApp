package pl.mariaboniecka.nextholidayinfoapp.service.holidayapiclient;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import pl.mariaboniecka.nextholidayinfoapp.model.Holiday;

import java.util.ArrayList;

@Data
class HolidayAPIResponse {
    @JsonProperty("holidays")
    private ArrayList<Holiday> listOfHoliday;
}
