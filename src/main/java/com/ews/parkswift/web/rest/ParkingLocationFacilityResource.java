package com.ews.parkswift.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ews.parkswift.domain.ParkingLocationFacility;
import com.ews.parkswift.repository.ParkingLocationFacilityRepository;
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
 * REST controller for managing ParkingLocationFacility.
 */
@RestController
@RequestMapping("/api")
public class ParkingLocationFacilityResource {

    private final Logger log = LoggerFactory.getLogger(ParkingLocationFacilityResource.class);

    @Inject
    private ParkingLocationFacilityRepository parkingLocationFacilityRepository;

    /**
     * POST  /parkingLocationFacilitys -> Create a new parkingLocationFacility.
     */
    @RequestMapping(value = "/parkingLocationFacilitys",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> create(@Valid @RequestBody ParkingLocationFacility parkingLocationFacility) throws URISyntaxException {
        log.debug("REST request to save ParkingLocationFacility : {}", parkingLocationFacility);
        if (parkingLocationFacility.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new parkingLocationFacility cannot already have an ID").build();
        }
        parkingLocationFacilityRepository.save(parkingLocationFacility);
        return ResponseEntity.created(new URI("/api/parkingLocationFacilitys/" + parkingLocationFacility.getId())).build();
    }

    /**
     * PUT  /parkingLocationFacilitys -> Updates an existing parkingLocationFacility.
     */
    @RequestMapping(value = "/parkingLocationFacilitys",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> update(@Valid @RequestBody ParkingLocationFacility parkingLocationFacility) throws URISyntaxException {
        log.debug("REST request to update ParkingLocationFacility : {}", parkingLocationFacility);
        if (parkingLocationFacility.getId() == null) {
            return create(parkingLocationFacility);
        }
        parkingLocationFacilityRepository.save(parkingLocationFacility);
        return ResponseEntity.ok().build();
    }

    /**
     * GET  /parkingLocationFacilitys -> get all the parkingLocationFacilitys.
     */
    @RequestMapping(value = "/parkingLocationFacilitys",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<ParkingLocationFacility> getAll() {
        log.debug("REST request to get all ParkingLocationFacilitys");
        return parkingLocationFacilityRepository.findAll();
    }

    /**
     * GET  /parkingLocationFacilitys/:id -> get the "id" parkingLocationFacility.
     */
    @RequestMapping(value = "/parkingLocationFacilitys/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ParkingLocationFacility> get(@PathVariable Long id) {
        log.debug("REST request to get ParkingLocationFacility : {}", id);
        return Optional.ofNullable(parkingLocationFacilityRepository.findOne(id))
            .map(parkingLocationFacility -> new ResponseEntity<>(
                parkingLocationFacility,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /parkingLocationFacilitys/:id -> delete the "id" parkingLocationFacility.
     */
    @RequestMapping(value = "/parkingLocationFacilitys/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete ParkingLocationFacility : {}", id);
        parkingLocationFacilityRepository.delete(id);
    }
}
