package com.ews.parkswift.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ews.parkswift.domain.AvailabilitySchedule;
import com.ews.parkswift.repository.AvailabilityScheduleRepository;
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
 * REST controller for managing availabilitySchedule.
 */
@RestController
@RequestMapping("/api")
public class AvailabilityScheduleResource {

    private final Logger log = LoggerFactory.getLogger(AvailabilityScheduleResource.class);

    @Inject
    private AvailabilityScheduleRepository availabilityScheduleRepository;

    /**
     * POST  /availabilitySchedules -> Create a new availabilitySchedule.
     */
    @RequestMapping(value = "/availabilitySchedules",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> create(@Valid @RequestBody AvailabilitySchedule availabilitySchedule) throws URISyntaxException {
        log.debug("REST request to save availabilitySchedule : {}", availabilitySchedule);
        if (availabilitySchedule.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new availabilitySchedule cannot already have an ID").build();
        }
        availabilityScheduleRepository.save(availabilitySchedule);
        return ResponseEntity.created(new URI("/api/availabilitySchedules/" + availabilitySchedule.getId())).build();
    }

    /**
     * PUT  /availabilitySchedules -> Updates an existing availabilitySchedule.
     */
    @RequestMapping(value = "/availabilitySchedules",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> update(@Valid @RequestBody AvailabilitySchedule availabilitySchedule) throws URISyntaxException {
        log.debug("REST request to update availabilitySchedule : {}", availabilitySchedule);
        if (availabilitySchedule.getId() == null) {
            return create(availabilitySchedule);
        }
        availabilityScheduleRepository.save(availabilitySchedule);
        return ResponseEntity.ok().build();
    }

    /**
     * GET  /availabilitySchedules -> get all the availabilitySchedules.
     */
    @RequestMapping(value = "/availabilitySchedules",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<AvailabilitySchedule>> getAll(@RequestParam(value = "page" , required = false) Integer offset,
                                  @RequestParam(value = "per_page", required = false) Integer limit)
        throws URISyntaxException {
        Page<AvailabilitySchedule> page = availabilityScheduleRepository.findAll(PaginationUtil.generatePageRequest(offset, limit));
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/availabilitySchedules", offset, limit);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /availabilitySchedules/:id -> get the "id" availabilitySchedule.
     */
    @RequestMapping(value = "/availabilitySchedules/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<AvailabilitySchedule> get(@PathVariable Long id) {
        log.debug("REST request to get availabilitySchedule : {}", id);
        return Optional.ofNullable(availabilityScheduleRepository.findOne(id))
            .map(availabilitySchedule -> new ResponseEntity<>(
                availabilitySchedule,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /availabilitySchedules/:id -> delete the "id" availabilitySchedule.
     */
    @RequestMapping(value = "/availabilitySchedules/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete availabilitySchedule : {}", id);
        availabilityScheduleRepository.delete(id);
    }
}
