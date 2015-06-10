package com.ews.parkswift.repository;

import com.ews.parkswift.domain.ParkingSpacePriceEntry;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the ParkingSpacePriceEntry entity.
 */
public interface ParkingSpacePriceEntryRepository extends JpaRepository<ParkingSpacePriceEntry,Long> {

}
