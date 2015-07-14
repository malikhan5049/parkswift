package com.ews.parkswift.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ews.parkswift.domain.ParkingSpace;

/**
 * Spring Data JPA repository for the ParkingSpace entity.
 */
public interface ParkingSpaceRepository extends JpaRepository<ParkingSpace,Long> {

	List<ParkingSpace> findAllByParkingLocationId(Long id);

}
