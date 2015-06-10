package com.ews.parkswift.repository;

import com.ews.parkswift.domain.ReservedParking;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the ReservedParking entity.
 */
public interface ReservedParkingRepository extends JpaRepository<ReservedParking,Long> {

}
