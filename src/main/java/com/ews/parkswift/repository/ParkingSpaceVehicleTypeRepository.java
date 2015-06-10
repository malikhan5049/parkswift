package com.ews.parkswift.repository;

import com.ews.parkswift.domain.ParkingSpaceVehicleType;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the ParkingSpaceVehicleType entity.
 */
public interface ParkingSpaceVehicleTypeRepository extends JpaRepository<ParkingSpaceVehicleType,Long> {

}
