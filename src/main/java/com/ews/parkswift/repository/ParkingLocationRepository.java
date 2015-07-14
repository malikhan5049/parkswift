package com.ews.parkswift.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ews.parkswift.domain.ParkingLocation;

/**
 * Spring Data JPA repository for the ParkingLocation entity.
 */
public interface ParkingLocationRepository extends JpaRepository<ParkingLocation,Long> {

    @Query("select parkingLocation from ParkingLocation parkingLocation where parkingLocation.user.login = ?#{principal.username}")
    List<ParkingLocation> findAllForCurrentUser();

	ParkingLocation findOneByParkingLocationContactInfoId(Long id);


}
