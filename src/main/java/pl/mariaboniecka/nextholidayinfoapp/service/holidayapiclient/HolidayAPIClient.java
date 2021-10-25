package pl.mariaboniecka.nextholidayinfoapp.service.holidayapiclient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import pl.mariaboniecka.nextholidayinfoapp.model.Holiday;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Setter
@RequiredArgsConstructor
public class HolidayAPIClient {
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private ObjectMapper objectMapper;
    @Value("${holidayapi.countriesurl}")
    private String countriesUrl;
    @Value("${holidayapi.holidaysurl}")
    private String holidaysUrl;
    @Value("${holidayapi.apikey}")
    private String apiKey;

    public List<String> fetchCountryCodes() throws JsonProcessingException {
        String resourceUrl = countriesUrl + "?key=" + apiKey;
        List<String> listOfCountryCodes = new ArrayList<String>();
        ResponseEntity<String> response = restTemplate.getForEntity(resourceUrl, String.class);
        JsonNode jsonNode = objectMapper.readTree(response.getBody())
                .get("countries");
        for (int i = 0; i < jsonNode.size(); i++) {
            var country = jsonNode.get(i);
            listOfCountryCodes.add(country.get("code").asText());
        }
        return listOfCountryCodes;
    }

    public String getFirstHolidayWithDateAndCountry(LocalDate date, String countryCode, int yearOfCheckedDate) throws JsonProcessingException {
        String requestUrl
                = holidaysUrl + "?key=" + apiKey + "&country=" + countryCode + "&year=" + yearOfCheckedDate;
        ResponseEntity<String> response
                = restTemplate.getForEntity(requestUrl, String.class);
        HolidayAPIResponse theResponse = objectMapper.readValue(response.getBody(), HolidayAPIResponse.class);
        Optional<Holiday> holidaysAfterDate = theResponse.getListOfHoliday()
                .stream()
                .filter(h -> h.getDate().isAfter(date))
                .findFirst();

        return holidaysAfterDate.get().getName();
    }
}
