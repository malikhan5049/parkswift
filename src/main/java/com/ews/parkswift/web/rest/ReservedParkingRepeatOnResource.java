package com.ews.parkswift.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ews.parkswift.domain.ReservedParkingRepeatOn;
import com.ews.parkswift.repository.ReservedParkingRepeatOnRepository;
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
 * REST controller for managing ReservedParkingRepeatOn.
 */
@RestController
@RequestMapping("/api")
public class ReservedParkingRepeatOnResource {

    private final Logger log = LoggerFactory.getLogger(ReservedParkingRepeatOnResource.class);

    @Inject
    private ReservedParkingRepeatOnRepository reservedParkingRepeatOnRepository;

    /**
     * POST  /reservedParkingRepeatOns -> Create a new reservedParkingRepeatOn.
     */
    @RequestMapping(value = "/reservedParkingRepeatOns",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> create(@RequestBody ReservedParkingRepeatOn reservedParkingRepeatOn) throws URISyntaxException {
        log.debug("REST request to save ReservedParkingRepeatOn : {}", reservedParkingRepeatOn);
        if (reservedParkingRepeatOn.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new reservedParkingRepeatOn cannot already have an ID").build();
        }
        reservedParkingRepeatOnRepository.save(reservedParkingRepeatOn);
        return ResponseEntity.created(new URI("/api/reservedParkingRepeatOns/" + reservedParkingRepeatOn.getId())).build();
    }

    /**
     * PUT  /reservedParkingRepeatOns -> Updates an existing reservedParkingRepeatOn.
     */
    @RequestMapping(value = "/reservedParkingRepeatOns",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> update(@RequestBody ReservedParkingRepeatOn reservedParkingRepeatOn) throws URISyntaxException {
        log.debug("REST request to update ReservedParkingRepeatOn : {}", reservedParkingRepeatOn);
        if (reservedParkingRepeatOn.getId() == null) {
            return create(reservedParkingRepeatOn);
        }
        reservedParkingRepeatOnRepository.save(reservedParkingRepeatOn);
        return ResponseEntity.ok().build();
    }

    /**
     * GET  /reservedParkingRepeatOns -> get all the reservedParkingRepeatOns.
     */
    @RequestMapping(value = "/reservedParkingRepeatOns",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<ReservedParkingRepeatOn> getAll() {
        log.debug("REST request to get all ReservedParkingRepeatOns");
        return reservedParkingRepeatOnRepository.findAll();
    }

    /**
     * GET  /reservedParkingRepeatOns/:id -> get the "id" reservedParkingRepeatOn.
     */
    @RequestMapping(value = "/reservedParkingRepeatOns/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ReservedParkingRepeatOn> get(@PathVariable Long id) {
        log.debug("REST request to get ReservedParkingRepeatOn : {}", id);
        return Optional.ofNullable(reservedParkingRepeatOnRepository.findOne(id))
            .map(reservedParkingRepeatOn -> new ResponseEntity<>(
                reservedParkingRepeatOn,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /reservedParkingRepeatOns/:id -> delete the "id" reservedParkingRepeatOn.
     */
    @RequestMapping(value = "/reservedParkingRepeatOns/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete ReservedParkingRepeatOn : {}", id);
        reservedParkingRepeatOnRepository.delete(id);
    }
}
