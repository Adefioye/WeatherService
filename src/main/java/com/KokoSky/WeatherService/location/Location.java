package com.KokoSky.WeatherService.location;

import com.KokoSky.WeatherService.hourlyWeather.HourlyWeather;
import com.KokoSky.WeatherService.realtimeWeather.RealtimeWeather;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.ArrayList;
import java.util.List;


@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "locations")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Location {
    @Id
    @Column(length = 12, nullable = false, unique = true)
    @NotBlank
    @EqualsAndHashCode.Include
    private String code;

    @Column(length = 128, nullable = false)
    @NotBlank
    @JsonProperty("city_name")
    private String cityName;

    @Column(length = 128, nullable = false)
    @JsonProperty("region_name")
    @NotBlank
    @NotNull
    private String regionName;

    @Column(length = 64, nullable = false)
    @NotBlank
    @JsonProperty("country_name")
    private String countryName;

    @Column(length = 2, nullable = false)
    @NotBlank
    @JsonProperty("country_code")
    private String countryCode;

    @NotNull
    private boolean enabled;

    @NotNull
    @JsonIgnore
    private boolean trashed = false;

    @OneToOne(mappedBy = "location", cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    @JsonIgnore
    private RealtimeWeather realtimeWeather;

    @OneToMany(mappedBy = "id.location", cascade = CascadeType.ALL)
    @Builder.Default
    private List<HourlyWeather> listHourlyWeather = new ArrayList<>();

    public Location(String cityName, String regionName, String countryName, String countryCode) {
        this.cityName = cityName;
        this.regionName = regionName;
        this.countryName = countryName;
        this.countryCode = countryCode;
    }

    @Override
    public String toString() {
        return cityName + ", " + (regionName != null ? regionName + ", " : "") + countryName;
    }
}
