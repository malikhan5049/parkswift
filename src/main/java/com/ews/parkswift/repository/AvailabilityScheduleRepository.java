package com.ews.parkswift.repository;

import com.ews.parkswift.domain.AvailabilitySchedule;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the availabilitySchedule entity.
 */
public interface AvailabilityScheduleRepository extends JpaRepository<AvailabilitySchedule,Long> {

}
