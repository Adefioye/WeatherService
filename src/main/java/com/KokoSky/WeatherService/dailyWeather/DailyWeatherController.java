package com.KokoSky.WeatherService.dailyWeather;

import com.KokoSky.WeatherService.geolocation.GeolocationService;
import com.KokoSky.WeatherService.location.Location;
import com.KokoSky.WeatherService.utility.CommonUtility;
import jakarta.servlet.http.HttpServletRequest;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/daily")
public class DailyWeatherController {

    private final DailyWeatherService dailyWeatherService;
    private final GeolocationService geolocationService;
    private final ModelMapper modelMapper;

    public DailyWeatherController(
            DailyWeatherService dailyWeatherService,
            GeolocationService locationService,
            ModelMapper modelMapper) {
        this.dailyWeatherService = dailyWeatherService;
        this.geolocationService = locationService;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    public ResponseEntity<?> listDailyForecastByIPAddress(HttpServletRequest request) {
        String ipAddress = CommonUtility.getIPAddress(request);

        Location locationFromIP = geolocationService.getLocation(ipAddress);
        List<DailyWeather> dailyForecast = dailyWeatherService.getByLocation(locationFromIP);

        if (dailyForecast.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(listEntity2DTO(dailyForecast));
    }

    private DailyWeatherListDTO listEntity2DTO(List<DailyWeather> dailyForecast) {
        Location location = dailyForecast.get(0).getId().getLocation();

        DailyWeatherListDTO listDTO = new DailyWeatherListDTO();
        listDTO.setLocation(location.toString());

        dailyForecast.forEach(dailyWeather -> {
            listDTO.addDailyWeatherDTO(modelMapper.map(dailyWeather, DailyWeatherDTO.class));
        });

        return listDTO;
    }

}
