package com.ews.parkswift.repository;

import com.ews.parkswift.domain.AvailableParking;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the AvailableParking entity.
 */
public interface AvailableParkingRepository extends JpaRepository<AvailableParking,Long> {

}
