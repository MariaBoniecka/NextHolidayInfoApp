package pl.mariaboniecka.nextholidayinfoapp.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.mariaboniecka.nextholidayinfoapp.service.holidayapiclient.HolidayAPIClient;

import java.util.List;

@ExtendWith(MockitoExtension.class)
public class SupportedCountryCodesTests {

    @Mock
    private HolidayAPIClient holidayAPIClient;
    @InjectMocks
    private SupportedCountryCodes supportedCountryCodes;

    @Test
    public void givenEmptyCountryCodesList_whenGetSupportedCountryCodes_thenReturnListOfCountryCodes() throws JsonProcessingException {
        //given
        Mockito.when(holidayAPIClient.fetchCountryCodes())
                .thenReturn(List.of("PL", "GB", "DE"));
        //when
        var listOfCountryCodes = supportedCountryCodes.getSupportedCountryCodes();
        //then
        Assertions.assertThat(listOfCountryCodes).hasSize(3);
        Assertions.assertThat(listOfCountryCodes).contains("PL");
        Assertions.assertThat(listOfCountryCodes).contains("GB");
        Assertions.assertThat(listOfCountryCodes).contains("DE");
    }

    @Test
    public void givenNotEmptyCountryCodesList_whenGetSupportedCountryCodes_thenReturnListOfCountryCodes() throws JsonProcessingException {
        //given
        supportedCountryCodes.setCountryCodes(List.of("PL", "FR"));
        //when
        var listOfCountryCodes = supportedCountryCodes.getSupportedCountryCodes();
        //then
        Assertions.assertThat(listOfCountryCodes).hasSize(2);
        Assertions.assertThat(listOfCountryCodes).contains("PL");
        Assertions.assertThat(listOfCountryCodes).contains("FR");
    }
}
