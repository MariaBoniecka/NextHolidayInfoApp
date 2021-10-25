package pl.mariaboniecka.nextholidayinfoapp.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.mariaboniecka.nextholidayinfoapp.exceptions.CountryNotFoundException;
import pl.mariaboniecka.nextholidayinfoapp.exceptions.DateNotSupportedException;
import pl.mariaboniecka.nextholidayinfoapp.model.NextHolidayResponse;
import pl.mariaboniecka.nextholidayinfoapp.service.holidayapiclient.HolidayAPIClient;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class NextHolidayInfoService {

    @Autowired
    private HolidayAPIClient holidayAPIClient;
    @Autowired
    private SupportedCountryCodes supportedCountryCodes;
    @Autowired
    private ObjectMapper objectMapper;

    public NextHolidayResponse getHolidays(LocalDate date, String country1, String country2) throws JsonProcessingException {
        int yearOfCheckedDate = date.plusDays(1).getYear();
        int previousYear = LocalDate.now().minusYears(1).getYear();
        if (yearOfCheckedDate != previousYear) {
            throw new DateNotSupportedException("Date not supported - you can only see holidays from the previous year");
        }

        NextHolidayResponse holidayResponse = new NextHolidayResponse();
        holidayResponse.setDate(date);
        List<String> listOfCountryCodes = supportedCountryCodes.getSupportedCountryCodes();

        holidayResponse.setHoliday1(getNextHolidayForCountry(country1, date, yearOfCheckedDate, listOfCountryCodes));
        holidayResponse.setHoliday2(getNextHolidayForCountry(country2, date, yearOfCheckedDate, listOfCountryCodes));

        return holidayResponse;
    }

    private String getNextHolidayForCountry(String countryCode, LocalDate date, int yearOfCheckedDate, List<String> listOfCountryCodes) throws JsonProcessingException {
        if (listOfCountryCodes.contains(countryCode)) {
           return holidayAPIClient.getFirstHolidayWithDateAndCountry(date, countryCode, yearOfCheckedDate);
        }
        else {
            throw new CountryNotFoundException("Country " + countryCode + " not found");
        }
    }
}
