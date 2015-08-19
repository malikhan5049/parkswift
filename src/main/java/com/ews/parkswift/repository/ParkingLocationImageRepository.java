package com.ews.parkswift.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ews.parkswift.domain.ParkingLocation;
import com.ews.parkswift.domain.ParkingLocationImage;

/**
 * Spring Data JPA repository for the ParkingSpaceImage entity.
 */
public interface ParkingLocationImageRepository extends JpaRepository<ParkingLocationImage,Long> {

	List<ParkingLocationImage> findAllByParkingLocation(ParkingLocation parkingLocation);
	ParkingLocationImage findOneByParkingLocation(ParkingLocation parkingLocation);
}
