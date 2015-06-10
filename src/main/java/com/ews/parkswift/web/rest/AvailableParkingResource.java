package com.ews.parkswift.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ews.parkswift.domain.AvailableParking;
import com.ews.parkswift.repository.AvailableParkingRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * REST controller for managing AvailableParking.
 */
@RestController
@RequestMapping("/api")
public class AvailableParkingResource {

    private final Logger log = LoggerFactory.getLogger(AvailableParkingResource.class);

    @Inject
    private AvailableParkingRepository availableParkingRepository;

    /**
     * POST  /availableParkings -> Create a new availableParking.
     */
    @RequestMapping(value = "/availableParkings",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> create(@Valid @RequestBody AvailableParking availableParking) throws URISyntaxException {
        log.debug("REST request to save AvailableParking : {}", availableParking);
        if (availableParking.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new availableParking cannot already have an ID").build();
        }
        availableParkingRepository.save(availableParking);
        return ResponseEntity.created(new URI("/api/availableParkings/" + availableParking.getId())).build();
    }

    /**
     * PUT  /availableParkings -> Updates an existing availableParking.
     */
    @RequestMapping(value = "/availableParkings",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> update(@Valid @RequestBody AvailableParking availableParking) throws URISyntaxException {
        log.debug("REST request to update AvailableParking : {}", availableParking);
        if (availableParking.getId() == null) {
            return create(availableParking);
        }
        availableParkingRepository.save(availableParking);
        return ResponseEntity.ok().build();
    }

    /**
     * GET  /availableParkings -> get all the availableParkings.
     */
    @RequestMapping(value = "/availableParkings",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<AvailableParking> getAll() {
        log.debug("REST request to get all AvailableParkings");
        return availableParkingRepository.findAll();
    }

    /**
     * GET  /availableParkings/:id -> get the "id" availableParking.
     */
    @RequestMapping(value = "/availableParkings/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<AvailableParking> get(@PathVariable Long id) {
        log.debug("REST request to get AvailableParking : {}", id);
        return Optional.ofNullable(availableParkingRepository.findOne(id))
            .map(availableParking -> new ResponseEntity<>(
                availableParking,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /availableParkings/:id -> delete the "id" availableParking.
     */
    @RequestMapping(value = "/availableParkings/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete AvailableParking : {}", id);
        availableParkingRepository.delete(id);
    }
}
