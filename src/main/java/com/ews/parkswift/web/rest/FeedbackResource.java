package com.ews.parkswift.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ews.parkswift.domain.Feedback;
import com.ews.parkswift.repository.FeedbackRepository;
import com.ews.parkswift.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Feedback.
 */
@RestController
@RequestMapping("/api")
public class FeedbackResource {

    private final Logger log = LoggerFactory.getLogger(FeedbackResource.class);

    @Inject
    private FeedbackRepository feedbackRepository;

    /**
     * POST  /feedbacks -> Create a new feedback.
     */
    @RequestMapping(value = "/feedbacks",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> create(@Valid @RequestBody Feedback feedback) throws URISyntaxException {
        log.debug("REST request to save Feedback : {}", feedback);
        if (feedback.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new feedback cannot already have an ID").build();
        }
        feedbackRepository.save(feedback);
        return ResponseEntity.created(new URI("/api/feedbacks/" + feedback.getId())).build();
    }

    /**
     * PUT  /feedbacks -> Updates an existing feedback.
     */
    @RequestMapping(value = "/feedbacks",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> update(@Valid @RequestBody Feedback feedback) throws URISyntaxException {
        log.debug("REST request to update Feedback : {}", feedback);
        if (feedback.getId() == null) {
            return create(feedback);
        }
        feedbackRepository.save(feedback);
        return ResponseEntity.ok().build();
    }

    /**
     * GET  /feedbacks -> get all the feedbacks.
     */
    @RequestMapping(value = "/feedbacks",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Feedback>> getAll(@RequestParam(value = "page" , required = false) Integer offset,
                                  @RequestParam(value = "per_page", required = false) Integer limit)
        throws URISyntaxException {
        Page<Feedback> page = feedbackRepository.findAll(PaginationUtil.generatePageRequest(offset, limit));
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/feedbacks", offset, limit);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /feedbacks/:id -> get the "id" feedback.
     */
    @RequestMapping(value = "/feedbacks/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Feedback> get(@PathVariable Long id) {
        log.debug("REST request to get Feedback : {}", id);
        return Optional.ofNullable(feedbackRepository.findOne(id))
            .map(feedback -> new ResponseEntity<>(
                feedback,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /feedbacks/:id -> delete the "id" feedback.
     */
    @RequestMapping(value = "/feedbacks/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete Feedback : {}", id);
        feedbackRepository.delete(id);
    }
}
