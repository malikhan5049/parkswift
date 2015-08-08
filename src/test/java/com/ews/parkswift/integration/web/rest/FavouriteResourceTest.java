package com.ews.parkswift.integration.web.rest;

import com.ews.parkswift.Application;
import com.ews.parkswift.domain.Favourite;
import com.ews.parkswift.repository.FavouriteRepository;
import com.ews.parkswift.web.rest.FavouriteResource;

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
 * Test class for the FavouriteResource REST controller.
 *
 * @see FavouriteResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class FavouriteResourceTest {


    @Inject
    private FavouriteRepository favouriteRepository;

    private MockMvc restFavouriteMockMvc;

    private Favourite favourite;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        FavouriteResource favouriteResource = new FavouriteResource();
        ReflectionTestUtils.setField(favouriteResource, "favouriteRepository", favouriteRepository);
        this.restFavouriteMockMvc = MockMvcBuilders.standaloneSetup(favouriteResource).build();
    }

    @Before
    public void initTest() {
        favourite = new Favourite();
    }

    @Test
    @Transactional
    public void createFavourite() throws Exception {
        int databaseSizeBeforeCreate = favouriteRepository.findAll().size();

        // Create the Favourite
        restFavouriteMockMvc.perform(post("/api/favourites")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(favourite)))
                .andExpect(status().isCreated());

        // Validate the Favourite in the database
        List<Favourite> favourites = favouriteRepository.findAll();
        assertThat(favourites).hasSize(databaseSizeBeforeCreate + 1);
        Favourite testFavourite = favourites.get(favourites.size() - 1);
    }

    @Test
    @Transactional
    public void getAllFavourites() throws Exception {
        // Initialize the database
        favouriteRepository.saveAndFlush(favourite);

        // Get all the favourites
        restFavouriteMockMvc.perform(get("/api/favourites"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(favourite.getId().intValue())));
    }

    @Test
    @Transactional
    public void getFavourite() throws Exception {
        // Initialize the database
        favouriteRepository.saveAndFlush(favourite);

        // Get the favourite
        restFavouriteMockMvc.perform(get("/api/favourites/{id}", favourite.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(favourite.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingFavourite() throws Exception {
        // Get the favourite
        restFavouriteMockMvc.perform(get("/api/favourites/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFavourite() throws Exception {
        // Initialize the database
        favouriteRepository.saveAndFlush(favourite);

		int databaseSizeBeforeUpdate = favouriteRepository.findAll().size();

        // Update the favourite
        restFavouriteMockMvc.perform(put("/api/favourites")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(favourite)))
                .andExpect(status().isOk());

        // Validate the Favourite in the database
        List<Favourite> favourites = favouriteRepository.findAll();
        assertThat(favourites).hasSize(databaseSizeBeforeUpdate);
        Favourite testFavourite = favourites.get(favourites.size() - 1);
    }

    @Test
    @Transactional
    public void deleteFavourite() throws Exception {
        // Initialize the database
        favouriteRepository.saveAndFlush(favourite);

		int databaseSizeBeforeDelete = favouriteRepository.findAll().size();

        // Get the favourite
        restFavouriteMockMvc.perform(delete("/api/favourites/{id}", favourite.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Favourite> favourites = favouriteRepository.findAll();
        assertThat(favourites).hasSize(databaseSizeBeforeDelete - 1);
    }
}
