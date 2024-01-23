package com.KokoSky.WeatherService.location;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import java.util.List;


public interface LocationRepository extends CrudRepository<Location, String> {

    public boolean existsLocationByCode(String code);

    @Query("select l from Location l where l.trashed = false")
    public List<Location> findAllUntrashedLocations();
}
