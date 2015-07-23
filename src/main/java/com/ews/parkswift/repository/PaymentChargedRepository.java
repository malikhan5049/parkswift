package com.ews.parkswift.repository;

import com.ews.parkswift.domain.PaymentCharged;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the PaymentCharged entity.
 */
public interface PaymentChargedRepository extends JpaRepository<PaymentCharged,Long> {

}
