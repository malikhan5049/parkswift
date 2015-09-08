package com.ews.parkswift.web.rest;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;
import com.ews.parkswift.domain.ParkingSpace;
import com.ews.parkswift.repository.ParkingLocationRepository;
import com.ews.parkswift.repository.ParkingSpaceRepository;
import com.ews.parkswift.service.ParkingSpaceService;
import com.ews.parkswift.vo.ParkingSpaceValidator;
import com.ews.parkswift.web.rest.util.PaginationUtil;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * REST controller for managing ParkingSpace.
 */
@RestController
@RequestMapping("/api")
public class ParkingSpaceResource {

    private final Logger log = LoggerFactory.getLogger(ParkingSpaceResource.class);
    private final  static ObjectMapper mapper =  new ObjectMapper();
    
    @Inject
    private ParkingSpaceRepository parkingSpaceRepository;
    
    @Inject
    private ParkingLocationRepository parkingLocationRepository;
    
    @Inject
    private ParkingSpaceService parkingSpaceService;
    
    
    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.addValidators(new ParkingSpaceValidator(parkingLocationRepository));
    }
    
    
    
	
	
    /**
     * POST  /parkingSpaces -> Create a new parkingSpace.
     * @throws IOException 
     * @throws JsonMappingException 
     * @throws JsonParseException 
     */
	@RequestMapping(value = "/parkingSpaces",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<?> create(@Valid @RequestBody ParkingSpace parkingSpace)throws URISyntaxException, JsonParseException, JsonMappingException, IOException {//@Valid @RequestBody ParkingSpace parkingSpace) throws URISyntaxException {
    	
        log.debug("REST request to save ParkingSpace : {}", parkingSpace);
        if (parkingSpace.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new parkingSpace cannot already have an ID").build();
        }
        
        parkingSpaceService.save(parkingSpace);
        return ResponseEntity.created(new URI("/api/parkingSpaces/" + parkingSpace.getId())).body(parkingSpace);
    }

    /**
     * PUT  /parkingSpaces -> Updates an existing parkingSpace.
     * @throws IOException 
     * @throws JsonMappingException 
     * @throws JsonParseException 
     */
    @RequestMapping(value = "/parkingSpaces",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<?> update(@Valid @RequestBody ParkingSpace parkingSpace) throws URISyntaxException, JsonParseException, JsonMappingException, IOException {
        log.debug("REST request to update ParkingSpace : {}", parkingSpace);
        if (parkingSpace.getId() == null) {
            return create(parkingSpace);
        }
        parkingSpaceService.save(parkingSpace);
        return ResponseEntity.ok().build();
    }

    /**
     * GET  /parkingSpaces -> get all the parkingSpaces.
     */
    @RequestMapping(value = "/parkingSpaces/paginated",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<ParkingSpace>> getAll(@RequestParam(value = "page" , required = false) Integer offset,
            @RequestParam(value = "per_page", required = false) Integer limit)
		throws URISyntaxException {
		Page<ParkingSpace> page = parkingSpaceRepository.findAll(PaginationUtil.generatePageRequest(offset, limit));
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/parkingSpaces", offset, limit);
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
    
    /**
     * GET  /parkingSpaces -> get all the parkingSpaces.
     */
    @RequestMapping(value = "/parkingSpaces/parkingLocation{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<ParkingSpace> getAll(@PathVariable Long id)
		throws URISyntaxException {
		return parkingSpaceRepository.findAllByParkingLocationId(id);
    }

    /**
     * GET  /parkingSpaces/:id -> get the "id" parkingSpace.
     */
    @RequestMapping(value = "/parkingSpaces/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ParkingSpace> get(@PathVariable Long id) {
        log.debug("REST request to get ParkingSpace : {}", id);
        return Optional.ofNullable(parkingSpaceRepository.findOne(id))
            .map(parkingSpace -> new ResponseEntity<>(
                parkingSpace,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /parkingSpaces/:id -> delete the "id" parkingSpace.
     */
    @RequestMapping(value = "/parkingSpaces/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete ParkingSpace : {}", id);
        parkingSpaceRepository.delete(id);
    }
}
