package pl.mariaboniecka.nextholidayinfoapp.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.coyote.Response;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import pl.mariaboniecka.nextholidayinfoapp.NextHolidayInfoApp;
import pl.mariaboniecka.nextholidayinfoapp.exceptions.CountryNotFoundException;
import pl.mariaboniecka.nextholidayinfoapp.exceptions.DateNotSupportedException;
import pl.mariaboniecka.nextholidayinfoapp.exceptions.ErrorDescription;
import pl.mariaboniecka.nextholidayinfoapp.model.NextHolidayResponse;
import pl.mariaboniecka.nextholidayinfoapp.service.NextHolidayInfoService;

import java.time.LocalDate;


@ExtendWith(SpringExtension.class)
@SpringBootTest(
        classes = NextHolidayInfoApp.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
public class NextHolidayInfoControllerTests {
    @Autowired
    private TestRestTemplate testRestTemplate;
    @Autowired
    private NextHolidayInfoController nextHolidayInfoController;
    @MockBean
    private NextHolidayInfoService nextHolidayInfoService;
    @LocalServerPort
    private int port;

    private String baseUrl;

    @BeforeEach
    void setup() {
        baseUrl = "http://localhost:" + port;
    }

    @Test
    public void givenValidData_whenGetNextHolidays_thenReturnNextHolidayResponseBody() throws JsonProcessingException {
        //given
        LocalDate dateOneYearAgo = LocalDate.now().minusYears(1);
        String polishHolidayName = "PolishHolidayName";
        String britishHolidayName = "BritishHolidayName";
        NextHolidayResponse nextHolidayResponse = new NextHolidayResponse();
        nextHolidayResponse.setDate(dateOneYearAgo);
        nextHolidayResponse.setHoliday1(polishHolidayName);
        nextHolidayResponse.setHoliday2(britishHolidayName);
       Mockito.when(nextHolidayInfoService.getHolidays(dateOneYearAgo, "PL", "GB"))
               .thenReturn(nextHolidayResponse);
        //when
        ResponseEntity<NextHolidayResponse> responseEntity = testRestTemplate.getForEntity(
                baseUrl + "/nextHolidays?date=" + dateOneYearAgo + "&country1=PL&country2=GB", NextHolidayResponse.class);
        //then
        Assertions.assertThat(responseEntity.getBody().getDate()).isEqualTo(dateOneYearAgo);
        Assertions.assertThat(responseEntity.getBody().getHoliday1()).isEqualTo(polishHolidayName);
        Assertions.assertThat(responseEntity.getBody().getHoliday2()).isEqualTo(britishHolidayName);
    }

    @Test
    public void givenValidData_whenGetNextHolidays_thenReturnStatusCode200() throws JsonProcessingException {
        //given
        LocalDate dateOneYearAgo = LocalDate.now().minusYears(1);
        String polishHolidayName = "PolishHolidayName";
        String britishHolidayName = "BritishHolidayName";
        NextHolidayResponse nextHolidayResponse = new NextHolidayResponse();
        nextHolidayResponse.setDate(dateOneYearAgo);
        nextHolidayResponse.setHoliday1(polishHolidayName);
        nextHolidayResponse.setHoliday2(britishHolidayName);
        Mockito.when(nextHolidayInfoService.getHolidays(dateOneYearAgo, "PL", "GB"))
                .thenReturn(nextHolidayResponse);
        //when
        ResponseEntity<NextHolidayResponse> responseEntity = testRestTemplate.getForEntity(
                baseUrl + "/nextHolidays?date=" + dateOneYearAgo + "&country1=PL&country2=GB", NextHolidayResponse.class);
        //then
        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void givenInvalidDate_whenGetNextHolidays_thenReturnBadRequest() throws JsonProcessingException {
        //given
        LocalDate today = LocalDate.now();
        Mockito.when(nextHolidayInfoService.getHolidays(today, "PL", "GB"))
                .thenThrow(new DateNotSupportedException("Date not supported - you can only see holidays from the previous year"));
        //when
        ResponseEntity<NextHolidayResponse> responseEntity = testRestTemplate.getForEntity(
                baseUrl + "/nextHolidays?date=" + today + "&country1=PL&country2=GB", NextHolidayResponse.class);
        //then
        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void givenInvalidDate_whenGetNextHolidays_thenReturnValidErrorData() throws JsonProcessingException {
        //given
        LocalDate today = LocalDate.now();
        Mockito.when(nextHolidayInfoService.getHolidays(today, "PL", "GB"))
                .thenThrow(new DateNotSupportedException("Date not supported - you can only see holidays from the previous year"));
        //when
        ResponseEntity<ErrorDescription> responseEntity = testRestTemplate.getForEntity(
                baseUrl + "/nextHolidays?date=" + today + "&country1=PL&country2=GB", ErrorDescription.class);
        //then
        Assertions.assertThat(responseEntity.getBody().getErrorReason()).isEqualTo("Date not supported - you can only see holidays from the previous year");
    }

    @Test
    public void givenInvalidCountryCode_whenGetNextHolidays_thenReturnBadRequest() throws JsonProcessingException {
        //given
        LocalDate dateOneYearAgo = LocalDate.now().minusYears(1);
        Mockito.when(nextHolidayInfoService.getHolidays(dateOneYearAgo, "UU", "GB"))
                .thenThrow(new CountryNotFoundException("Country UU not found"));
        //when
        ResponseEntity<NextHolidayResponse> responseEntity = testRestTemplate.getForEntity(
                baseUrl + "/nextHolidays?date=" + dateOneYearAgo + "&country1=UU&country2=GB", NextHolidayResponse.class);
        //then
        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void givenInvalidCountryCode_whenGetNextHolidays_thenReturnValidErrorData() throws JsonProcessingException {
        //given
        LocalDate dateOneYearAgo = LocalDate.now().minusYears(1);
        Mockito.when(nextHolidayInfoService.getHolidays(dateOneYearAgo, "UU", "GB"))
                .thenThrow(new CountryNotFoundException("Country UU not found"));
        //when
        ResponseEntity<ErrorDescription> responseEntity = testRestTemplate.getForEntity(
                baseUrl + "/nextHolidays?date=" + dateOneYearAgo + "&country1=UU&country2=GB", ErrorDescription.class);
        //then
        Assertions.assertThat(responseEntity.getBody().getErrorReason()).isEqualTo("Country UU not found");
    }

    @Test
    public void givenMissingRequiredRequestParameter_whenGetNextHolidays_thenThrow() {
        //given
        LocalDate dateOneYearAgo = LocalDate.now().minusYears(1);
        //when
        ResponseEntity<ErrorDescription> responseEntity = testRestTemplate.getForEntity(
                baseUrl + "/nextHolidays?date=" + dateOneYearAgo + "&country1=PL", ErrorDescription.class);
        //then
        Assertions.assertThat(responseEntity.getBody().getErrorReason()).isEqualTo("Missing parameter");
    }

}
