package com.ews.parkswift.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;
import com.ews.parkswift.domain.BookingScheduleRepeatOn;
import com.ews.parkswift.repository.BookingScheduleRepeatOnRepository;

/**
 * REST controller for managing bookingScheduleRepeatOn.
 */
@RestController
@RequestMapping("/api")
public class BookingScheduleRepeatOnResource {

    private final Logger log = LoggerFactory.getLogger(BookingScheduleRepeatOnResource.class);

    @Inject
    private BookingScheduleRepeatOnRepository bookingScheduleRepeatOnRepository;

    /**
     * POST  /bookingScheduleRepeatOns -> Create a new bookingScheduleRepeatOn.
     */
    @RequestMapping(value = "/bookingScheduleRepeatOns",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> create(@RequestBody BookingScheduleRepeatOn bookingScheduleRepeatOn) throws URISyntaxException {
        log.debug("REST request to save bookingScheduleRepeatOn : {}", bookingScheduleRepeatOn);
        if (bookingScheduleRepeatOn.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new bookingScheduleRepeatOn cannot already have an ID").build();
        }
        bookingScheduleRepeatOnRepository.save(bookingScheduleRepeatOn);
        return ResponseEntity.created(new URI("/api/bookingScheduleRepeatOns/" + bookingScheduleRepeatOn.getId())).build();
    }

    /**
     * PUT  /bookingScheduleRepeatOns -> Updates an existing bookingScheduleRepeatOn.
     */
    @RequestMapping(value = "/bookingScheduleRepeatOns",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> update(@RequestBody BookingScheduleRepeatOn bookingScheduleRepeatOn) throws URISyntaxException {
        log.debug("REST request to update bookingScheduleRepeatOn : {}", bookingScheduleRepeatOn);
        if (bookingScheduleRepeatOn.getId() == null) {
            return create(bookingScheduleRepeatOn);
        }
        bookingScheduleRepeatOnRepository.save(bookingScheduleRepeatOn);
        return ResponseEntity.ok().build();
    }

    /**
     * GET  /bookingScheduleRepeatOns -> get all the bookingScheduleRepeatOns.
     */
    @RequestMapping(value = "/bookingScheduleRepeatOns",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<BookingScheduleRepeatOn> getAll() {
        log.debug("REST request to get all bookingScheduleRepeatOns");
        return bookingScheduleRepeatOnRepository.findAll();
    }

    /**
     * GET  /bookingScheduleRepeatOns/:id -> get the "id" bookingScheduleRepeatOn.
     */
    @RequestMapping(value = "/bookingScheduleRepeatOns/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<BookingScheduleRepeatOn> get(@PathVariable Long id) {
        log.debug("REST request to get bookingScheduleRepeatOn : {}", id);
        return Optional.ofNullable(bookingScheduleRepeatOnRepository.findOne(id))
            .map(bookingScheduleRepeatOn -> new ResponseEntity<>(
                bookingScheduleRepeatOn,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /bookingScheduleRepeatOns/:id -> delete the "id" bookingScheduleRepeatOn.
     */
    @RequestMapping(value = "/bookingScheduleRepeatOns/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete bookingScheduleRepeatOn : {}", id);
        bookingScheduleRepeatOnRepository.delete(id);
    }
}
