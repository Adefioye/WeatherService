package com.KokoSky.WeatherService.location;

import com.KokoSky.WeatherService.exceptions.ResourceNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;


import java.util.Collections;
import java.util.List;


import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LocationController.class)
public class LocationControllerTest {

    @MockBean
    private LocationService locationService;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    public void whenPostLocation_returnCreateLocationWithStatusCode201() throws Exception {
        String code = "LACA_US";
        String cityName = "Los Angeles";
        String regionName = "California";
        String countryName = "United States Of America";
        String countryCode = "US";
        boolean enabled = true;
        Location location = Location
                .builder()
                .code(code)
                .cityName(cityName)
                .regionName(regionName)
                .countryName(countryName)
                .countryCode(countryCode)
                .enabled(enabled)
                .build();

        String ENDPOINT_URI = "/api/v1/locations";

        when(locationService.addLocation(any(Location.class))).thenReturn(location);

        mockMvc.perform(post(ENDPOINT_URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(location)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "%s/%s".formatted(ENDPOINT_URI, location.getCode())))
                .andExpect(jsonPath("$.code").value(location.getCode()))
                .andExpect(jsonPath("$.city_name").value(location.getCityName()))
                .andExpect(jsonPath("$.region_name").value(location.getRegionName()))
                .andExpect(jsonPath("$.country_name").value(location.getCountryName()))
                .andExpect(jsonPath("$.country_code").value(location.getCountryCode()))
                .andDo(print());

    }

    @Test
    public void whenPostEmptyLocation_returnStatusCode400() throws Exception {
        Location location = new Location();

        String ENDPOINT_URI = "/api/v1/locations";

        mockMvc.perform(post(ENDPOINT_URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(location)))
                .andExpect(status().isBadRequest())
                .andDo(print());

    }

    @Test
    public void whenGetLocations_andNoLocations_returnEmptyLocationWithStatusCode204() throws Exception {

        String ENDPOINT_URI = "/api/v1/locations";
        when(locationService.getLocations()).thenReturn(Collections.emptyList());

        mockMvc.perform(get(ENDPOINT_URI)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andDo(print());
    }

    @Test
    public void whenGetLocations_returnLocationsWithStatusCode200() throws Exception {
        String code = "LACA_US";
        String cityName = "Los Angeles";
        String regionName = "California";
        String countryName = "United States Of America";
        String countryCode = "US";
        boolean enabled = true;
        Location location = Location
                .builder()
                .code(code)
                .cityName(cityName)
                .regionName(regionName)
                .countryName(countryName)
                .countryCode(countryCode)
                .enabled(enabled)
                .build();

        String ENDPOINT_URI = "/api/v1/locations";
        when(locationService.getLocations()).thenReturn(List.of(location));

        mockMvc.perform(get(ENDPOINT_URI)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andDo(print());
    }

    @Test
    public void whenGetLocationByCode_andNoLocation_returnNotFoundWithStatusCode404() throws Exception {
        String nonExistentLocationCode = "LACA_US";
        String ENDPOINT_URI = "/api/v1/locations/%s";
        when(locationService.getLocationByCode(nonExistentLocationCode)).thenThrow(ResourceNotFoundException.class);

        mockMvc.perform(get(ENDPOINT_URI.formatted(nonExistentLocationCode)))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    public void whenGetLocationByCode_andLocationExists_returnLocationWithStatusCode200() throws Exception {
        String code = "LACA_US";
        String cityName = "Los Angeles";
        String regionName = "California";
        String countryName = "United States Of America";
        String countryCode = "US";
        boolean enabled = true;
        Location location = Location
                .builder()
                .code(code)
                .cityName(cityName)
                .regionName(regionName)
                .countryName(countryName)
                .countryCode(countryCode)
                .enabled(enabled)
                .build();

        String ENDPOINT_URI = "/api/v1/locations/%s";
        when(locationService.getLocationByCode(code)).thenReturn(location);

        mockMvc.perform(get(ENDPOINT_URI.formatted(code)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(location.getCode()))
                .andExpect(jsonPath("$.city_name").value(location.getCityName()))
                .andExpect(jsonPath("$.region_name").value(location.getRegionName()))
                .andExpect(jsonPath("$.country_name").value(location.getCountryName()))
                .andExpect(jsonPath("$.country_code").value(location.getCountryCode()))
                .andDo(print());
    }
}
