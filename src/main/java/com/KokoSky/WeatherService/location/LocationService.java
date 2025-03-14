package com.KokoSky.WeatherService.location;


import com.KokoSky.WeatherService.exceptions.DuplicateResourceException;
import com.KokoSky.WeatherService.exceptions.ResourceNotFoundException;
import java.util.List;
import java.util.Optional;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class LocationService {

    private final LocationRepository locationRepository;

    public LocationService(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    @Transactional
    public Location addLocation(Location locationRequest) {
        if (locationRepository.existsLocationByCode(locationRequest.getCode())) {
            throw new DuplicateResourceException("Sorry! location code %s already exist!".formatted(locationRequest.getCode()));
        }
        return locationRepository.save(locationRequest);
    }

    public List<Location> getLocations() {
        return locationRepository.findAllUntrashedLocations();
    }

    public Location getLocationByCode(String code) {
        return locationRepository.findUntrashedLocationsByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("Sorry! cannot find location with code: %s".formatted(code)));
    }

    @Transactional
    public Location updateLocationByCode(Location newLocation) {
        String code = newLocation.getCode();

        if (!locationRepository.existsLocationByCode(code)) {
            throw new ResourceNotFoundException("Sorry! cannot find location with code: %s".formatted(code));
        }

        Location updatedLocation = Location
                .builder()
                .code(code)
                .cityName(newLocation.getCityName())
                .regionName(newLocation.getRegionName())
                .countryName(newLocation.getCountryName())
                .countryCode(newLocation.getCountryCode())
                .enabled(newLocation.isEnabled())
                .build();

        return locationRepository.save(updatedLocation);
    }

    @Transactional
    public void deleteLocationByCode(String code) {
        if (!locationRepository.existsLocationByCode(code)) {
            throw new ResourceNotFoundException("Sorry! cannot find location with code: %s".formatted(code));
        }

        locationRepository.softDeleteByCode(code);
    }

}
