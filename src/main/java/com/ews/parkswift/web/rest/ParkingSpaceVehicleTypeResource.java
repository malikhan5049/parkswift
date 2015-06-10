package com.ews.parkswift.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ews.parkswift.domain.ParkingSpaceVehicleType;
import com.ews.parkswift.repository.ParkingSpaceVehicleTypeRepository;
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
 * REST controller for managing ParkingSpaceVehicleType.
 */
@RestController
@RequestMapping("/api")
public class ParkingSpaceVehicleTypeResource {

    private final Logger log = LoggerFactory.getLogger(ParkingSpaceVehicleTypeResource.class);

    @Inject
    private ParkingSpaceVehicleTypeRepository parkingSpaceVehicleTypeRepository;

    /**
     * POST  /parkingSpaceVehicleTypes -> Create a new parkingSpaceVehicleType.
     */
    @RequestMapping(value = "/parkingSpaceVehicleTypes",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> create(@Valid @RequestBody ParkingSpaceVehicleType parkingSpaceVehicleType) throws URISyntaxException {
        log.debug("REST request to save ParkingSpaceVehicleType : {}", parkingSpaceVehicleType);
        if (parkingSpaceVehicleType.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new parkingSpaceVehicleType cannot already have an ID").build();
        }
        parkingSpaceVehicleTypeRepository.save(parkingSpaceVehicleType);
        return ResponseEntity.created(new URI("/api/parkingSpaceVehicleTypes/" + parkingSpaceVehicleType.getId())).build();
    }

    /**
     * PUT  /parkingSpaceVehicleTypes -> Updates an existing parkingSpaceVehicleType.
     */
    @RequestMapping(value = "/parkingSpaceVehicleTypes",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> update(@Valid @RequestBody ParkingSpaceVehicleType parkingSpaceVehicleType) throws URISyntaxException {
        log.debug("REST request to update ParkingSpaceVehicleType : {}", parkingSpaceVehicleType);
        if (parkingSpaceVehicleType.getId() == null) {
            return create(parkingSpaceVehicleType);
        }
        parkingSpaceVehicleTypeRepository.save(parkingSpaceVehicleType);
        return ResponseEntity.ok().build();
    }

    /**
     * GET  /parkingSpaceVehicleTypes -> get all the parkingSpaceVehicleTypes.
     */
    @RequestMapping(value = "/parkingSpaceVehicleTypes",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<ParkingSpaceVehicleType> getAll() {
        log.debug("REST request to get all ParkingSpaceVehicleTypes");
        return parkingSpaceVehicleTypeRepository.findAll();
    }

    /**
     * GET  /parkingSpaceVehicleTypes/:id -> get the "id" parkingSpaceVehicleType.
     */
    @RequestMapping(value = "/parkingSpaceVehicleTypes/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ParkingSpaceVehicleType> get(@PathVariable Long id) {
        log.debug("REST request to get ParkingSpaceVehicleType : {}", id);
        return Optional.ofNullable(parkingSpaceVehicleTypeRepository.findOne(id))
            .map(parkingSpaceVehicleType -> new ResponseEntity<>(
                parkingSpaceVehicleType,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /parkingSpaceVehicleTypes/:id -> delete the "id" parkingSpaceVehicleType.
     */
    @RequestMapping(value = "/parkingSpaceVehicleTypes/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete ParkingSpaceVehicleType : {}", id);
        parkingSpaceVehicleTypeRepository.delete(id);
    }
}
