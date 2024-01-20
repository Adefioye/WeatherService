package com.KokoSky.WeatherService.location;

import com.KokoSky.WeatherService.exceptions.DuplicateResourceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LocationServiceTest {

    @InjectMocks
    private LocationService underTest;

    @Mock
    private LocationRepository locationRepository;

    @BeforeEach
    void setUp() {
        locationRepository.deleteAll();
    }

    @Test
    public void whenAddLocation_returnNotNull() {

        // Given
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

        when(locationRepository.existsLocationByCode(code)).thenReturn(false);
        when(locationRepository.save(location)).thenReturn(location);

        // When
        Location newLocation = underTest.addLocation(location);

        assertThat(newLocation).isNotNull();
    }

    @Test
    public void whenLocationExists_throwDuplicateResourceException() {

        // Given
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

        when(locationRepository.existsLocationByCode(code)).thenReturn(true);

        // When
        assertThatThrownBy(() -> underTest.addLocation(location))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessage("Sorry! location code %s already exist!".formatted(code));

        verify(locationRepository, times(1)).existsLocationByCode(code);
        verify(locationRepository, never()).save(any(Location.class));

    }
}
