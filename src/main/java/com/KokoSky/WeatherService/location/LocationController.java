package com.KokoSky.WeatherService.location;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/locations")
public class LocationController {

    private final LocationService locationService;

    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    @PostMapping
    public ResponseEntity<Location> addLocation(@RequestBody @Valid Location locationRequest) {
        Location newLocation = locationService.addLocation(locationRequest);

        URI uriLocation = URI.create("/api/v1/locations/%s".formatted(newLocation.getCode()));

        return ResponseEntity.created(uriLocation).body(newLocation);
    }
}
