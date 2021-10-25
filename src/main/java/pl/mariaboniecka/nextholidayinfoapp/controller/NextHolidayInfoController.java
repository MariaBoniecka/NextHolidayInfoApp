package pl.mariaboniecka.nextholidayinfoapp.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.mariaboniecka.nextholidayinfoapp.model.NextHolidayResponse;
import pl.mariaboniecka.nextholidayinfoapp.service.NextHolidayInfoService;

import java.time.LocalDate;

@RestController
@AllArgsConstructor
public class NextHolidayInfoController {
    @Autowired
    private NextHolidayInfoService holidayService;

    @GetMapping(value = "/nextHolidays")
    public ResponseEntity<NextHolidayResponse> getNextHolidays(
            @RequestParam @DateTimeFormat(pattern="yyyy-MM-dd") LocalDate date,
            @RequestParam String country1,
            @RequestParam String country2) throws JsonProcessingException {

        return ResponseEntity.ok()
                .body(holidayService.getHolidays(date, country1, country2));
    }
}
