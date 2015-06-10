package com.ews.parkswift.repository;

import com.ews.parkswift.domain.ParkingSpaceImage;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the ParkingSpaceImage entity.
 */
public interface ParkingSpaceImageRepository extends JpaRepository<ParkingSpaceImage,Long> {

}
