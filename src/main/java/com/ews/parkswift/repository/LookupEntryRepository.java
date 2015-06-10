package com.ews.parkswift.repository;

import com.ews.parkswift.domain.LookupEntry;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the LookupEntry entity.
 */
public interface LookupEntryRepository extends JpaRepository<LookupEntry,Long> {

}
