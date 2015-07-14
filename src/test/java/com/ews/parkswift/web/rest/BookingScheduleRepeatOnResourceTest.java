package com.ews.parkswift.web.rest;

import com.ews.parkswift.Application;
import com.ews.parkswift.domain.BookingScheduleRepeatOn;
import com.ews.parkswift.repository.BookingScheduleRepeatOnRepository;

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
 * Test class for the ReservedParkingRepeatOnResource REST controller.
 *
 * @see BookingScheduleRepeatOnResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class BookingScheduleRepeatOnResourceTest {


    private static final Integer DEFAULT_DATE_OF_MONTH = 0;
    private static final Integer UPDATED_DATE_OF_MONTH = 1;
    private static final String DEFAULT_DAY_OF_WEEK = "SAMPLE_TEXT";
    private static final String UPDATED_DAY_OF_WEEK = "UPDATED_TEXT";

    @Inject
    private BookingScheduleRepeatOnRepository reservedParkingRepeatOnRepository;

    private MockMvc restReservedParkingRepeatOnMockMvc;

    private BookingScheduleRepeatOn reservedParkingRepeatOn;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        BookingScheduleRepeatOnResource reservedParkingRepeatOnResource = new BookingScheduleRepeatOnResource();
        ReflectionTestUtils.setField(reservedParkingRepeatOnResource, "reservedParkingRepeatOnRepository", reservedParkingRepeatOnRepository);
        this.restReservedParkingRepeatOnMockMvc = MockMvcBuilders.standaloneSetup(reservedParkingRepeatOnResource).build();
    }

    @Before
    public void initTest() {
        reservedParkingRepeatOn = new BookingScheduleRepeatOn();
        reservedParkingRepeatOn.setDayOfWeek(DEFAULT_DAY_OF_WEEK);
    }

    @Test
    @Transactional
    public void createReservedParkingRepeatOn() throws Exception {
        int databaseSizeBeforeCreate = reservedParkingRepeatOnRepository.findAll().size();

        // Create the ReservedParkingRepeatOn
        restReservedParkingRepeatOnMockMvc.perform(post("/api/reservedParkingRepeatOns")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(reservedParkingRepeatOn)))
                .andExpect(status().isCreated());

        // Validate the ReservedParkingRepeatOn in the database
        List<BookingScheduleRepeatOn> reservedParkingRepeatOns = reservedParkingRepeatOnRepository.findAll();
        assertThat(reservedParkingRepeatOns).hasSize(databaseSizeBeforeCreate + 1);
        BookingScheduleRepeatOn testReservedParkingRepeatOn = reservedParkingRepeatOns.get(reservedParkingRepeatOns.size() - 1);
        assertThat(testReservedParkingRepeatOn.getDayOfWeek()).isEqualTo(DEFAULT_DAY_OF_WEEK);
    }

    @Test
    @Transactional
    public void getAllReservedParkingRepeatOns() throws Exception {
        // Initialize the database
        reservedParkingRepeatOnRepository.saveAndFlush(reservedParkingRepeatOn);

        // Get all the reservedParkingRepeatOns
        restReservedParkingRepeatOnMockMvc.perform(get("/api/reservedParkingRepeatOns"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(reservedParkingRepeatOn.getId().intValue())))
                .andExpect(jsonPath("$.[*].dateOfMonth").value(hasItem(DEFAULT_DATE_OF_MONTH)))
                .andExpect(jsonPath("$.[*].dayOfWeek").value(hasItem(DEFAULT_DAY_OF_WEEK.toString())));
    }

    @Test
    @Transactional
    public void getReservedParkingRepeatOn() throws Exception {
        // Initialize the database
        reservedParkingRepeatOnRepository.saveAndFlush(reservedParkingRepeatOn);

        // Get the reservedParkingRepeatOn
        restReservedParkingRepeatOnMockMvc.perform(get("/api/reservedParkingRepeatOns/{id}", reservedParkingRepeatOn.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(reservedParkingRepeatOn.getId().intValue()))
            .andExpect(jsonPath("$.dateOfMonth").value(DEFAULT_DATE_OF_MONTH))
            .andExpect(jsonPath("$.dayOfWeek").value(DEFAULT_DAY_OF_WEEK.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingReservedParkingRepeatOn() throws Exception {
        // Get the reservedParkingRepeatOn
        restReservedParkingRepeatOnMockMvc.perform(get("/api/reservedParkingRepeatOns/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateReservedParkingRepeatOn() throws Exception {
        // Initialize the database
        reservedParkingRepeatOnRepository.saveAndFlush(reservedParkingRepeatOn);

		int databaseSizeBeforeUpdate = reservedParkingRepeatOnRepository.findAll().size();

        // Update the reservedParkingRepeatOn
        reservedParkingRepeatOn.setDayOfWeek(UPDATED_DAY_OF_WEEK);
        restReservedParkingRepeatOnMockMvc.perform(put("/api/reservedParkingRepeatOns")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(reservedParkingRepeatOn)))
                .andExpect(status().isOk());

        // Validate the ReservedParkingRepeatOn in the database
        List<BookingScheduleRepeatOn> reservedParkingRepeatOns = reservedParkingRepeatOnRepository.findAll();
        assertThat(reservedParkingRepeatOns).hasSize(databaseSizeBeforeUpdate);
        BookingScheduleRepeatOn testReservedParkingRepeatOn = reservedParkingRepeatOns.get(reservedParkingRepeatOns.size() - 1);
        assertThat(testReservedParkingRepeatOn.getDayOfWeek()).isEqualTo(UPDATED_DAY_OF_WEEK);
    }

    @Test
    @Transactional
    public void deleteReservedParkingRepeatOn() throws Exception {
        // Initialize the database
        reservedParkingRepeatOnRepository.saveAndFlush(reservedParkingRepeatOn);

		int databaseSizeBeforeDelete = reservedParkingRepeatOnRepository.findAll().size();

        // Get the reservedParkingRepeatOn
        restReservedParkingRepeatOnMockMvc.perform(delete("/api/reservedParkingRepeatOns/{id}", reservedParkingRepeatOn.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<BookingScheduleRepeatOn> reservedParkingRepeatOns = reservedParkingRepeatOnRepository.findAll();
        assertThat(reservedParkingRepeatOns).hasSize(databaseSizeBeforeDelete - 1);
    }
}
