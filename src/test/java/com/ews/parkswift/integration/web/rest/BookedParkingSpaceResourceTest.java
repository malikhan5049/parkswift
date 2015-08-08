package com.ews.parkswift.integration.web.rest;

import com.ews.parkswift.Application;
import com.ews.parkswift.domain.BookedParkingSpace;
import com.ews.parkswift.repository.BookedParkingSpaceRepository;
import com.ews.parkswift.web.rest.BookedParkingSpaceResource;

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
 * Test class for the BookedParkingSpaceResource REST controller.
 *
 * @see BookedParkingSpaceResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class BookedParkingSpaceResourceTest {


    @Inject
    private BookedParkingSpaceRepository bookedParkingSpaceRepository;

    private MockMvc restBookedParkingSpaceMockMvc;

    private BookedParkingSpace bookedParkingSpace;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        BookedParkingSpaceResource bookedParkingSpaceResource = new BookedParkingSpaceResource();
        ReflectionTestUtils.setField(bookedParkingSpaceResource, "bookedParkingSpaceRepository", bookedParkingSpaceRepository);
        this.restBookedParkingSpaceMockMvc = MockMvcBuilders.standaloneSetup(bookedParkingSpaceResource).build();
    }

    @Before
    public void initTest() {
        bookedParkingSpace = new BookedParkingSpace();
    }

    @Test
    @Transactional
    public void createBookedParkingSpace() throws Exception {
        int databaseSizeBeforeCreate = bookedParkingSpaceRepository.findAll().size();

        // Create the BookedParkingSpace
        restBookedParkingSpaceMockMvc.perform(post("/api/bookedParkingSpaces")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(bookedParkingSpace)))
                .andExpect(status().isCreated());

        // Validate the BookedParkingSpace in the database
        List<BookedParkingSpace> bookedParkingSpaces = bookedParkingSpaceRepository.findAll();
        assertThat(bookedParkingSpaces).hasSize(databaseSizeBeforeCreate + 1);
        BookedParkingSpace testBookedParkingSpace = bookedParkingSpaces.get(bookedParkingSpaces.size() - 1);
    }

    @Test
    @Transactional
    public void getAllBookedParkingSpaces() throws Exception {
        // Initialize the database
        bookedParkingSpaceRepository.saveAndFlush(bookedParkingSpace);

        // Get all the bookedParkingSpaces
        restBookedParkingSpaceMockMvc.perform(get("/api/bookedParkingSpaces"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(bookedParkingSpace.getId().intValue())));
    }

    @Test
    @Transactional
    public void getBookedParkingSpace() throws Exception {
        // Initialize the database
        bookedParkingSpaceRepository.saveAndFlush(bookedParkingSpace);

        // Get the bookedParkingSpace
        restBookedParkingSpaceMockMvc.perform(get("/api/bookedParkingSpaces/{id}", bookedParkingSpace.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(bookedParkingSpace.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingBookedParkingSpace() throws Exception {
        // Get the bookedParkingSpace
        restBookedParkingSpaceMockMvc.perform(get("/api/bookedParkingSpaces/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBookedParkingSpace() throws Exception {
        // Initialize the database
        bookedParkingSpaceRepository.saveAndFlush(bookedParkingSpace);

		int databaseSizeBeforeUpdate = bookedParkingSpaceRepository.findAll().size();

        // Update the bookedParkingSpace
        restBookedParkingSpaceMockMvc.perform(put("/api/bookedParkingSpaces")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(bookedParkingSpace)))
                .andExpect(status().isOk());

        // Validate the BookedParkingSpace in the database
        List<BookedParkingSpace> bookedParkingSpaces = bookedParkingSpaceRepository.findAll();
        assertThat(bookedParkingSpaces).hasSize(databaseSizeBeforeUpdate);
        BookedParkingSpace testBookedParkingSpace = bookedParkingSpaces.get(bookedParkingSpaces.size() - 1);
    }

    @Test
    @Transactional
    public void deleteBookedParkingSpace() throws Exception {
        // Initialize the database
        bookedParkingSpaceRepository.saveAndFlush(bookedParkingSpace);

		int databaseSizeBeforeDelete = bookedParkingSpaceRepository.findAll().size();

        // Get the bookedParkingSpace
        restBookedParkingSpaceMockMvc.perform(delete("/api/bookedParkingSpaces/{id}", bookedParkingSpace.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<BookedParkingSpace> bookedParkingSpaces = bookedParkingSpaceRepository.findAll();
        assertThat(bookedParkingSpaces).hasSize(databaseSizeBeforeDelete - 1);
    }
}
