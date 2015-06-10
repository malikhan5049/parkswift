package com.ews.parkswift.repository;

import com.ews.parkswift.domain.ParkingLocation;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the ParkingLocation entity.
 */
public interface ParkingLocationRepository extends JpaRepository<ParkingLocation,Long> {

    @Query("select parkingLocation from ParkingLocation parkingLocation where parkingLocation.user.login = ?#{principal.username}")
    List<ParkingLocation> findAllForCurrentUser();

}
