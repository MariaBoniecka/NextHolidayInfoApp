package pl.mariaboniecka.nextholidayinfoapp.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import pl.mariaboniecka.nextholidayinfoapp.NextHolidayInfoApp;
import pl.mariaboniecka.nextholidayinfoapp.exceptions.CountryNotFoundException;
import pl.mariaboniecka.nextholidayinfoapp.exceptions.DateNotSupportedException;
import pl.mariaboniecka.nextholidayinfoapp.model.NextHolidayResponse;
import pl.mariaboniecka.nextholidayinfoapp.service.holidayapiclient.HolidayAPIClient;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(SpringExtension.class)
@SpringBootTest(
        classes = NextHolidayInfoApp.class,
        webEnvironment = SpringBootTest.WebEnvironment.NONE
)
public class NextHolidayInfoServiceTests {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private NextHolidayInfoService nextHolidayInfoService;
    @MockBean
    private SupportedCountryCodes supportedCountryCodes;
    @MockBean
    private HolidayAPIClient holidayAPIClient;

    @Test
    public void givenValidData_whenGetHolidays_thenReturnHolidayResponse() throws JsonProcessingException {
        //given
        LocalDate dateOneYearAgo = LocalDate.now().minusYears(1);
        String polishHolidayName = "PolishHolidayName";
        String britishHolidayName = "BritishHolidayName";
        Mockito.when(supportedCountryCodes.getSupportedCountryCodes())
                .thenReturn(List.of("PL", "GB", "DE"));
        Mockito.when(holidayAPIClient.getFirstHolidayWithDateAndCountry(eq(dateOneYearAgo), eq("PL"), eq(dateOneYearAgo.getYear())))
                .thenReturn(polishHolidayName);
        Mockito.when(holidayAPIClient.getFirstHolidayWithDateAndCountry(eq(dateOneYearAgo), eq("GB"), eq(dateOneYearAgo.getYear())))
                .thenReturn(britishHolidayName);
        //when
        NextHolidayResponse nextHolidayResponse = nextHolidayInfoService.getHolidays(dateOneYearAgo, "PL", "GB");
        //then
        Assertions.assertThat(nextHolidayResponse.getDate()).isEqualTo(dateOneYearAgo);
        Assertions.assertThat(nextHolidayResponse.getHoliday1()).isEqualTo(polishHolidayName);
        Assertions.assertThat(nextHolidayResponse.getHoliday2()).isEqualTo(britishHolidayName);
    }

    @Test
    public void givenInvalidDate_whenGetHolidays_thenThrowException() throws JsonProcessingException {
        //given
        LocalDate today = LocalDate.now();
        //when
        Throwable throwable = Assertions.catchThrowable(() ->  nextHolidayInfoService.getHolidays(today, "PL", "GB"));
        //then
        Assertions.assertThat(throwable).isNotNull();
        Assertions.assertThat(throwable).isInstanceOf(DateNotSupportedException.class);
        Assertions.assertThat(throwable.getMessage()).isEqualTo("Date not supported - you can only see holidays from the previous year");
    }

    @Test
    public void givenInvalidCountryCode_whenGetHolidays_thenThrowException() throws JsonProcessingException {
        //given
        LocalDate dateOneYearAgo = LocalDate.now().minusYears(1);
        Mockito.when(supportedCountryCodes.getSupportedCountryCodes())
                .thenReturn(List.of("PL", "GB", "DE"));
        //when
        Throwable throwable = Assertions.catchThrowable(() ->  nextHolidayInfoService.getHolidays(dateOneYearAgo, "UU", "GB"));
        //then
        Assertions.assertThat(throwable).isNotNull();
        Assertions.assertThat(throwable).isInstanceOf(CountryNotFoundException.class);
        Assertions.assertThat(throwable.getMessage()).isEqualTo("Country UU not found");
    }
}
