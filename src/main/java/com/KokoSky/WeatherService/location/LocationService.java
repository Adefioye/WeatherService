package com.KokoSky.WeatherService.location;


import com.KokoSky.WeatherService.exceptions.DuplicateResourceException;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class LocationService {

    private final LocationRepository locationRepository;

    public LocationService(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    public Location addLocation(Location locationRequest) {
        if (locationRepository.existsLocationByCode(locationRequest.getCode())) {
            throw new DuplicateResourceException("Sorry! location code %s already exist!".formatted(locationRequest.getCode()));
        }
        return locationRepository.save(locationRequest);
    }

    public List<Location> getLocations() {
        return locationRepository.findAllUntrashedLocations();
    }
}
