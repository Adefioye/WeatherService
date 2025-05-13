package com.KokoSky.WeatherService.dailyWeather;

import com.KokoSky.WeatherService.exceptions.LocationNotFoundException;
import com.KokoSky.WeatherService.location.Location;
import com.KokoSky.WeatherService.location.LocationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DailyWeatherService {

    private final DailyWeatherRepository dailyWeatherRepository;
    private final LocationRepository locationRepository;

    public DailyWeatherService(
            DailyWeatherRepository dailyWeatherRepository,
            LocationRepository locationRepository
    ) {
        this.dailyWeatherRepository = dailyWeatherRepository;
        this.locationRepository = locationRepository;
    }

    public List<DailyWeather> getByLocation(Location location) {
        String countryCode = location.getCountryCode();
        String cityName = location.getCityName();

        Location locationInDB = locationRepository.findByCountryCodeAndCityName(countryCode, cityName);

        if (locationInDB == null) {
            throw new LocationNotFoundException(countryCode, cityName);
        }
        return dailyWeatherRepository.findByLocationCode(locationInDB.getCode());
    }
}
