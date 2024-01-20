package com.KokoSky.WeatherService.location;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;


@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class LocationRepositoryTest {

    @Autowired
    private LocationRepository locationRepository;

    @BeforeEach
    void setUp() {
        locationRepository.deleteAll();
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

        locationRepository.save(location);
        boolean isLocationPresent = locationRepository.existsLocationByCode(location.getCode());

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

        locationRepository.save(location);
        boolean isLocationPresent = locationRepository.existsLocationByCode(nonExistentLocationCode);

        assertThat(isLocationPresent).isFalse();
    }
}
