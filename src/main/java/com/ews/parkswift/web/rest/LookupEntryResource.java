package com.ews.parkswift.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ews.parkswift.domain.LookupEntry;
import com.ews.parkswift.repository.LookupEntryRepository;
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
 * REST controller for managing LookupEntry.
 */
@RestController
@RequestMapping("/api")
public class LookupEntryResource {

    private final Logger log = LoggerFactory.getLogger(LookupEntryResource.class);

    @Inject
    private LookupEntryRepository lookupEntryRepository;

    /**
     * POST  /lookupEntrys -> Create a new lookupEntry.
     */
    @RequestMapping(value = "/lookupEntrys",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> create(@Valid @RequestBody LookupEntry lookupEntry) throws URISyntaxException {
        log.debug("REST request to save LookupEntry : {}", lookupEntry);
        if (lookupEntry.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new lookupEntry cannot already have an ID").build();
        }
        lookupEntryRepository.save(lookupEntry);
        return ResponseEntity.created(new URI("/api/lookupEntrys/" + lookupEntry.getId())).build();
    }

    /**
     * PUT  /lookupEntrys -> Updates an existing lookupEntry.
     */
    @RequestMapping(value = "/lookupEntrys",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> update(@Valid @RequestBody LookupEntry lookupEntry) throws URISyntaxException {
        log.debug("REST request to update LookupEntry : {}", lookupEntry);
        if (lookupEntry.getId() == null) {
            return create(lookupEntry);
        }
        lookupEntryRepository.save(lookupEntry);
        return ResponseEntity.ok().build();
    }

    /**
     * GET  /lookupEntrys -> get all the lookupEntrys.
     */
    @RequestMapping(value = "/lookupEntrys",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<LookupEntry>> getAll(@RequestParam(value = "page" , required = false) Integer offset,
                                  @RequestParam(value = "per_page", required = false) Integer limit)
        throws URISyntaxException {
        Page<LookupEntry> page = lookupEntryRepository.findAll(PaginationUtil.generatePageRequest(offset, limit));
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/lookupEntrys", offset, limit);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /lookupEntrys/:id -> get the "id" lookupEntry.
     */
    @RequestMapping(value = "/lookupEntrys/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<LookupEntry> get(@PathVariable Long id) {
        log.debug("REST request to get LookupEntry : {}", id);
        return Optional.ofNullable(lookupEntryRepository.findOne(id))
            .map(lookupEntry -> new ResponseEntity<>(
                lookupEntry,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /lookupEntrys/:id -> delete the "id" lookupEntry.
     */
    @RequestMapping(value = "/lookupEntrys/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete LookupEntry : {}", id);
        lookupEntryRepository.delete(id);
    }
}
