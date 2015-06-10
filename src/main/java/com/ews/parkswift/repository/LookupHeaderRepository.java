package com.ews.parkswift.repository;

import com.ews.parkswift.domain.LookupHeader; 
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the LookupHeader entity.
 */
public interface LookupHeaderRepository extends JpaRepository<LookupHeader,Long> {

}
