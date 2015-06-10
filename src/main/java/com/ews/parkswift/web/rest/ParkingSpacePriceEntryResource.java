package com.ews.parkswift.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ews.parkswift.domain.ParkingSpacePriceEntry;
import com.ews.parkswift.repository.ParkingSpacePriceEntryRepository;
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
 * REST controller for managing ParkingSpacePriceEntry.
 */
@RestController
@RequestMapping("/api")
public class ParkingSpacePriceEntryResource {

    private final Logger log = LoggerFactory.getLogger(ParkingSpacePriceEntryResource.class);

    @Inject
    private ParkingSpacePriceEntryRepository parkingSpacePriceEntryRepository;

    /**
     * POST  /parkingSpacePriceEntrys -> Create a new parkingSpacePriceEntry.
     */
    @RequestMapping(value = "/parkingSpacePriceEntrys",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> create(@Valid @RequestBody ParkingSpacePriceEntry parkingSpacePriceEntry) throws URISyntaxException {
        log.debug("REST request to save ParkingSpacePriceEntry : {}", parkingSpacePriceEntry);
        if (parkingSpacePriceEntry.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new parkingSpacePriceEntry cannot already have an ID").build();
        }
        parkingSpacePriceEntryRepository.save(parkingSpacePriceEntry);
        return ResponseEntity.created(new URI("/api/parkingSpacePriceEntrys/" + parkingSpacePriceEntry.getId())).build();
    }

    /**
     * PUT  /parkingSpacePriceEntrys -> Updates an existing parkingSpacePriceEntry.
     */
    @RequestMapping(value = "/parkingSpacePriceEntrys",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> update(@Valid @RequestBody ParkingSpacePriceEntry parkingSpacePriceEntry) throws URISyntaxException {
        log.debug("REST request to update ParkingSpacePriceEntry : {}", parkingSpacePriceEntry);
        if (parkingSpacePriceEntry.getId() == null) {
            return create(parkingSpacePriceEntry);
        }
        parkingSpacePriceEntryRepository.save(parkingSpacePriceEntry);
        return ResponseEntity.ok().build();
    }

    /**
     * GET  /parkingSpacePriceEntrys -> get all the parkingSpacePriceEntrys.
     */
    @RequestMapping(value = "/parkingSpacePriceEntrys",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<ParkingSpacePriceEntry> getAll() {
        log.debug("REST request to get all ParkingSpacePriceEntrys");
        return parkingSpacePriceEntryRepository.findAll();
    }

    /**
     * GET  /parkingSpacePriceEntrys/:id -> get the "id" parkingSpacePriceEntry.
     */
    @RequestMapping(value = "/parkingSpacePriceEntrys/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ParkingSpacePriceEntry> get(@PathVariable Long id) {
        log.debug("REST request to get ParkingSpacePriceEntry : {}", id);
        return Optional.ofNullable(parkingSpacePriceEntryRepository.findOne(id))
            .map(parkingSpacePriceEntry -> new ResponseEntity<>(
                parkingSpacePriceEntry,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /parkingSpacePriceEntrys/:id -> delete the "id" parkingSpacePriceEntry.
     */
    @RequestMapping(value = "/parkingSpacePriceEntrys/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete ParkingSpacePriceEntry : {}", id);
        parkingSpacePriceEntryRepository.delete(id);
    }
}
