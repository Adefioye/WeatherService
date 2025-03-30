package com.KokoSky.WeatherService.realtimeWeather;

import com.KokoSky.WeatherService.exceptions.GeolocationException;
import com.KokoSky.WeatherService.exceptions.LocationNotFoundException;
import com.KokoSky.WeatherService.geolocation.GeolocationService;
import com.KokoSky.WeatherService.location.Location;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RealtimeWeatherController.class)
public class RealtimeWeatherControllerTest {

    @MockBean
    private RealtimeWeatherService realWeatherService;

    @MockBean
    private GeolocationService geolocationService;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    private static final String END_POINT_PATH = "/api/v1/realtime";

    @Test
    public void testGETShouldReturnBadRequest_with400StatusCode() throws Exception {
        when(geolocationService.getLocation(anyString())).thenThrow(GeolocationException.class);

        mockMvc.perform(get(END_POINT_PATH))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    public void testGETShouldReturnNotFound_with404StatusCode_whenLocationNotFound() throws Exception {
        Location location = new Location();

        when(geolocationService.getLocation(anyString())).thenReturn(location);
        when(realWeatherService.getByLocation(location)).thenThrow(LocationNotFoundException.class);

        mockMvc.perform(get(END_POINT_PATH))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    public void testGETShouldReturnSuccess_with200StatusCode() throws Exception {
        // Given
        String locationCode = "LACA_US";
        String cityName = "Los Angeles";
        String regionName = "California";
        String countryName = "United States Of America";
        String countryCode = "US";
        boolean enabled = true;
        boolean trashed = false;


        Location location = Location
                .builder()
                .code(locationCode)
                .cityName(cityName)
                .regionName(regionName)
                .countryName(countryName)
                .countryCode(countryCode)
                .enabled(enabled)
                .trashed(trashed)
                .build();

        RealtimeWeather realTimeWeather = RealtimeWeather
                .builder()
                .temperature(75)
                .humidity(50)
                .precipitation(1015)
                .windSpeed(57)
                .status("Snowy")
                .lastUpdated(new Date())
                .build();

        // Set location on realtimeWeather
        location.setRealtimeWeather(realTimeWeather);
        realTimeWeather.setLocation(location);

//        String expectedLocation = location.getCityName() + ", " + location.getRegionName() + ", " + location.getCountryName();

        when(geolocationService.getLocation(anyString())).thenReturn(location);
        when(realWeatherService.getByLocation(location)).thenReturn(realTimeWeather);

        mockMvc.perform(get(END_POINT_PATH))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.location.city_name").value(cityName))
                .andExpect(jsonPath("$.location.region_name").value(regionName))
                .andExpect(jsonPath("$.location.country_name").value(countryName))
                .andDo(print());
    }
}
