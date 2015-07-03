package com.ews.parkswift.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ews.parkswift.domain.ParkingLocationImage;
import com.ews.parkswift.repository.ParkingLocationImageRepository;
import com.ews.parkswift.service.ParkingLocationImageService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * REST controller for managing ParkingSpaceImage.
 */
@RestController
@RequestMapping("/api")
public class ParkingLocationImageResource {

    private final Logger log = LoggerFactory.getLogger(ParkingLocationImageResource.class);

    @Inject
    private ParkingLocationImageRepository parkingLocationImageRepository;
    @Inject
    private ParkingLocationImageService parkingLocationImageService;

    /**
     * POST  /parkingLocationImages -> Create a new parkingSpaceImage.
     */
    @RequestMapping(value = "/parkingLocationImages",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> create(@Valid @RequestBody ParkingLocationImage parkingSpaceImage) throws URISyntaxException {
        log.debug("REST request to save ParkingSpaceImage : {}", parkingSpaceImage);
        if (parkingSpaceImage.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new parkingSpaceImage cannot already have an ID").build();
        }
        parkingLocationImageRepository.save(parkingSpaceImage);
        return ResponseEntity.created(new URI("/api/parkingLocationImages/" + parkingSpaceImage.getId())).build();
    }

    /**
     * PUT  /parkingLocationImages -> Updates an existing parkingSpaceImage.
     */
    @RequestMapping(value = "/parkingLocationImages",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> update(@Valid @RequestBody ParkingLocationImage parkingSpaceImage) throws URISyntaxException {
        log.debug("REST request to update ParkingSpaceImage : {}", parkingSpaceImage);
        if (parkingSpaceImage.getId() == null) {
            return create(parkingSpaceImage);
        }
        parkingLocationImageRepository.save(parkingSpaceImage);
        return ResponseEntity.ok().build();
    }

    /**
     * GET  /parkingLocationImages -> get all the parkingSpaceImages.
     */
    @RequestMapping(value = "/parkingLocationImages/{parkingLocationId}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @Timed
    public ResponseEntity<List<byte[]>> getAllByParkingLocationId(@PathVariable Long parkingLocationId) {
        log.debug("REST request to get all ParkingLocationImages");
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        return new ResponseEntity<List<byte[]>>(parkingLocationImageService.findAllByParkingLocation(parkingLocationId), headers, HttpStatus.OK);
    }

    /**
     * GET  /parkingLocationImages/:id -> get the "id" parkingSpaceImage.
     *//*
    @RequestMapping(value = "/parkingLocationImages/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ParkingLocationImage> get(@PathVariable Long id) {
        log.debug("REST request to get ParkingSpaceImage : {}", id);
        return Optional.ofNullable(parkingLocationImageRepository.findOne(id))
            .map(parkingSpaceImage -> new ResponseEntity<>(
                parkingSpaceImage,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }*/

    /**
     * DELETE  /parkingLocationImages/:id -> delete the "id" parkingSpaceImage.
     */
    @RequestMapping(value = "/parkingLocationImages/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete ParkingSpaceImage : {}", id);
        parkingLocationImageRepository.delete(id);
    }
}
