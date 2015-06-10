package com.ews.parkswift.web.rest;

import com.ews.parkswift.Application;
import com.ews.parkswift.domain.ParkingSpaceVehicleType;
import com.ews.parkswift.repository.ParkingSpaceVehicleTypeRepository;

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
 * Test class for the ParkingSpaceVehicleTypeResource REST controller.
 *
 * @see ParkingSpaceVehicleTypeResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class ParkingSpaceVehicleTypeResourceTest {

    private static final String DEFAULT_TYPE = "SAMPLE_TEXT";
    private static final String UPDATED_TYPE = "UPDATED_TEXT";

    @Inject
    private ParkingSpaceVehicleTypeRepository parkingSpaceVehicleTypeRepository;

    private MockMvc restParkingSpaceVehicleTypeMockMvc;

    private ParkingSpaceVehicleType parkingSpaceVehicleType;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ParkingSpaceVehicleTypeResource parkingSpaceVehicleTypeResource = new ParkingSpaceVehicleTypeResource();
        ReflectionTestUtils.setField(parkingSpaceVehicleTypeResource, "parkingSpaceVehicleTypeRepository", parkingSpaceVehicleTypeRepository);
        this.restParkingSpaceVehicleTypeMockMvc = MockMvcBuilders.standaloneSetup(parkingSpaceVehicleTypeResource).build();
    }

    @Before
    public void initTest() {
        parkingSpaceVehicleType = new ParkingSpaceVehicleType();
        parkingSpaceVehicleType.setType(DEFAULT_TYPE);
    }

    @Test
    @Transactional
    public void createParkingSpaceVehicleType() throws Exception {
        int databaseSizeBeforeCreate = parkingSpaceVehicleTypeRepository.findAll().size();

        // Create the ParkingSpaceVehicleType
        restParkingSpaceVehicleTypeMockMvc.perform(post("/api/parkingSpaceVehicleTypes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(parkingSpaceVehicleType)))
                .andExpect(status().isCreated());

        // Validate the ParkingSpaceVehicleType in the database
        List<ParkingSpaceVehicleType> parkingSpaceVehicleTypes = parkingSpaceVehicleTypeRepository.findAll();
        assertThat(parkingSpaceVehicleTypes).hasSize(databaseSizeBeforeCreate + 1);
        ParkingSpaceVehicleType testParkingSpaceVehicleType = parkingSpaceVehicleTypes.get(parkingSpaceVehicleTypes.size() - 1);
        assertThat(testParkingSpaceVehicleType.getType()).isEqualTo(DEFAULT_TYPE);
    }

    @Test
    @Transactional
    public void checkTypeIsRequired() throws Exception {
        // Validate the database is empty
        assertThat(parkingSpaceVehicleTypeRepository.findAll()).hasSize(0);
        // set the field null
        parkingSpaceVehicleType.setType(null);

        // Create the ParkingSpaceVehicleType, which fails.
        restParkingSpaceVehicleTypeMockMvc.perform(post("/api/parkingSpaceVehicleTypes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(parkingSpaceVehicleType)))
                .andExpect(status().isBadRequest());

        // Validate the database is still empty
        List<ParkingSpaceVehicleType> parkingSpaceVehicleTypes = parkingSpaceVehicleTypeRepository.findAll();
        assertThat(parkingSpaceVehicleTypes).hasSize(0);
    }

    @Test
    @Transactional
    public void getAllParkingSpaceVehicleTypes() throws Exception {
        // Initialize the database
        parkingSpaceVehicleTypeRepository.saveAndFlush(parkingSpaceVehicleType);

        // Get all the parkingSpaceVehicleTypes
        restParkingSpaceVehicleTypeMockMvc.perform(get("/api/parkingSpaceVehicleTypes"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(parkingSpaceVehicleType.getId().intValue())))
                .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())));
    }

    @Test
    @Transactional
    public void getParkingSpaceVehicleType() throws Exception {
        // Initialize the database
        parkingSpaceVehicleTypeRepository.saveAndFlush(parkingSpaceVehicleType);

        // Get the parkingSpaceVehicleType
        restParkingSpaceVehicleTypeMockMvc.perform(get("/api/parkingSpaceVehicleTypes/{id}", parkingSpaceVehicleType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(parkingSpaceVehicleType.getId().intValue()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingParkingSpaceVehicleType() throws Exception {
        // Get the parkingSpaceVehicleType
        restParkingSpaceVehicleTypeMockMvc.perform(get("/api/parkingSpaceVehicleTypes/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateParkingSpaceVehicleType() throws Exception {
        // Initialize the database
        parkingSpaceVehicleTypeRepository.saveAndFlush(parkingSpaceVehicleType);

		int databaseSizeBeforeUpdate = parkingSpaceVehicleTypeRepository.findAll().size();

        // Update the parkingSpaceVehicleType
        parkingSpaceVehicleType.setType(UPDATED_TYPE);
        restParkingSpaceVehicleTypeMockMvc.perform(put("/api/parkingSpaceVehicleTypes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(parkingSpaceVehicleType)))
                .andExpect(status().isOk());

        // Validate the ParkingSpaceVehicleType in the database
        List<ParkingSpaceVehicleType> parkingSpaceVehicleTypes = parkingSpaceVehicleTypeRepository.findAll();
        assertThat(parkingSpaceVehicleTypes).hasSize(databaseSizeBeforeUpdate);
        ParkingSpaceVehicleType testParkingSpaceVehicleType = parkingSpaceVehicleTypes.get(parkingSpaceVehicleTypes.size() - 1);
        assertThat(testParkingSpaceVehicleType.getType()).isEqualTo(UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void deleteParkingSpaceVehicleType() throws Exception {
        // Initialize the database
        parkingSpaceVehicleTypeRepository.saveAndFlush(parkingSpaceVehicleType);

		int databaseSizeBeforeDelete = parkingSpaceVehicleTypeRepository.findAll().size();

        // Get the parkingSpaceVehicleType
        restParkingSpaceVehicleTypeMockMvc.perform(delete("/api/parkingSpaceVehicleTypes/{id}", parkingSpaceVehicleType.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<ParkingSpaceVehicleType> parkingSpaceVehicleTypes = parkingSpaceVehicleTypeRepository.findAll();
        assertThat(parkingSpaceVehicleTypes).hasSize(databaseSizeBeforeDelete - 1);
    }
}
