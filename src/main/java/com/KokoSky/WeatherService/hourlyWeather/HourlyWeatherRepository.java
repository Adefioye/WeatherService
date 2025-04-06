package com.KokoSky.WeatherService.hourlyWeather;

import org.springframework.data.repository.CrudRepository;

public interface HourlyWeatherRepository extends CrudRepository<HourlyWeather, HourlyWeatherId> {
}
