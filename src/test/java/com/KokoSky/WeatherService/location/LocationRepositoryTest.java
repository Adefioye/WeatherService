package com.KokoSky.WeatherService.location;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import com.KokoSky.WeatherService.realtimeWeather.RealtimeWeather;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class LocationRepositoryTest {

    @Autowired
    private LocationRepository underTest;

    @BeforeEach
    void setUp() {
        underTest.deleteAll();
    }

    @Test
    public void whenLocationCodeExists_returnTrue() {
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

        underTest.save(location);
        boolean isLocationPresent = underTest.existsLocationByCode(location.getCode());

        assertThat(isLocationPresent).isTrue();
    }

    @Test
    public void whenLocationCodeDoesNotExist_returnFalse() {
        String nonExistentLocationCode = "BWR_NG";
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

        underTest.save(location);
        boolean isLocationPresent = underTest.existsLocationByCode(nonExistentLocationCode);

        assertThat(isLocationPresent).isFalse();
    }

    @Test
    public void whenGivenLocation_andTrashedIsTrue_returnEmptyList() {
        // When
        String code = "LACA_US";
        String cityName = "Los Angeles";
        String regionName = "California";
        String countryName = "United States Of America";
        String countryCode = "US";
        boolean enabled = true;
        boolean trashed = true;

        Location location = Location
                .builder()
                .code(code)
                .cityName(cityName)
                .regionName(regionName)
                .countryName(countryName)
                .countryCode(countryCode)
                .enabled(enabled)
                .trashed(trashed)
                .build();

        // When
        underTest.save(location);
        List<Location> allUntrashedLocations = underTest.findAllUntrashedLocations();

        // Then
        assertThat(allUntrashedLocations.size()).isEqualTo(0);
    }

    @Test
    public void whenGivenLocation_andTrashedIsFalse_returnLocation() {
        // When
        String code = "LACA_US";
        String cityName = "Los Angeles";
        String regionName = "California";
        String countryName = "United States Of America";
        String countryCode = "US";
        boolean enabled = true;
        boolean trashed = false;

        Location location = Location
                .builder()
                .code(code)
                .cityName(cityName)
                .regionName(regionName)
                .countryName(countryName)
                .countryCode(countryCode)
                .enabled(enabled)
                .trashed(trashed)
                .build();

        // When
        underTest.save(location);
        List<Location> allUntrashedLocations = underTest.findAllUntrashedLocations();

        // Then
        assertThat(allUntrashedLocations.size()).isEqualTo(1);
    }

    @Test
    public void whenGivenLocationCode_andPresent_returnLocation() {
        // When
        String code = "LACA_US";
        String cityName = "Los Angeles";
        String regionName = "California";
        String countryName = "United States Of America";
        String countryCode = "US";
        boolean enabled = true;
        boolean trashed = false;

        Location location = Location
                .builder()
                .code(code)
                .cityName(cityName)
                .regionName(regionName)
                .countryName(countryName)
                .countryCode(countryCode)
                .enabled(enabled)
                .trashed(trashed)
                .build();

        // When
        underTest.save(location);
        Optional<Location> locationByCode = underTest.findUntrashedLocationsByCode(code);

        // Then
        assertThat(locationByCode).isPresent();
    }

    @Test
    public void whenGivenLocationCode_andAbsent_returnNull() {
        // When
        String nonExistentLocationCode = "LACA_US";

        // When
        Optional<Location> locationByCode = underTest.findUntrashedLocationsByCode(nonExistentLocationCode);

        // Then
        assertThat(locationByCode).isNotPresent();
    }
    
    @Test
    public void testRealTimeWeather_addedSuccesfullyThroughLocation() {
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

        // Set both location and realtimeWeather
        location.setRealtimeWeather(realTimeWeather);
        realTimeWeather.setLocation(location);

        // When
        underTest.save(location);
        Optional<Location> savedLocation = underTest.findUntrashedLocationsByCode(locationCode);

        // Then
        assertThat(savedLocation).isPresent();
        assertThat(savedLocation.get().getRealtimeWeather().getLocationCode()).isEqualTo(locationCode);
    }
}
