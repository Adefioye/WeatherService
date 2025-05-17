package com.KokoSky.WeatherService.fullWeather;

import com.KokoSky.WeatherService.exceptions.LocationNotFoundException;
import com.KokoSky.WeatherService.location.Location;
import com.KokoSky.WeatherService.location.LocationRepository;
import org.springframework.stereotype.Service;

@Service
public class FullWeatherService {

    private LocationRepository locationRepository;

    public FullWeatherService(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    public Location getByLocation(Location locationFromIP) {
        String cityName = locationFromIP.getCityName();
        String countryCode = locationFromIP.getCountryCode();

        Location locationInDB = locationRepository.findByCountryCodeAndCityName(countryCode, cityName);

        if (locationInDB == null) {
            throw new LocationNotFoundException(countryCode, cityName);
        }

        return locationInDB;
    }
}
