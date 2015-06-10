package com.ews.parkswift.repository;

import com.ews.parkswift.domain.ParkingLocationFacility;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the ParkingLocationFacility entity.
 */
public interface ParkingLocationFacilityRepository extends JpaRepository<ParkingLocationFacility,Long> {

}
