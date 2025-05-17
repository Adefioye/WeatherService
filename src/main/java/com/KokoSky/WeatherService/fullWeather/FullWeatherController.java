package com.KokoSky.WeatherService.fullWeather;

import com.KokoSky.WeatherService.geolocation.GeolocationService;
import com.KokoSky.WeatherService.location.Location;
import com.KokoSky.WeatherService.utility.CommonUtility;
import jakarta.servlet.http.HttpServletRequest;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/full")
public class FullWeatherController {

    private final GeolocationService geolocationService;
    private final FullWeatherService fullWeatherService;
    private final ModelMapper modelMapper;

    public FullWeatherController(
            GeolocationService locationService,
            FullWeatherService weatherService,
            ModelMapper modelMapper) {
        this.geolocationService = locationService;
        this.fullWeatherService = weatherService;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    public ResponseEntity<?> getFullWeatherByIPAddress(HttpServletRequest request) {
        String ipAddress = CommonUtility.getIPAddress(request);

        Location locationFromIP = geolocationService.getLocation(ipAddress);
        Location locationInDB = fullWeatherService.getByLocation(locationFromIP);

        return ResponseEntity.ok(entity2DTO(locationInDB));
    }

    private FullWeatherDTO entity2DTO(Location entity) {
        FullWeatherDTO dto = modelMapper.map(entity, FullWeatherDTO.class);

        // Do not show the field location in realtime_weather object
        dto.getRealtimeWeather().setLocation(null);
        return dto;
    }
}
