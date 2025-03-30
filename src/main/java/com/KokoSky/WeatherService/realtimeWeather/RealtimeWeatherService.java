package com.KokoSky.WeatherService.realtimeWeather;

import com.KokoSky.WeatherService.exceptions.LocationNotFoundException;
import com.KokoSky.WeatherService.location.Location;
import org.springframework.stereotype.Service;

@Service
public class RealtimeWeatherService {

    private final RealtimeWeatherRepository realtimeWeatherRepository;

    public RealtimeWeatherService(RealtimeWeatherRepository realtimeWeatherRepository) {
        this.realtimeWeatherRepository = realtimeWeatherRepository;
    }

    public RealtimeWeather getByLocation(Location location) {
        String countryCode = location.getCountryCode();
        String cityName = location.getCityName();

        RealtimeWeather realtimeWeather = realtimeWeatherRepository.findByCountryCodeAndCity(countryCode, cityName);

        if (realtimeWeather == null) {
            throw new LocationNotFoundException(
                "Sorry! cannot find realtime weather for location: %s, %s".formatted(countryCode, cityName));
        }

        return realtimeWeather;
    }

    public RealtimeWeather getByLocationCode(String locationCode) {

        RealtimeWeather realtimeWeather = realtimeWeatherRepository.findByLocationCode(locationCode);

        if (realtimeWeather == null) {
            throw new LocationNotFoundException(
                    "Sorry! cannot find realtime weather for location code: %s".formatted(locationCode));
        }

        return realtimeWeather;
    }
}
