package com.ews.parkswift.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ews.parkswift.domain.ReservedParking;
import com.ews.parkswift.repository.ReservedParkingRepository;
import com.ews.parkswift.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
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
 * REST controller for managing ReservedParking.
 */
@RestController
@RequestMapping("/api")
public class ReservedParkingResource {

    private final Logger log = LoggerFactory.getLogger(ReservedParkingResource.class);

    @Inject
    private ReservedParkingRepository reservedParkingRepository;

    /**
     * POST  /reservedParkings -> Create a new reservedParking.
     */
    @RequestMapping(value = "/reservedParkings",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> create(@Valid @RequestBody ReservedParking reservedParking) throws URISyntaxException {
        log.debug("REST request to save ReservedParking : {}", reservedParking);
        if (reservedParking.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new reservedParking cannot already have an ID").build();
        }
        reservedParkingRepository.save(reservedParking);
        return ResponseEntity.created(new URI("/api/reservedParkings/" + reservedParking.getId())).build();
    }

    /**
     * PUT  /reservedParkings -> Updates an existing reservedParking.
     */
    @RequestMapping(value = "/reservedParkings",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> update(@Valid @RequestBody ReservedParking reservedParking) throws URISyntaxException {
        log.debug("REST request to update ReservedParking : {}", reservedParking);
        if (reservedParking.getId() == null) {
            return create(reservedParking);
        }
        reservedParkingRepository.save(reservedParking);
        return ResponseEntity.ok().build();
    }

    /**
     * GET  /reservedParkings -> get all the reservedParkings.
     */
    @RequestMapping(value = "/reservedParkings",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<ReservedParking>> getAll(@RequestParam(value = "page" , required = false) Integer offset,
                                  @RequestParam(value = "per_page", required = false) Integer limit)
        throws URISyntaxException {
        Page<ReservedParking> page = reservedParkingRepository.findAll(PaginationUtil.generatePageRequest(offset, limit));
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/reservedParkings", offset, limit);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /reservedParkings/:id -> get the "id" reservedParking.
     */
    @RequestMapping(value = "/reservedParkings/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ReservedParking> get(@PathVariable Long id) {
        log.debug("REST request to get ReservedParking : {}", id);
        return Optional.ofNullable(reservedParkingRepository.findOne(id))
            .map(reservedParking -> new ResponseEntity<>(
                reservedParking,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /reservedParkings/:id -> delete the "id" reservedParking.
     */
    @RequestMapping(value = "/reservedParkings/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete ReservedParking : {}", id);
        reservedParkingRepository.delete(id);
    }
}
