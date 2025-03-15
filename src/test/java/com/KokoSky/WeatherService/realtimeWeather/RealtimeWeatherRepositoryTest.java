package com.KokoSky.WeatherService.realtimeWeather;


import com.KokoSky.WeatherService.location.Location;
import com.KokoSky.WeatherService.location.LocationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class RealtimeWeatherRepositoryTest {

    @Autowired
    private RealtimeWeatherRepository underTest;

    @Autowired
    private LocationRepository locationRepository;

    @BeforeEach
    void setUp() {
        underTest.deleteAll();
        locationRepository.deleteAll();
    }

    @Test
    public void testRealtimeWeather_isSavedSuccessfully() {
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

        // Save location
        locationRepository.save(location);

        RealtimeWeather updatedRealtimeWeather = underTest.save(realTimeWeather);

        assertThat(updatedRealtimeWeather.getLocationCode()).isEqualTo(locationCode);
    }
}
