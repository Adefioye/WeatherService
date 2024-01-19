package com.KokoSky.WeatherService.location;


import org.springframework.stereotype.Service;

@Service
public class LocationService {

    private final LocationRepository locationRepository;


    public LocationService(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    public Location addLocation(Location locationRequest) {
        return locationRepository.save(locationRequest);
    }
}
