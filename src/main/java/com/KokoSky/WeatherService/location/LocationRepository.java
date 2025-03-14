package com.KokoSky.WeatherService.location;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import java.util.List;
import java.util.Optional;


public interface LocationRepository extends CrudRepository<Location, String> {

    boolean existsLocationByCode(String code);

    @Query("select l from Location l where l.trashed = false")
    List<Location> findAllUntrashedLocations();

    @Query("select l from Location l where l.code = ?1 and l.trashed = false")
    Optional<Location> findUntrashedLocationsByCode(String code);

    @Modifying
    @Query("UPDATE Location l SET l.trashed = true WHERE l.code = ?1")
    void softDeleteByCode(String code);
}
