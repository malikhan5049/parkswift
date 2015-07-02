package com.ews.parkswift.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ews.parkswift.domain.LookupHeader;
import com.ews.parkswift.repository.LookupHeaderRepository;
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
 * REST controller for managing LookupHeader.
 */
@RestController
@RequestMapping("/api")
public class LookupHeaderResource {

    private final Logger log = LoggerFactory.getLogger(LookupHeaderResource.class);

    @Inject
    private LookupHeaderRepository lookupHeaderRepository;

    /**
     * POST  /lookupHeaders -> Create a new lookupHeader.
     */
    @RequestMapping(value = "/lookupHeaders",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> create(@Valid @RequestBody LookupHeader lookupHeader) throws URISyntaxException {
        log.debug("REST request to save LookupHeader : {}", lookupHeader);
        if (lookupHeader.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new lookupHeader cannot already have an ID").build();
        }
        lookupHeaderRepository.save(lookupHeader);
        return ResponseEntity.created(new URI("/api/lookupHeaders/" + lookupHeader.getId())).build();
    }

    /**
     * PUT  /lookupHeaders -> Updates an existing lookupHeader.
     */
    @RequestMapping(value = "/lookupHeaders",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> update(@Valid @RequestBody LookupHeader lookupHeader) throws URISyntaxException {
        log.debug("REST request to update LookupHeader : {}", lookupHeader);
        if (lookupHeader.getId() == null) {
            return create(lookupHeader);
        }
        lookupHeaderRepository.save(lookupHeader);
        return ResponseEntity.ok().build();
    }

    /**
     * GET  /lookupHeaders -> get all the lookupHeaders.
     */
    @RequestMapping(value = "/lookupHeaders",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<LookupHeader> getAll() {
        log.debug("REST request to get all LookupHeaders");
        return lookupHeaderRepository.findAll();
    }


    
    @RequestMapping(value = "/lookupHeaders/{code}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<LookupHeader> getByCode(@PathVariable String code) {
        log.debug("REST request to get LookupHeader : {}", code);
        return Optional.ofNullable(lookupHeaderRepository.findAllByCode(code))
                .map(lookupHeader -> new ResponseEntity<>(
                    lookupHeader,
                    HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /lookupHeaders/:id -> delete the "id" lookupHeader.
     */
    @RequestMapping(value = "/lookupHeaders/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete LookupHeader : {}", id);
        lookupHeaderRepository.delete(id);
    }
}
