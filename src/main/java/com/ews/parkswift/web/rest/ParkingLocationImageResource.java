package com.ews.parkswift.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import javax.inject.Inject;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;
import com.ews.parkswift.domain.ParkingLocationImage;
import com.ews.parkswift.repository.ParkingLocationImageRepository;
import com.ews.parkswift.service.ParkingLocationImageService;

/**
 * REST controller for managing parkingLocationImage.
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
     * POST  /parkingLocationImages -> Create a new parkingLocationImage.
     */
    @RequestMapping(value = "/parkingLocationImages",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> create(@Valid @RequestBody ParkingLocationImage parkingLocationImage) throws URISyntaxException {
        log.debug("REST request to save parkingLocationImage : {}", parkingLocationImage);
        if (parkingLocationImage.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new parkingSpaceImage cannot already have an ID").build();
        }
        parkingLocationImageRepository.save(parkingLocationImage);
        return ResponseEntity.created(new URI("/api/parkingLocationImages/" + parkingLocationImage.getId())).build();
    }

    /**
     * PUT  /parkingLocationImages -> Updates an existing parkingLocationImage.
     */
    @RequestMapping(value = "/parkingLocationImages",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> update(@Valid @RequestBody ParkingLocationImage parkingLocationImage) throws URISyntaxException {
        log.debug("REST request to update parkingLocationImage : {}", parkingLocationImage);
        if (parkingLocationImage.getId() == null) {
            return create(parkingLocationImage);
        }
        parkingLocationImageRepository.save(parkingLocationImage);
        return ResponseEntity.ok().build();
    }

    /**
     * GET  /parkingLocationImages -> get all the parkingLocationImage.
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
     * GET  /parkingLocationImages/:id -> get the "id" parkingLocationImage.
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
     * DELETE  /parkingLocationImages/:id -> delete the "id" parkingLocationImage.
     */
    @RequestMapping(value = "/parkingLocationImages/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete parkingLocationImage : {}", id);
        parkingLocationImageRepository.delete(id);
    }
}
