package pl.mariaboniecka.nextholidayinfoapp.service.holidayapiclient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

@ExtendWith(MockitoExtension.class)
public class HolidayAPIClientTests {

    @Mock
    private RestTemplate restTemplate;
    @InjectMocks
    private HolidayAPIClient holidayAPIClient;

    @BeforeEach
    public void setup() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        holidayAPIClient.setObjectMapper(objectMapper);
    }
    @Test
    public void givenNoIssue_whenFetchCountryCodes_thenReturnListOfCountryCodes() throws JsonProcessingException {
        //given
        String json = "{\"countries\": [{\"code\":\"PL\"}, {\"code\":\"US\"}, {\"code\":\"FR\"}] }";
        Mockito.when(restTemplate.getForEntity(anyString(), any()))
                .thenReturn(ResponseEntity.ok(json));
        //when
        var listOfCodes = holidayAPIClient.fetchCountryCodes();
        //then
        Assertions.assertThat(listOfCodes).hasSize(3);
        Assertions.assertThat(listOfCodes).contains("PL");
        Assertions.assertThat(listOfCodes).contains("US");
        Assertions.assertThat(listOfCodes).contains("FR");
    }

    @Test
    public void givenNoIssues_whenGetFirstHolidayWithDateAndCountry_thenReturnHoliday() throws JsonProcessingException {
        //given
        String json = "{\"holidays\": [{\"name\":\"Holiday1\", \"date\":\"2020-01-05\"}, {\"name\":\"Holiday2\", \"date\":\"2020-11-05\"}, {\"name\":\"Holiday3\", \"date\":\"2020-12-10\"}]}";
        Mockito.when(restTemplate.getForEntity(anyString(), any()))
                .thenReturn(ResponseEntity.ok(json));
        //when
        String holiday = holidayAPIClient.getFirstHolidayWithDateAndCountry(LocalDate.now().minusYears(1), "PL", 2020);
        //then
        Assertions.assertThat(holiday).isEqualTo("Holiday2");
    }
}
