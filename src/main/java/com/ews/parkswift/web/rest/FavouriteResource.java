package com.ews.parkswift.web.rest;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;
import javax.validation.Valid;

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
import com.ews.parkswift.domain.Favourite;
import com.ews.parkswift.repository.FavouriteRepository;
import com.ews.parkswift.web.rest.dto.parking.FavouriteLocationsDTO;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

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
    
    
    @RequestMapping(value = "/favouriteLocations",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<FavouriteLocationsDTO> findFavouriteLocations(@Valid @RequestBody FavouriteLocationsDTO favouriteLocationsDTO)throws URISyntaxException, JsonParseException, JsonMappingException, IOException {//@Valid @RequestBody ParkingSpace parkingSpace) throws URISyntaxException {
    	
        log.debug("REST request to find User's favourite location: {}", favouriteLocationsDTO);
        
        List<Favourite> listFavouriteLocations = favouriteRepository.findAllForCurrentUser();
        List<FavouriteLocationsDTO> listReturning = new ArrayList<>();
        FavouriteLocationsDTO _favouriteLocationsDTO = null;

        for(Favourite favourite: listFavouriteLocations){
        	_favouriteLocationsDTO = new FavouriteLocationsDTO(); 
        	listReturning.add(populateFavouriteLocationsDTO(favourite,_favouriteLocationsDTO));
        }
        
        
        return listReturning;
    }
    
    private FavouriteLocationsDTO populateFavouriteLocationsDTO(Favourite favourite, FavouriteLocationsDTO favouriteLocationsDTO){
    		favouriteLocationsDTO.setFavLocId(favourite.getId());
    		favouriteLocationsDTO.setLocId(favourite.getParkingLocation().getId());
    		favouriteLocationsDTO.setLocation(favourite.getParkingLocation().getAddressLine1());
    		favouriteLocationsDTO.setBusinessType(favourite.getParkingLocation().getBussinessType());
    		return favouriteLocationsDTO;
    }
    
    
}
