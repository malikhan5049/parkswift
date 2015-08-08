package com.ews.parkswift.integration.web.rest;

import com.ews.parkswift.Application;
import com.ews.parkswift.domain.AvailabilityScheduleRepeatOn;
import com.ews.parkswift.repository.AvailabilityScheduleRepeatOnRepository;
import com.ews.parkswift.web.rest.AvailabilityScheduleRepeatOnResource;

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
 * Test class for the availabilityScheduleRepeatOnResource REST controller.
 *
 * @see AvailabilityScheduleRepeatOnResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class AvailabilityScheduleRepeatOnResourceTest {


    private static final Integer DEFAULT_DAY_OF_MONTH = 0;
    private static final Integer UPDATED_DAY_OF_MONTH = 1;
    private static final String DEFAULT_DATE_OF_WEEK = "SAMPLE_TEXT";
    private static final String UPDATED_DATE_OF_WEEK = "UPDATED_TEXT";

    @Inject
    private AvailabilityScheduleRepeatOnRepository availabilityScheduleRepeatOnRepository;

    private MockMvc restavailabilityScheduleRepeatOnMockMvc;

    private AvailabilityScheduleRepeatOn availabilityScheduleRepeatOn;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        AvailabilityScheduleRepeatOnResource availabilityScheduleRepeatOnResource = new AvailabilityScheduleRepeatOnResource();
        ReflectionTestUtils.setField(availabilityScheduleRepeatOnResource, "availabilityScheduleRepeatOnRepository", availabilityScheduleRepeatOnRepository);
        this.restavailabilityScheduleRepeatOnMockMvc = MockMvcBuilders.standaloneSetup(availabilityScheduleRepeatOnResource).build();
    }

    @Before
    public void initTest() {
        availabilityScheduleRepeatOn = new AvailabilityScheduleRepeatOn();
        availabilityScheduleRepeatOn.setDayOfWeek(DEFAULT_DATE_OF_WEEK);
    }

    @Test
    @Transactional
    public void createavailabilityScheduleRepeatOn() throws Exception {
        int databaseSizeBeforeCreate = availabilityScheduleRepeatOnRepository.findAll().size();

        // Create the availabilityScheduleRepeatOn
        restavailabilityScheduleRepeatOnMockMvc.perform(post("/api/availabilityScheduleRepeatOns")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(availabilityScheduleRepeatOn)))
                .andExpect(status().isCreated());

        // Validate the availabilityScheduleRepeatOn in the database
        List<AvailabilityScheduleRepeatOn> availabilityScheduleRepeatOns = availabilityScheduleRepeatOnRepository.findAll();
        assertThat(availabilityScheduleRepeatOns).hasSize(databaseSizeBeforeCreate + 1);
        AvailabilityScheduleRepeatOn testavailabilityScheduleRepeatOn = availabilityScheduleRepeatOns.get(availabilityScheduleRepeatOns.size() - 1);
        assertThat(testavailabilityScheduleRepeatOn.getDayOfWeek()).isEqualTo(DEFAULT_DATE_OF_WEEK);
    }

    @Test
    @Transactional
    public void getAllavailabilityScheduleRepeatOns() throws Exception {
        // Initialize the database
        availabilityScheduleRepeatOnRepository.saveAndFlush(availabilityScheduleRepeatOn);

        // Get all the availabilityScheduleRepeatOns
        restavailabilityScheduleRepeatOnMockMvc.perform(get("/api/availabilityScheduleRepeatOns"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(availabilityScheduleRepeatOn.getId().intValue())))
                .andExpect(jsonPath("$.[*].dayOfMonth").value(hasItem(DEFAULT_DAY_OF_MONTH)))
                .andExpect(jsonPath("$.[*].dateOfWeek").value(hasItem(DEFAULT_DATE_OF_WEEK.toString())));
    }

    @Test
    @Transactional
    public void getavailabilityScheduleRepeatOn() throws Exception {
        // Initialize the database
        availabilityScheduleRepeatOnRepository.saveAndFlush(availabilityScheduleRepeatOn);

        // Get the availabilityScheduleRepeatOn
        restavailabilityScheduleRepeatOnMockMvc.perform(get("/api/availabilityScheduleRepeatOns/{id}", availabilityScheduleRepeatOn.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(availabilityScheduleRepeatOn.getId().intValue()))
            .andExpect(jsonPath("$.dayOfMonth").value(DEFAULT_DAY_OF_MONTH))
            .andExpect(jsonPath("$.dateOfWeek").value(DEFAULT_DATE_OF_WEEK.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingavailabilityScheduleRepeatOn() throws Exception {
        // Get the availabilityScheduleRepeatOn
        restavailabilityScheduleRepeatOnMockMvc.perform(get("/api/availabilityScheduleRepeatOns/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateavailabilityScheduleRepeatOn() throws Exception {
        // Initialize the database
        availabilityScheduleRepeatOnRepository.saveAndFlush(availabilityScheduleRepeatOn);

		int databaseSizeBeforeUpdate = availabilityScheduleRepeatOnRepository.findAll().size();

        // Update the availabilityScheduleRepeatOn
        availabilityScheduleRepeatOn.setDayOfWeek(UPDATED_DATE_OF_WEEK);
        restavailabilityScheduleRepeatOnMockMvc.perform(put("/api/availabilityScheduleRepeatOns")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(availabilityScheduleRepeatOn)))
                .andExpect(status().isOk());

        // Validate the availabilityScheduleRepeatOn in the database
        List<AvailabilityScheduleRepeatOn> availabilityScheduleRepeatOns = availabilityScheduleRepeatOnRepository.findAll();
        assertThat(availabilityScheduleRepeatOns).hasSize(databaseSizeBeforeUpdate);
        AvailabilityScheduleRepeatOn testavailabilityScheduleRepeatOn = availabilityScheduleRepeatOns.get(availabilityScheduleRepeatOns.size() - 1);
        assertThat(testavailabilityScheduleRepeatOn.getDayOfWeek()).isEqualTo(UPDATED_DATE_OF_WEEK);
    }

    @Test
    @Transactional
    public void deleteavailabilityScheduleRepeatOn() throws Exception {
        // Initialize the database
        availabilityScheduleRepeatOnRepository.saveAndFlush(availabilityScheduleRepeatOn);

		int databaseSizeBeforeDelete = availabilityScheduleRepeatOnRepository.findAll().size();

        // Get the availabilityScheduleRepeatOn
        restavailabilityScheduleRepeatOnMockMvc.perform(delete("/api/availabilityScheduleRepeatOns/{id}", availabilityScheduleRepeatOn.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<AvailabilityScheduleRepeatOn> availabilityScheduleRepeatOns = availabilityScheduleRepeatOnRepository.findAll();
        assertThat(availabilityScheduleRepeatOns).hasSize(databaseSizeBeforeDelete - 1);
    }
}
