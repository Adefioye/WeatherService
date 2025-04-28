package com.KokoSky.WeatherService.hourlyWeather;

import com.KokoSky.WeatherService.exceptions.LocationNotFoundException;
import com.KokoSky.WeatherService.location.Location;
import com.KokoSky.WeatherService.location.LocationRepository;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class HourlyWeatherService {
    private final HourlyWeatherRepository hourlyWeatherRepository;
    private final LocationRepository locationRepository;

    public HourlyWeatherService(HourlyWeatherRepository hourlyWeatherRepository, LocationRepository locationRepository) {
        super();
        this.hourlyWeatherRepository = hourlyWeatherRepository;
        this.locationRepository = locationRepository;
    }

    public List<HourlyWeather> getByLocation(Location location, int currentHour) throws LocationNotFoundException {
        String countryCode = location.getCountryCode();
        String cityName = location.getCityName();

        Location locationInDB = locationRepository.findByCountryCodeAndCityName(countryCode, cityName);

        if (locationInDB == null) {
            throw new LocationNotFoundException("No location found with the given country code and city name");
        }

        return hourlyWeatherRepository.findByLocationCodeAndHour(locationInDB.getCode(), currentHour);
    }

    public List<HourlyWeather> getByLocationCode(String locationCode, int currentHour) throws LocationNotFoundException {

        Location locationInDB = locationRepository.findByCode(locationCode);

        if (locationInDB == null) {
            throw new LocationNotFoundException("No location found with the given code");
        }

        return hourlyWeatherRepository.findByLocationCodeAndHour(locationCode, currentHour);
    }

    public List<HourlyWeather> updateByLocationCode(String locationCode, List<HourlyWeather> hourlyForecastInRequest) {
        return Collections.emptyList();
    }
}
