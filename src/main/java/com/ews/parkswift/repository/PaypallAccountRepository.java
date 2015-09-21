package com.ews.parkswift.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ews.parkswift.domain.PaypallAccount;

/**
 * Spring Data JPA repository for the PaypallAccount entity.
 */
public interface PaypallAccountRepository extends JpaRepository<PaypallAccount,Long> {

    @Query("select paypallAccount from PaypallAccount paypallAccount where paypallAccount.user.login = ?#{principal.username}")
    List<PaypallAccount> findAllForCurrentUser();

	PaypallAccount findOneByEmail(String email);

	Optional<PaypallAccount>  findOneById(Long id);

}
