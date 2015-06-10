package com.ews.parkswift.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ews.parkswift.domain.Favourite;
import com.ews.parkswift.repository.FavouriteRepository;
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
 * REST controller for managing Favourite.
 */
@RestController
@RequestMapping("/api")
public class FavouriteResource {

    private final Logger log = LoggerFactory.getLogger(FavouriteResource.class);

    @Inject
    private FavouriteRepository favouriteRepository;

    /**
     * POST  /favourites -> Create a new favourite.
     */
    @RequestMapping(value = "/favourites",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> create(@RequestBody Favourite favourite) throws URISyntaxException {
        log.debug("REST request to save Favourite : {}", favourite);
        if (favourite.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new favourite cannot already have an ID").build();
        }
        favouriteRepository.save(favourite);
        return ResponseEntity.created(new URI("/api/favourites/" + favourite.getId())).build();
    }

    /**
     * PUT  /favourites -> Updates an existing favourite.
     */
    @RequestMapping(value = "/favourites",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> update(@RequestBody Favourite favourite) throws URISyntaxException {
        log.debug("REST request to update Favourite : {}", favourite);
        if (favourite.getId() == null) {
            return create(favourite);
        }
        favouriteRepository.save(favourite);
        return ResponseEntity.ok().build();
    }

    /**
     * GET  /favourites -> get all the favourites.
     */
    @RequestMapping(value = "/favourites",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Favourite> getAll() {
        log.debug("REST request to get all Favourites");
        return favouriteRepository.findAll();
    }

    /**
     * GET  /favourites/:id -> get the "id" favourite.
     */
    @RequestMapping(value = "/favourites/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Favourite> get(@PathVariable Long id) {
        log.debug("REST request to get Favourite : {}", id);
        return Optional.ofNullable(favouriteRepository.findOne(id))
            .map(favourite -> new ResponseEntity<>(
                favourite,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /favourites/:id -> delete the "id" favourite.
     */
    @RequestMapping(value = "/favourites/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete Favourite : {}", id);
        favouriteRepository.delete(id);
    }
}
