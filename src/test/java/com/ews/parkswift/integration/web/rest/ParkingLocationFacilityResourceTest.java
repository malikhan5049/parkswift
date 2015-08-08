package com.ews.parkswift.integration.web.rest;

import com.ews.parkswift.Application;
import com.ews.parkswift.domain.ParkingLocationFacility;
import com.ews.parkswift.repository.ParkingLocationFacilityRepository;
import com.ews.parkswift.web.rest.ParkingLocationFacilityResource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.Matchers.hasItem;

import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ParkingLocationFacilityResource REST controller.
 *
 * @see ParkingLocationFacilityResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class ParkingLocationFacilityResourceTest {

    private static final String DEFAULT_FACILITY = "SAMPLE_TEXT";
    private static final String UPDATED_FACILITY = "UPDATED_TEXT";

    @Inject
    private ParkingLocationFacilityRepository parkingLocationFacilityRepository;

    private MockMvc restParkingLocationFacilityMockMvc;

    private ParkingLocationFacility parkingLocationFacility;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ParkingLocationFacilityResource parkingLocationFacilityResource = new ParkingLocationFacilityResource();
        ReflectionTestUtils.setField(parkingLocationFacilityResource, "parkingLocationFacilityRepository", parkingLocationFacilityRepository);
        this.restParkingLocationFacilityMockMvc = MockMvcBuilders.standaloneSetup(parkingLocationFacilityResource).build();
    }

    @Before
    public void initTest() {
        parkingLocationFacility = new ParkingLocationFacility();
        parkingLocationFacility.setFacility(DEFAULT_FACILITY);
    }

    @Test
    @Transactional
    public void createParkingLocationFacility() throws Exception {
        int databaseSizeBeforeCreate = parkingLocationFacilityRepository.findAll().size();

        // Create the ParkingLocationFacility
        restParkingLocationFacilityMockMvc.perform(post("/api/parkingLocationFacilitys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(parkingLocationFacility)))
                .andExpect(status().isCreated());

        // Validate the ParkingLocationFacility in the database
        List<ParkingLocationFacility> parkingLocationFacilitys = parkingLocationFacilityRepository.findAll();
        assertThat(parkingLocationFacilitys).hasSize(databaseSizeBeforeCreate + 1);
        ParkingLocationFacility testParkingLocationFacility = parkingLocationFacilitys.get(parkingLocationFacilitys.size() - 1);
        assertThat(testParkingLocationFacility.getFacility()).isEqualTo(DEFAULT_FACILITY);
    }

    @Test
    @Transactional
    public void checkFacilityIsRequired() throws Exception {
        // Validate the database is empty
        assertThat(parkingLocationFacilityRepository.findAll()).hasSize(0);
        // set the field null
        parkingLocationFacility.setFacility(null);

        // Create the ParkingLocationFacility, which fails.
        restParkingLocationFacilityMockMvc.perform(post("/api/parkingLocationFacilitys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(parkingLocationFacility)))
                .andExpect(status().isBadRequest());

        // Validate the database is still empty
        List<ParkingLocationFacility> parkingLocationFacilitys = parkingLocationFacilityRepository.findAll();
        assertThat(parkingLocationFacilitys).hasSize(0);
    }

    @Test
    @Transactional
    public void getAllParkingLocationFacilitys() throws Exception {
        // Initialize the database
        parkingLocationFacilityRepository.saveAndFlush(parkingLocationFacility);

        // Get all the parkingLocationFacilitys
        restParkingLocationFacilityMockMvc.perform(get("/api/parkingLocationFacilitys"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(parkingLocationFacility.getId().intValue())))
                .andExpect(jsonPath("$.[*].facility").value(hasItem(DEFAULT_FACILITY.toString())));
    }

    @Test
    @Transactional
    public void getParkingLocationFacility() throws Exception {
        // Initialize the database
        parkingLocationFacilityRepository.saveAndFlush(parkingLocationFacility);

        // Get the parkingLocationFacility
        restParkingLocationFacilityMockMvc.perform(get("/api/parkingLocationFacilitys/{id}", parkingLocationFacility.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(parkingLocationFacility.getId().intValue()))
            .andExpect(jsonPath("$.facility").value(DEFAULT_FACILITY.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingParkingLocationFacility() throws Exception {
        // Get the parkingLocationFacility
        restParkingLocationFacilityMockMvc.perform(get("/api/parkingLocationFacilitys/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateParkingLocationFacility() throws Exception {
        // Initialize the database
        parkingLocationFacilityRepository.saveAndFlush(parkingLocationFacility);

		int databaseSizeBeforeUpdate = parkingLocationFacilityRepository.findAll().size();

        // Update the parkingLocationFacility
        parkingLocationFacility.setFacility(UPDATED_FACILITY);
        restParkingLocationFacilityMockMvc.perform(put("/api/parkingLocationFacilitys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(parkingLocationFacility)))
                .andExpect(status().isOk());

        // Validate the ParkingLocationFacility in the database
        List<ParkingLocationFacility> parkingLocationFacilitys = parkingLocationFacilityRepository.findAll();
        assertThat(parkingLocationFacilitys).hasSize(databaseSizeBeforeUpdate);
        ParkingLocationFacility testParkingLocationFacility = parkingLocationFacilitys.get(parkingLocationFacilitys.size() - 1);
        assertThat(testParkingLocationFacility.getFacility()).isEqualTo(UPDATED_FACILITY);
    }

    @Test
    @Transactional
    public void deleteParkingLocationFacility() throws Exception {
        // Initialize the database
        parkingLocationFacilityRepository.saveAndFlush(parkingLocationFacility);

		int databaseSizeBeforeDelete = parkingLocationFacilityRepository.findAll().size();

        // Get the parkingLocationFacility
        restParkingLocationFacilityMockMvc.perform(delete("/api/parkingLocationFacilitys/{id}", parkingLocationFacility.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<ParkingLocationFacility> parkingLocationFacilitys = parkingLocationFacilityRepository.findAll();
        assertThat(parkingLocationFacilitys).hasSize(databaseSizeBeforeDelete - 1);
    }
}
