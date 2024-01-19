package com.KokoSky.WeatherService.location;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

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
        System.out.println("Entering a controller: " + locationRequest);
        Location savedLocation = locationService.addLocation(locationRequest);

        String locationUri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{code}")
                .buildAndExpand(savedLocation.getCode())
                .toUriString();

        return ResponseEntity.created(URI.create(locationUri)).body(savedLocation);
    }
}
