package com.ews.parkswift.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ews.parkswift.domain.AvailabilityScheduleRepeatOn;
import com.ews.parkswift.repository.AvailabilityScheduleRepeatOnRepository;
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
 * REST controller for managing availabilityScheduleRepeatOn.
 */
@RestController
@RequestMapping("/api")
public class AvailabilityScheduleRepeatOnResource {

    private final Logger log = LoggerFactory.getLogger(AvailabilityScheduleRepeatOnResource.class);

    @Inject
    private AvailabilityScheduleRepeatOnRepository availabilityScheduleRepeatOnRepository;

    /**
     * POST  /availabilityScheduleRepeatOns -> Create a new availabilityScheduleRepeatOn.
     */
    @RequestMapping(value = "/availabilityScheduleRepeatOns",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> create(@RequestBody AvailabilityScheduleRepeatOn availabilityScheduleRepeatOn) throws URISyntaxException {
        log.debug("REST request to save availabilityScheduleRepeatOn : {}", availabilityScheduleRepeatOn);
        if (availabilityScheduleRepeatOn.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new availabilityScheduleRepeatOn cannot already have an ID").build();
        }
        availabilityScheduleRepeatOnRepository.save(availabilityScheduleRepeatOn);
        return ResponseEntity.created(new URI("/api/availabilityScheduleRepeatOns/" + availabilityScheduleRepeatOn.getId())).build();
    }

    /**
     * PUT  /availabilityScheduleRepeatOns -> Updates an existing availabilityScheduleRepeatOn.
     */
    @RequestMapping(value = "/availabilityScheduleRepeatOns",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> update(@RequestBody AvailabilityScheduleRepeatOn availabilityScheduleRepeatOn) throws URISyntaxException {
        log.debug("REST request to update availabilityScheduleRepeatOn : {}", availabilityScheduleRepeatOn);
        if (availabilityScheduleRepeatOn.getId() == null) {
            return create(availabilityScheduleRepeatOn);
        }
        availabilityScheduleRepeatOnRepository.save(availabilityScheduleRepeatOn);
        return ResponseEntity.ok().build();
    }

    /**
     * GET  /availabilityScheduleRepeatOns -> get all the availabilityScheduleRepeatOns.
     */
    @RequestMapping(value = "/availabilityScheduleRepeatOns",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<AvailabilityScheduleRepeatOn> getAll() {
        log.debug("REST request to get all availabilityScheduleRepeatOns");
        return availabilityScheduleRepeatOnRepository.findAll();
    }

    /**
     * GET  /availabilityScheduleRepeatOns/:id -> get the "id" availabilityScheduleRepeatOn.
     */
    @RequestMapping(value = "/availabilityScheduleRepeatOns/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<AvailabilityScheduleRepeatOn> get(@PathVariable Long id) {
        log.debug("REST request to get availabilityScheduleRepeatOn : {}", id);
        return Optional.ofNullable(availabilityScheduleRepeatOnRepository.findOne(id))
            .map(availabilityScheduleRepeatOn -> new ResponseEntity<>(
                availabilityScheduleRepeatOn,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /availabilityScheduleRepeatOns/:id -> delete the "id" availabilityScheduleRepeatOn.
     */
    @RequestMapping(value = "/availabilityScheduleRepeatOns/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete availabilityScheduleRepeatOn : {}", id);
        availabilityScheduleRepeatOnRepository.delete(id);
    }
}
