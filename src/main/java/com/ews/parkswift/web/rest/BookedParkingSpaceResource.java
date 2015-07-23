package com.ews.parkswift.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ews.parkswift.domain.BookedParkingSpace;
import com.ews.parkswift.repository.BookedParkingSpaceRepository;
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
 * REST controller for managing BookedParkingSpace.
 */
@RestController
@RequestMapping("/api")
public class BookedParkingSpaceResource {

    private final Logger log = LoggerFactory.getLogger(BookedParkingSpaceResource.class);

    @Inject
    private BookedParkingSpaceRepository bookedParkingSpaceRepository;

    /**
     * POST  /bookedParkingSpaces -> Create a new bookedParkingSpace.
     */
    @RequestMapping(value = "/bookedParkingSpaces",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> create(@RequestBody BookedParkingSpace bookedParkingSpace) throws URISyntaxException {
        log.debug("REST request to save BookedParkingSpace : {}", bookedParkingSpace);
        if (bookedParkingSpace.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new bookedParkingSpace cannot already have an ID").build();
        }
        bookedParkingSpaceRepository.save(bookedParkingSpace);
        return ResponseEntity.created(new URI("/api/bookedParkingSpaces/" + bookedParkingSpace.getId())).build();
    }

    /**
     * PUT  /bookedParkingSpaces -> Updates an existing bookedParkingSpace.
     */
    @RequestMapping(value = "/bookedParkingSpaces",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> update(@RequestBody BookedParkingSpace bookedParkingSpace) throws URISyntaxException {
        log.debug("REST request to update BookedParkingSpace : {}", bookedParkingSpace);
        if (bookedParkingSpace.getId() == null) {
            return create(bookedParkingSpace);
        }
        bookedParkingSpaceRepository.save(bookedParkingSpace);
        return ResponseEntity.ok().build();
    }

    /**
     * GET  /bookedParkingSpaces -> get all the bookedParkingSpaces.
     */
    @RequestMapping(value = "/bookedParkingSpaces",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<BookedParkingSpace> getAll() {
        log.debug("REST request to get all BookedParkingSpaces");
        return bookedParkingSpaceRepository.findAll();
    }

    /**
     * GET  /bookedParkingSpaces/:id -> get the "id" bookedParkingSpace.
     */
    @RequestMapping(value = "/bookedParkingSpaces/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<BookedParkingSpace> get(@PathVariable Long id) {
        log.debug("REST request to get BookedParkingSpace : {}", id);
        return Optional.ofNullable(bookedParkingSpaceRepository.findOne(id))
            .map(bookedParkingSpace -> new ResponseEntity<>(
                bookedParkingSpace,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /bookedParkingSpaces/:id -> delete the "id" bookedParkingSpace.
     */
    @RequestMapping(value = "/bookedParkingSpaces/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete BookedParkingSpace : {}", id);
        bookedParkingSpaceRepository.delete(id);
    }
}
