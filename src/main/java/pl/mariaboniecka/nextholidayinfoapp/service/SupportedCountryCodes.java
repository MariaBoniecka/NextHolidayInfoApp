package pl.mariaboniecka.nextholidayinfoapp.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.mariaboniecka.nextholidayinfoapp.service.holidayapiclient.HolidayAPIClient;

import java.util.ArrayList;
import java.util.List;

@Component
@Data
class SupportedCountryCodes {
    @Autowired
    private HolidayAPIClient holidayAPIClient;
    private List<String> countryCodes = new ArrayList<>();

    public List<String> getSupportedCountryCodes() throws JsonProcessingException {
        if (countryCodes.isEmpty()) {
            countryCodes.addAll(holidayAPIClient.fetchCountryCodes());
        }
        return countryCodes;
    }
}