package com.ews.parkswift.repository;

import com.ews.parkswift.domain.AvailableParkingRepeatOn;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the AvailableParkingRepeatOn entity.
 */
public interface AvailableParkingRepeatOnRepository extends JpaRepository<AvailableParkingRepeatOn,Long> {

}
