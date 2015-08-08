package com.ews.parkswift.integration.web.rest;

import com.ews.parkswift.Application;
import com.ews.parkswift.domain.ParkingSpacePriceEntry;
import com.ews.parkswift.repository.ParkingSpacePriceEntryRepository;
import com.ews.parkswift.web.rest.ParkingSpacePriceEntryResource;

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

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ParkingSpacePriceEntryResource REST controller.
 *
 * @see ParkingSpacePriceEntryResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class ParkingSpacePriceEntryResourceTest {

    private static final String DEFAULT_TYPE = "SAMPLE_TEXT";
    private static final String UPDATED_TYPE = "UPDATED_TEXT";

    private static final BigDecimal DEFAULT_PRICE = new BigDecimal(0);
    private static final BigDecimal UPDATED_PRICE = new BigDecimal(1);

    @Inject
    private ParkingSpacePriceEntryRepository parkingSpacePriceEntryRepository;

    private MockMvc restParkingSpacePriceEntryMockMvc;

    private ParkingSpacePriceEntry parkingSpacePriceEntry;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ParkingSpacePriceEntryResource parkingSpacePriceEntryResource = new ParkingSpacePriceEntryResource();
        ReflectionTestUtils.setField(parkingSpacePriceEntryResource, "parkingSpacePriceEntryRepository", parkingSpacePriceEntryRepository);
        this.restParkingSpacePriceEntryMockMvc = MockMvcBuilders.standaloneSetup(parkingSpacePriceEntryResource).build();
    }

    @Before
    public void initTest() {
        parkingSpacePriceEntry = new ParkingSpacePriceEntry();
        parkingSpacePriceEntry.setType(DEFAULT_TYPE);
        parkingSpacePriceEntry.setPrice(DEFAULT_PRICE);
    }

    @Test
    @Transactional
    public void createParkingSpacePriceEntry() throws Exception {
        int databaseSizeBeforeCreate = parkingSpacePriceEntryRepository.findAll().size();

        // Create the ParkingSpacePriceEntry
        restParkingSpacePriceEntryMockMvc.perform(post("/api/parkingSpacePriceEntrys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(parkingSpacePriceEntry)))
                .andExpect(status().isCreated());

        // Validate the ParkingSpacePriceEntry in the database
        List<ParkingSpacePriceEntry> parkingSpacePriceEntrys = parkingSpacePriceEntryRepository.findAll();
        assertThat(parkingSpacePriceEntrys).hasSize(databaseSizeBeforeCreate + 1);
        ParkingSpacePriceEntry testParkingSpacePriceEntry = parkingSpacePriceEntrys.get(parkingSpacePriceEntrys.size() - 1);
        assertThat(testParkingSpacePriceEntry.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testParkingSpacePriceEntry.getPrice()).isEqualTo(DEFAULT_PRICE);
    }

    @Test
    @Transactional
    public void checkTypeIsRequired() throws Exception {
        // Validate the database is empty
        assertThat(parkingSpacePriceEntryRepository.findAll()).hasSize(0);
        // set the field null
        parkingSpacePriceEntry.setType(null);

        // Create the ParkingSpacePriceEntry, which fails.
        restParkingSpacePriceEntryMockMvc.perform(post("/api/parkingSpacePriceEntrys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(parkingSpacePriceEntry)))
                .andExpect(status().isBadRequest());

        // Validate the database is still empty
        List<ParkingSpacePriceEntry> parkingSpacePriceEntrys = parkingSpacePriceEntryRepository.findAll();
        assertThat(parkingSpacePriceEntrys).hasSize(0);
    }

    @Test
    @Transactional
    public void checkPriceIsRequired() throws Exception {
        // Validate the database is empty
        assertThat(parkingSpacePriceEntryRepository.findAll()).hasSize(0);
        // set the field null
        parkingSpacePriceEntry.setPrice(null);

        // Create the ParkingSpacePriceEntry, which fails.
        restParkingSpacePriceEntryMockMvc.perform(post("/api/parkingSpacePriceEntrys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(parkingSpacePriceEntry)))
                .andExpect(status().isBadRequest());

        // Validate the database is still empty
        List<ParkingSpacePriceEntry> parkingSpacePriceEntrys = parkingSpacePriceEntryRepository.findAll();
        assertThat(parkingSpacePriceEntrys).hasSize(0);
    }

    @Test
    @Transactional
    public void getAllParkingSpacePriceEntrys() throws Exception {
        // Initialize the database
        parkingSpacePriceEntryRepository.saveAndFlush(parkingSpacePriceEntry);

        // Get all the parkingSpacePriceEntrys
        restParkingSpacePriceEntryMockMvc.perform(get("/api/parkingSpacePriceEntrys"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(parkingSpacePriceEntry.getId().intValue())))
                .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
                .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.intValue())));
    }

    @Test
    @Transactional
    public void getParkingSpacePriceEntry() throws Exception {
        // Initialize the database
        parkingSpacePriceEntryRepository.saveAndFlush(parkingSpacePriceEntry);

        // Get the parkingSpacePriceEntry
        restParkingSpacePriceEntryMockMvc.perform(get("/api/parkingSpacePriceEntrys/{id}", parkingSpacePriceEntry.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(parkingSpacePriceEntry.getId().intValue()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingParkingSpacePriceEntry() throws Exception {
        // Get the parkingSpacePriceEntry
        restParkingSpacePriceEntryMockMvc.perform(get("/api/parkingSpacePriceEntrys/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateParkingSpacePriceEntry() throws Exception {
        // Initialize the database
        parkingSpacePriceEntryRepository.saveAndFlush(parkingSpacePriceEntry);

		int databaseSizeBeforeUpdate = parkingSpacePriceEntryRepository.findAll().size();

        // Update the parkingSpacePriceEntry
        parkingSpacePriceEntry.setType(UPDATED_TYPE);
        parkingSpacePriceEntry.setPrice(UPDATED_PRICE);
        restParkingSpacePriceEntryMockMvc.perform(put("/api/parkingSpacePriceEntrys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(parkingSpacePriceEntry)))
                .andExpect(status().isOk());

        // Validate the ParkingSpacePriceEntry in the database
        List<ParkingSpacePriceEntry> parkingSpacePriceEntrys = parkingSpacePriceEntryRepository.findAll();
        assertThat(parkingSpacePriceEntrys).hasSize(databaseSizeBeforeUpdate);
        ParkingSpacePriceEntry testParkingSpacePriceEntry = parkingSpacePriceEntrys.get(parkingSpacePriceEntrys.size() - 1);
        assertThat(testParkingSpacePriceEntry.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testParkingSpacePriceEntry.getPrice()).isEqualTo(UPDATED_PRICE);
    }

    @Test
    @Transactional
    public void deleteParkingSpacePriceEntry() throws Exception {
        // Initialize the database
        parkingSpacePriceEntryRepository.saveAndFlush(parkingSpacePriceEntry);

		int databaseSizeBeforeDelete = parkingSpacePriceEntryRepository.findAll().size();

        // Get the parkingSpacePriceEntry
        restParkingSpacePriceEntryMockMvc.perform(delete("/api/parkingSpacePriceEntrys/{id}", parkingSpacePriceEntry.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<ParkingSpacePriceEntry> parkingSpacePriceEntrys = parkingSpacePriceEntryRepository.findAll();
        assertThat(parkingSpacePriceEntrys).hasSize(databaseSizeBeforeDelete - 1);
    }
}
