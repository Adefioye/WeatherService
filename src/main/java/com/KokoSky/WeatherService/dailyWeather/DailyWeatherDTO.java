package com.KokoSky.WeatherService.dailyWeather;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonPropertyOrder({"day_of_month", "month", "min_temp", "max_temp", "precipitation", "status"})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DailyWeatherDTO {

    @JsonProperty("day_of_month")
    private int dayOfMonth;

    private int month;

    @JsonProperty("min_temp")
    private int minTemp;

    @JsonProperty("max_temp")
    private int maxTemp;

    private int precipitation;

    private String status;
}
