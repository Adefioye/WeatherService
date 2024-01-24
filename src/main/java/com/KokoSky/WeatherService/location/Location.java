package com.KokoSky.WeatherService.location;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "locations")
public class Location {
    @Id
    @Column(length = 12, nullable = false, unique = true)
    @NotBlank
    private String code;

    @Column(length = 128, nullable = false)
    @NotBlank
    @JsonProperty("city_name")
    private String cityName;

    @Column(length = 128, nullable = false)
    @JsonProperty("region_name")
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
}
