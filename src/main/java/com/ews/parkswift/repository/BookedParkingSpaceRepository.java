package com.ews.parkswift.repository;

import com.ews.parkswift.domain.BookedParkingSpace;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the BookedParkingSpace entity.
 */
public interface BookedParkingSpaceRepository extends JpaRepository<BookedParkingSpace,Long> {

}
