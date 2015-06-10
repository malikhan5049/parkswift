package com.ews.parkswift.repository;

import com.ews.parkswift.domain.PaypallAccount;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the PaypallAccount entity.
 */
public interface PaypallAccountRepository extends JpaRepository<PaypallAccount,Long> {

    @Query("select paypallAccount from PaypallAccount paypallAccount where paypallAccount.user.login = ?#{principal.username}")
    List<PaypallAccount> findAllForCurrentUser();

}
