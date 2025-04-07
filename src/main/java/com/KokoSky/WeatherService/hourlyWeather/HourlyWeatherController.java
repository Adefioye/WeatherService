package com.KokoSky.WeatherService.hourlyWeather;

import com.KokoSky.WeatherService.exceptions.GeolocationException;
import com.KokoSky.WeatherService.exceptions.LocationNotFoundException;
import com.KokoSky.WeatherService.geolocation.GeolocationService;
import com.KokoSky.WeatherService.location.Location;
import com.KokoSky.WeatherService.utility.CommonUtility;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

public class HourlyWeatherController {
    private final HourlyWeatherService hourlyWeatherService;
    private final GeolocationService locationService;

    public HourlyWeatherController(HourlyWeatherService hourlyWeatherService, GeolocationService locationService) {
        super();
        this.hourlyWeatherService = hourlyWeatherService;
        this.locationService = locationService;
    }

    @GetMapping
    public ResponseEntity<?> listHourlyForecastByIPAddress(HttpServletRequest request) {
        String ipAddress = CommonUtility.getIPAddress(request);

        int currentHour = Integer.parseInt(request.getHeader("X-Current-Hour"));

        try {
            Location locationFromIP = locationService.getLocation(ipAddress);

            List<HourlyWeather> hourlyForecast = hourlyWeatherService.getByLocation(locationFromIP, currentHour);

            if (hourlyForecast.isEmpty()) {
                return ResponseEntity.noContent().build();
            }

            return ResponseEntity.ok(hourlyForecast);
        } catch (GeolocationException ex) {

            return ResponseEntity.badRequest().build();
        } catch (LocationNotFoundException ex) {

            return ResponseEntity.notFound().build();
        }

    }
}
