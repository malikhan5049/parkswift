package com.ews.parkswift.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ews.parkswift.domain.ParkingLocation;
import com.ews.parkswift.repository.ParkingLocationRepository;
import com.ews.parkswift.service.ParkingLocationService;

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
 * REST controller for managing ParkingLocation.
 */
@RestController
@RequestMapping("/api")
public class ParkingLocationResource {

    private final Logger log = LoggerFactory.getLogger(ParkingLocationResource.class);

    @Inject
    private ParkingLocationRepository parkingLocationRepository;
    @Inject
    private ParkingLocationService parkingLocationService;

    /**
     * POST  /parkingLocations -> Create a new parkingLocation.
     */
    @RequestMapping(value = "/parkingLocations",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> create(@Valid @RequestBody ParkingLocation parkingLocation) throws URISyntaxException {
        log.debug("REST request to save ParkingLocation : {}", parkingLocation);
        if (parkingLocation.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new parkingLocation cannot already have an ID").build();
        }
        parkingLocationService.save(parkingLocation);
        return ResponseEntity.created(new URI("/api/parkingLocations/" + parkingLocation.getId())).build();
    }

    /**
     * PUT  /parkingLocations -> Updates an existing parkingLocation.
     */
    @RequestMapping(value = "/parkingLocations",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> update(@Valid @RequestBody ParkingLocation parkingLocation) throws URISyntaxException {
        log.debug("REST request to update ParkingLocation : {}", parkingLocation);
        if (parkingLocation.getId() == null) {
            return create(parkingLocation);
        }
        parkingLocationService.save(parkingLocation);
        return ResponseEntity.ok().build();
    }

    /**
     * GET  /parkingLocations -> get all the parkingLocations.
     */
    @RequestMapping(value = "/parkingLocations",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<ParkingLocation> getAll() {
        log.debug("REST request to get all ParkingLocations");
        return parkingLocationRepository.findAll();
    }

    /**
     * GET  /parkingLocations/:id -> get the "id" parkingLocation.
     */
    @RequestMapping(value = "/parkingLocations/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ParkingLocation> get(@PathVariable Long id) {
        log.debug("REST request to get ParkingLocation : {}", id);
        return Optional.ofNullable(parkingLocationService.findOne(id))
            .map(parkingLocation -> new ResponseEntity<>(
                parkingLocation,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    
    
    

    /**
     * DELETE  /parkingLocations/:id -> delete the "id" parkingLocation.
     */
    @RequestMapping(value = "/parkingLocations/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete ParkingLocation : {}", id);
        parkingLocationService.delete(id);
    }
}
