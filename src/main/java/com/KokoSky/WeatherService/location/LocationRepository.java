package com.KokoSky.WeatherService.location;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;


public interface LocationRepository extends CrudRepository<Location, String> {

    boolean existsLocationByCode(String code);
//    @Query("select l from ")
//    Optional<Location> findUntrashedLocationById;
}
