package com.ews.parkswift.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ews.parkswift.domain.ParkingSpaceImage;
import com.ews.parkswift.repository.ParkingSpaceImageRepository;
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
 * REST controller for managing ParkingSpaceImage.
 */
@RestController
@RequestMapping("/api")
public class ParkingSpaceImageResource {

    private final Logger log = LoggerFactory.getLogger(ParkingSpaceImageResource.class);

    @Inject
    private ParkingSpaceImageRepository parkingSpaceImageRepository;

    /**
     * POST  /parkingSpaceImages -> Create a new parkingSpaceImage.
     */
    @RequestMapping(value = "/parkingSpaceImages",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> create(@Valid @RequestBody ParkingSpaceImage parkingSpaceImage) throws URISyntaxException {
        log.debug("REST request to save ParkingSpaceImage : {}", parkingSpaceImage);
        if (parkingSpaceImage.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new parkingSpaceImage cannot already have an ID").build();
        }
        parkingSpaceImageRepository.save(parkingSpaceImage);
        return ResponseEntity.created(new URI("/api/parkingSpaceImages/" + parkingSpaceImage.getId())).build();
    }

    /**
     * PUT  /parkingSpaceImages -> Updates an existing parkingSpaceImage.
     */
    @RequestMapping(value = "/parkingSpaceImages",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> update(@Valid @RequestBody ParkingSpaceImage parkingSpaceImage) throws URISyntaxException {
        log.debug("REST request to update ParkingSpaceImage : {}", parkingSpaceImage);
        if (parkingSpaceImage.getId() == null) {
            return create(parkingSpaceImage);
        }
        parkingSpaceImageRepository.save(parkingSpaceImage);
        return ResponseEntity.ok().build();
    }

    /**
     * GET  /parkingSpaceImages -> get all the parkingSpaceImages.
     */
    @RequestMapping(value = "/parkingSpaceImages",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<ParkingSpaceImage> getAll() {
        log.debug("REST request to get all ParkingSpaceImages");
        return parkingSpaceImageRepository.findAll();
    }

    /**
     * GET  /parkingSpaceImages/:id -> get the "id" parkingSpaceImage.
     */
    @RequestMapping(value = "/parkingSpaceImages/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ParkingSpaceImage> get(@PathVariable Long id) {
        log.debug("REST request to get ParkingSpaceImage : {}", id);
        return Optional.ofNullable(parkingSpaceImageRepository.findOne(id))
            .map(parkingSpaceImage -> new ResponseEntity<>(
                parkingSpaceImage,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /parkingSpaceImages/:id -> delete the "id" parkingSpaceImage.
     */
    @RequestMapping(value = "/parkingSpaceImages/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete ParkingSpaceImage : {}", id);
        parkingSpaceImageRepository.delete(id);
    }
}
