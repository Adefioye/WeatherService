package com.KokoSky.WeatherService.hourlyWeather;

import com.KokoSky.WeatherService.location.Location;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class HourlyWeatherRepositoryTest {

    @Autowired
    private HourlyWeatherRepository underTest;

    @BeforeEach
    void setUp() { underTest.deleteAll(); }

    @Test
    public void testAdd() {
        String locationCode = "DELHI_IN";
        int hourOfDay = 12;

        Location location = Location.builder().code(locationCode).build();
        HourlyWeatherId hourlyWeatherId = HourlyWeatherId
                .builder()
                .location(location)
                .hourOfDay(hourOfDay)
                .build();

        HourlyWeather forecast = HourlyWeather
                .builder()
                .id(hourlyWeatherId)
                .temperature(13)
                .precipitation(70)
                .status("Cloudy")
                .build();

        HourlyWeather updatedForecast = underTest.save(forecast);

        assertThat(updatedForecast.getId().getLocation().getCode()).isEqualTo(locationCode);
        assertThat(updatedForecast.getId().getHourOfDay()).isEqualTo(hourOfDay);
    }

    @Test
    public void testDelete() {
        String locationCode = "DELHI_IN";
        int hourOfDay = 12;

        Location location = Location.builder().code(locationCode).build();
        HourlyWeatherId hourlyWeatherId = HourlyWeatherId
                .builder()
                .location(location)
                .hourOfDay(hourOfDay)
                .build();

        HourlyWeather forecast = HourlyWeather
                .builder()
                .id(hourlyWeatherId)
                .temperature(13)
                .precipitation(70)
                .status("Cloudy")
                .build();

        underTest.save(forecast);
        HourlyWeather updatedForecast = underTest.save(forecast);
        assertThat(updatedForecast).isNotNull();

        // Delete from repository
        underTest.deleteById(hourlyWeatherId);

        Optional<HourlyWeather> expectedResult = underTest.findById(hourlyWeatherId);

        assertThat(expectedResult).isNotPresent();
    }

}
