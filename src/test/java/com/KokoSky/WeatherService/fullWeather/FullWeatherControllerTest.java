package com.KokoSky.WeatherService.fullWeather;

import com.KokoSky.WeatherService.dailyWeather.DailyWeather;
import com.KokoSky.WeatherService.dailyWeather.DailyWeatherId;
import com.KokoSky.WeatherService.exceptions.GeolocationException;
import com.KokoSky.WeatherService.exceptions.LocationNotFoundException;
import com.KokoSky.WeatherService.geolocation.GeolocationService;
import com.KokoSky.WeatherService.hourlyWeather.HourlyWeather;
import com.KokoSky.WeatherService.hourlyWeather.HourlyWeatherId;
import com.KokoSky.WeatherService.location.Location;
import com.KokoSky.WeatherService.realtimeWeather.RealtimeWeather;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FullWeatherController.class)
public class FullWeatherControllerTest {

    private static final String END_POINT_PATH = "/api/v1/full";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private FullWeatherService fullWeatherService;

    @MockBean
    private GeolocationService geolocationService;

    @Test
    public void testGetByIPShouldReturn400BadRequestBecauseGeolocationException() throws Exception {
        GeolocationException ex = new GeolocationException("Geolocation error");
        when(geolocationService.getLocation(anyString())).thenThrow(ex);

        mockMvc.perform(get(END_POINT_PATH))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is(ex.getMessage())))
                .andDo(print());
    }

    @Test
    public void testGetByIPShouldReturn404NotFound() throws Exception {
        Location location = Location.builder().code("DELHI_IN").build();

        when(geolocationService.getLocation(anyString())).thenReturn(location);

        LocationNotFoundException ex = new LocationNotFoundException(location.getCode());
        when(fullWeatherService.getByLocation(location)).thenThrow(ex);

        mockMvc.perform(get(END_POINT_PATH))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", is(ex.getMessage())))
                .andDo(print());
    }

    @Test
    public void testGetByIPShouldReturn200OK() throws Exception {
        Location location = Location.builder()
                .code("NYC_USA")
                .cityName("New York City")
                .regionName("New York")
                .countryCode("US")
                .countryName("United States of America")
                .build();

        RealtimeWeather realtimeWeather = RealtimeWeather.builder()
                .temperature(12)
                .humidity(32)
                .lastUpdated(new Date())
                .precipitation(88)
                .status("Cloudy")
                .windSpeed(5)
                .location(location) // Also sets locationCode in setter
                .build();

        location.setRealtimeWeather(realtimeWeather);

        DailyWeatherId id1 = DailyWeatherId
                .builder()
                .month(7)
                .dayOfMonth(16)
                .location(location)
                .build();

        DailyWeatherId id2 = DailyWeatherId
                .builder()
                .month(7)
                .dayOfMonth(17)
                .location(location)
                .build();

        DailyWeather dailyForecast1 = DailyWeather.builder()
                .id(id1)
                .minTemp(23)
                .maxTemp(32)
                .precipitation(40)
                .status("Cloudy")
                .build();

        DailyWeather dailyForecast2 = DailyWeather.builder()
                .id(id2)
                .minTemp(25)
                .maxTemp(34)
                .precipitation(30)
                .status("Sunny")
                .build();

        location.setListDailyWeather(List.of(dailyForecast1, dailyForecast2));

        HourlyWeather hourlyForecast1 = HourlyWeather.builder()
                .id(HourlyWeatherId.builder().location(location).hourOfDay(10).build())
                .temperature(13)
                .precipitation(70)
                .status("Cloudy")
                .build();

        HourlyWeather hourlyForecast2 = HourlyWeather.builder()
                .id(HourlyWeatherId.builder().location(location).hourOfDay(11).build())
                .temperature(15)
                .precipitation(60)
                .status("Sunny")
                .build();

        location.setListHourlyWeather(List.of(hourlyForecast1, hourlyForecast2));

        when(geolocationService.getLocation(anyString())).thenReturn(location);
        when(fullWeatherService.getByLocation(location)).thenReturn(location);

        String expectedLocation = location.toString();

        mockMvc.perform(get(END_POINT_PATH))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.location", is(expectedLocation)))
                .andExpect(jsonPath("$.realtime_weather.temperature", is(12)))
                .andExpect(jsonPath("$.hourly_forecast[0].hour_of_day", is(10)))
                .andExpect(jsonPath("$.daily_forecast[0].precipitation", is(40)))
                .andDo(print());
    }

}
