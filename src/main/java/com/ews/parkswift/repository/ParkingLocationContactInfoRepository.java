package com.ews.parkswift.repository;

import com.ews.parkswift.domain.ParkingLocationContactInfo;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the ParkingLocationContactInfo entity.
 */
public interface ParkingLocationContactInfoRepository extends JpaRepository<ParkingLocationContactInfo,Long> {

}
