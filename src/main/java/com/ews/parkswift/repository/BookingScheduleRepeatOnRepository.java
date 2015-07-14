package com.ews.parkswift.repository;

import com.ews.parkswift.domain.BookingScheduleRepeatOn;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the ReservedParkingRepeatOn entity.
 */
public interface BookingScheduleRepeatOnRepository extends JpaRepository<BookingScheduleRepeatOn,Long> {

}
