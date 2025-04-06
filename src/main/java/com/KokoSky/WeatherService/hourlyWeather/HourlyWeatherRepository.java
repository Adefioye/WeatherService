package com.KokoSky.WeatherService.hourlyWeather;

import com.KokoSky.WeatherService.location.Location;
import org.springframework.data.repository.CrudRepository;

public interface HourlyWeatherRepository extends CrudRepository<HourlyWeather, HourlyWeatherId> {
}
