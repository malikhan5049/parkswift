package com.ews.parkswift.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ews.parkswift.domain.ParkingLocationContactInfo;
import com.ews.parkswift.repository.ParkingLocationContactInfoRepository;
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
 * REST controller for managing ParkingLocationContactInfo.
 */
@RestController
@RequestMapping("/api")
public class ParkingLocationContactInfoResource {

    private final Logger log = LoggerFactory.getLogger(ParkingLocationContactInfoResource.class);

    @Inject
    private ParkingLocationContactInfoRepository parkingLocationContactInfoRepository;

    /**
     * POST  /parkingLocationContactInfos -> Create a new parkingLocationContactInfo.
     */
    @RequestMapping(value = "/parkingLocationContactInfos",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> create(@RequestBody ParkingLocationContactInfo parkingLocationContactInfo) throws URISyntaxException {
        log.debug("REST request to save ParkingLocationContactInfo : {}", parkingLocationContactInfo);
        if (parkingLocationContactInfo.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new parkingLocationContactInfo cannot already have an ID").build();
        }
        parkingLocationContactInfoRepository.save(parkingLocationContactInfo);
        return ResponseEntity.created(new URI("/api/parkingLocationContactInfos/" + parkingLocationContactInfo.getId())).build();
    }

    /**
     * PUT  /parkingLocationContactInfos -> Updates an existing parkingLocationContactInfo.
     */
    @RequestMapping(value = "/parkingLocationContactInfos",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> update(@RequestBody ParkingLocationContactInfo parkingLocationContactInfo) throws URISyntaxException {
        log.debug("REST request to update ParkingLocationContactInfo : {}", parkingLocationContactInfo);
        if (parkingLocationContactInfo.getId() == null) {
            return create(parkingLocationContactInfo);
        }
        parkingLocationContactInfoRepository.save(parkingLocationContactInfo);
        return ResponseEntity.ok().build();
    }

    /**
     * GET  /parkingLocationContactInfos -> get all the parkingLocationContactInfos.
     */
    @RequestMapping(value = "/parkingLocationContactInfos",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<ParkingLocationContactInfo> getAll() {
        log.debug("REST request to get all ParkingLocationContactInfos");
        return parkingLocationContactInfoRepository.findAll();
    }

    /**
     * GET  /parkingLocationContactInfos/:id -> get the "id" parkingLocationContactInfo.
     */
    @RequestMapping(value = "/parkingLocationContactInfos/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ParkingLocationContactInfo> get(@PathVariable Long id) {
        log.debug("REST request to get ParkingLocationContactInfo : {}", id);
        return Optional.ofNullable(parkingLocationContactInfoRepository.findOne(id))
            .map(parkingLocationContactInfo -> new ResponseEntity<>(
                parkingLocationContactInfo,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /parkingLocationContactInfos/:id -> delete the "id" parkingLocationContactInfo.
     */
    @RequestMapping(value = "/parkingLocationContactInfos/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete ParkingLocationContactInfo : {}", id);
        parkingLocationContactInfoRepository.delete(id);
    }
}
