package com.ews.parkswift.repository;

import com.ews.parkswift.domain.BookingSchedule;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the ReservedParking entity.
 */
public interface BookingScheduleRepository extends JpaRepository<BookingSchedule,Long> {

}
