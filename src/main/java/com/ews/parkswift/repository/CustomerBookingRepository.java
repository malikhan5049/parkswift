package com.ews.parkswift.repository;

import com.ews.parkswift.domain.CustomerBooking;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the CustomerBooking entity.
 */
public interface CustomerBookingRepository extends JpaRepository<CustomerBooking,Long> {

    @Query("select customerBooking from CustomerBooking customerBooking where customerBooking.user.login = ?#{principal.username} and customerBooking.status = 'BOOKED'")
    List<CustomerBooking> findAllForCurrentUser();

}
