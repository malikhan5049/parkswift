package com.ews.parkswift.repository;

import com.ews.parkswift.domain.ParkingSpace;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the ParkingSpace entity.
 */
public interface ParkingSpaceRepository extends JpaRepository<ParkingSpace,Long> {

}
