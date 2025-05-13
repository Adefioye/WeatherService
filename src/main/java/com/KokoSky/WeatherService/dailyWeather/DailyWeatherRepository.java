package com.KokoSky.WeatherService.dailyWeather;

import org.springframework.data.repository.CrudRepository;

public interface DailyWeatherRepository extends CrudRepository<DailyWeather, DailyWeatherId> {
}
