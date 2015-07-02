package com.ews.parkswift.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ews.parkswift.domain.LookupHeader;

/**
 * Spring Data JPA repository for the LookupHeader entity.
 */
public interface LookupHeaderRepository extends JpaRepository<LookupHeader,Long> {


	LookupHeader findAllByCode(String code);
	

}
