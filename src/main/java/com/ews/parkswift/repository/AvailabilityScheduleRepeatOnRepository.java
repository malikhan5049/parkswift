package com.ews.parkswift.repository;

import com.ews.parkswift.domain.AvailabilityScheduleRepeatOn;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the availabilityScheduleRepeatOn entity.
 */
public interface AvailabilityScheduleRepeatOnRepository extends JpaRepository<AvailabilityScheduleRepeatOn,Long> {

}
