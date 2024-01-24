package com.KokoSky.WeatherService.location;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import java.util.List;
import java.util.Optional;


public interface LocationRepository extends CrudRepository<Location, String> {

    public boolean existsLocationByCode(String code);

    @Query("select l from Location l where l.trashed = false")
    public List<Location> findAllUntrashedLocations();

    @Query("select l from Location l where l.code = ?1 and l.trashed = false")
    public Optional<Location> findUntrashedLocationsByCode(String code);
}
