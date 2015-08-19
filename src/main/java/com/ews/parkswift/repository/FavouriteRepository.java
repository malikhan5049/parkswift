package com.ews.parkswift.repository;

import com.ews.parkswift.domain.Favourite;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Favourite entity.
 */
public interface FavouriteRepository extends JpaRepository<Favourite,Long> {

    @Query("select favourite from Favourite favourite where favourite.user.login = ?#{principal.username}")
    List<Favourite> findAllForCurrentUser();
    
}
