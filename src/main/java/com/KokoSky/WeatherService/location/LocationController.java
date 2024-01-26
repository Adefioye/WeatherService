package com.KokoSky.WeatherService.location;

import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping
    public ResponseEntity<List<Location>> getLocations() {
        List<Location> locations = locationService.getLocations();

        if (locations.size() == 0) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok().body(locations);
    }

    @GetMapping("{code}")
    public ResponseEntity<Location> getLocations(@PathVariable String code) {

        Location locationByCode = locationService.getLocationByCode(code);
        return ResponseEntity.ok().body(locationByCode);
    }

    @PutMapping("{code}")
    public ResponseEntity<Location> getLocations(@PathVariable String code, @RequestBody @Valid Location newLocationRequest) {

        Location updatedLocationByCode = locationService.updateLocationByCode(code, newLocationRequest);
        return ResponseEntity.ok().body(updatedLocationByCode);
    }

    @DeleteMapping("{code}")
    public ResponseEntity<?> deleteLocation(@PathVariable String code) {
        locationService.deleteLocationByCode(code);

        return ResponseEntity.ok().build();
    }
}
