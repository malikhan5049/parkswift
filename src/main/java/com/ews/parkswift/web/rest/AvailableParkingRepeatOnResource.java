package com.ews.parkswift.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ews.parkswift.domain.AvailableParkingRepeatOn;
import com.ews.parkswift.repository.AvailableParkingRepeatOnRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing AvailableParkingRepeatOn.
 */
@RestController
@RequestMapping("/api")
public class AvailableParkingRepeatOnResource {

    private final Logger log = LoggerFactory.getLogger(AvailableParkingRepeatOnResource.class);

    @Inject
    private AvailableParkingRepeatOnRepository availableParkingRepeatOnRepository;

    /**
     * POST  /availableParkingRepeatOns -> Create a new availableParkingRepeatOn.
     */
    @RequestMapping(value = "/availableParkingRepeatOns",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> create(@RequestBody AvailableParkingRepeatOn availableParkingRepeatOn) throws URISyntaxException {
        log.debug("REST request to save AvailableParkingRepeatOn : {}", availableParkingRepeatOn);
        if (availableParkingRepeatOn.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new availableParkingRepeatOn cannot already have an ID").build();
        }
        availableParkingRepeatOnRepository.save(availableParkingRepeatOn);
        return ResponseEntity.created(new URI("/api/availableParkingRepeatOns/" + availableParkingRepeatOn.getId())).build();
    }

    /**
     * PUT  /availableParkingRepeatOns -> Updates an existing availableParkingRepeatOn.
     */
    @RequestMapping(value = "/availableParkingRepeatOns",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> update(@RequestBody AvailableParkingRepeatOn availableParkingRepeatOn) throws URISyntaxException {
        log.debug("REST request to update AvailableParkingRepeatOn : {}", availableParkingRepeatOn);
        if (availableParkingRepeatOn.getId() == null) {
            return create(availableParkingRepeatOn);
        }
        availableParkingRepeatOnRepository.save(availableParkingRepeatOn);
        return ResponseEntity.ok().build();
    }

    /**
     * GET  /availableParkingRepeatOns -> get all the availableParkingRepeatOns.
     */
    @RequestMapping(value = "/availableParkingRepeatOns",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<AvailableParkingRepeatOn> getAll() {
        log.debug("REST request to get all AvailableParkingRepeatOns");
        return availableParkingRepeatOnRepository.findAll();
    }

    /**
     * GET  /availableParkingRepeatOns/:id -> get the "id" availableParkingRepeatOn.
     */
    @RequestMapping(value = "/availableParkingRepeatOns/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<AvailableParkingRepeatOn> get(@PathVariable Long id) {
        log.debug("REST request to get AvailableParkingRepeatOn : {}", id);
        return Optional.ofNullable(availableParkingRepeatOnRepository.findOne(id))
            .map(availableParkingRepeatOn -> new ResponseEntity<>(
                availableParkingRepeatOn,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /availableParkingRepeatOns/:id -> delete the "id" availableParkingRepeatOn.
     */
    @RequestMapping(value = "/availableParkingRepeatOns/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete AvailableParkingRepeatOn : {}", id);
        availableParkingRepeatOnRepository.delete(id);
    }
}
