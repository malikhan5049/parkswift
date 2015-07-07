package com.ews.parkswift.web.rest;

import com.ews.parkswift.Application;
import com.ews.parkswift.domain.AvailableParkingRepeatOn;
import com.ews.parkswift.repository.AvailableParkingRepeatOnRepository;

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
 * Test class for the AvailableParkingRepeatOnResource REST controller.
 *
 * @see AvailableParkingRepeatOnResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class AvailableParkingRepeatOnResourceTest {


    private static final Integer DEFAULT_DAY_OF_MONTH = 0;
    private static final Integer UPDATED_DAY_OF_MONTH = 1;
    private static final String DEFAULT_DATE_OF_WEEK = "SAMPLE_TEXT";
    private static final String UPDATED_DATE_OF_WEEK = "UPDATED_TEXT";

    @Inject
    private AvailableParkingRepeatOnRepository availableParkingRepeatOnRepository;

    private MockMvc restAvailableParkingRepeatOnMockMvc;

    private AvailableParkingRepeatOn availableParkingRepeatOn;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        AvailableParkingRepeatOnResource availableParkingRepeatOnResource = new AvailableParkingRepeatOnResource();
        ReflectionTestUtils.setField(availableParkingRepeatOnResource, "availableParkingRepeatOnRepository", availableParkingRepeatOnRepository);
        this.restAvailableParkingRepeatOnMockMvc = MockMvcBuilders.standaloneSetup(availableParkingRepeatOnResource).build();
    }

    @Before
    public void initTest() {
        availableParkingRepeatOn = new AvailableParkingRepeatOn();
        availableParkingRepeatOn.setDayOfMonth(DEFAULT_DAY_OF_MONTH);
        availableParkingRepeatOn.setDayOfWeek(DEFAULT_DATE_OF_WEEK);
    }

    @Test
    @Transactional
    public void createAvailableParkingRepeatOn() throws Exception {
        int databaseSizeBeforeCreate = availableParkingRepeatOnRepository.findAll().size();

        // Create the AvailableParkingRepeatOn
        restAvailableParkingRepeatOnMockMvc.perform(post("/api/availableParkingRepeatOns")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(availableParkingRepeatOn)))
                .andExpect(status().isCreated());

        // Validate the AvailableParkingRepeatOn in the database
        List<AvailableParkingRepeatOn> availableParkingRepeatOns = availableParkingRepeatOnRepository.findAll();
        assertThat(availableParkingRepeatOns).hasSize(databaseSizeBeforeCreate + 1);
        AvailableParkingRepeatOn testAvailableParkingRepeatOn = availableParkingRepeatOns.get(availableParkingRepeatOns.size() - 1);
        assertThat(testAvailableParkingRepeatOn.getDayOfMonth()).isEqualTo(DEFAULT_DAY_OF_MONTH);
        assertThat(testAvailableParkingRepeatOn.getDayOfWeek()).isEqualTo(DEFAULT_DATE_OF_WEEK);
    }

    @Test
    @Transactional
    public void getAllAvailableParkingRepeatOns() throws Exception {
        // Initialize the database
        availableParkingRepeatOnRepository.saveAndFlush(availableParkingRepeatOn);

        // Get all the availableParkingRepeatOns
        restAvailableParkingRepeatOnMockMvc.perform(get("/api/availableParkingRepeatOns"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(availableParkingRepeatOn.getId().intValue())))
                .andExpect(jsonPath("$.[*].dayOfMonth").value(hasItem(DEFAULT_DAY_OF_MONTH)))
                .andExpect(jsonPath("$.[*].dateOfWeek").value(hasItem(DEFAULT_DATE_OF_WEEK.toString())));
    }

    @Test
    @Transactional
    public void getAvailableParkingRepeatOn() throws Exception {
        // Initialize the database
        availableParkingRepeatOnRepository.saveAndFlush(availableParkingRepeatOn);

        // Get the availableParkingRepeatOn
        restAvailableParkingRepeatOnMockMvc.perform(get("/api/availableParkingRepeatOns/{id}", availableParkingRepeatOn.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(availableParkingRepeatOn.getId().intValue()))
            .andExpect(jsonPath("$.dayOfMonth").value(DEFAULT_DAY_OF_MONTH))
            .andExpect(jsonPath("$.dateOfWeek").value(DEFAULT_DATE_OF_WEEK.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingAvailableParkingRepeatOn() throws Exception {
        // Get the availableParkingRepeatOn
        restAvailableParkingRepeatOnMockMvc.perform(get("/api/availableParkingRepeatOns/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAvailableParkingRepeatOn() throws Exception {
        // Initialize the database
        availableParkingRepeatOnRepository.saveAndFlush(availableParkingRepeatOn);

		int databaseSizeBeforeUpdate = availableParkingRepeatOnRepository.findAll().size();

        // Update the availableParkingRepeatOn
        availableParkingRepeatOn.setDayOfMonth(UPDATED_DAY_OF_MONTH);
        availableParkingRepeatOn.setDayOfWeek(UPDATED_DATE_OF_WEEK);
        restAvailableParkingRepeatOnMockMvc.perform(put("/api/availableParkingRepeatOns")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(availableParkingRepeatOn)))
                .andExpect(status().isOk());

        // Validate the AvailableParkingRepeatOn in the database
        List<AvailableParkingRepeatOn> availableParkingRepeatOns = availableParkingRepeatOnRepository.findAll();
        assertThat(availableParkingRepeatOns).hasSize(databaseSizeBeforeUpdate);
        AvailableParkingRepeatOn testAvailableParkingRepeatOn = availableParkingRepeatOns.get(availableParkingRepeatOns.size() - 1);
        assertThat(testAvailableParkingRepeatOn.getDayOfMonth()).isEqualTo(UPDATED_DAY_OF_MONTH);
        assertThat(testAvailableParkingRepeatOn.getDayOfWeek()).isEqualTo(UPDATED_DATE_OF_WEEK);
    }

    @Test
    @Transactional
    public void deleteAvailableParkingRepeatOn() throws Exception {
        // Initialize the database
        availableParkingRepeatOnRepository.saveAndFlush(availableParkingRepeatOn);

		int databaseSizeBeforeDelete = availableParkingRepeatOnRepository.findAll().size();

        // Get the availableParkingRepeatOn
        restAvailableParkingRepeatOnMockMvc.perform(delete("/api/availableParkingRepeatOns/{id}", availableParkingRepeatOn.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<AvailableParkingRepeatOn> availableParkingRepeatOns = availableParkingRepeatOnRepository.findAll();
        assertThat(availableParkingRepeatOns).hasSize(databaseSizeBeforeDelete - 1);
    }
}
