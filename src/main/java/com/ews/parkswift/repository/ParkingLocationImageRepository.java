package com.ews.parkswift.repository;

import com.ews.parkswift.domain.ParkingLocationImage;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the ParkingSpaceImage entity.
 */
public interface ParkingLocationImageRepository extends JpaRepository<ParkingLocationImage,Long> {

}
