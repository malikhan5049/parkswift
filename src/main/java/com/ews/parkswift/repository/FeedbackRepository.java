package com.ews.parkswift.repository;

import com.ews.parkswift.domain.Feedback;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Feedback entity.
 */
public interface FeedbackRepository extends JpaRepository<Feedback,Long> {

    @Query("select feedback from Feedback feedback where feedback.user.login = ?#{principal.username}")
    List<Feedback> findAllForCurrentUser();

}
