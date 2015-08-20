package com.ews.parkswift.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ews.parkswift.domain.Favourite;

/**
 * Spring Data JPA repository for the Favourite entity.
 */
public interface FavouriteRepository extends JpaRepository<Favourite,Long> {

    @Query("select favourite from Favourite favourite where favourite.user.login = ?#{principal.username}")
    List<Favourite> findAllForCurrentUser();
}
