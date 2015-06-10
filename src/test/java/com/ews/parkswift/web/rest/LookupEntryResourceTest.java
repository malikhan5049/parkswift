package com.ews.parkswift.web.rest;

import com.ews.parkswift.Application;
import com.ews.parkswift.domain.LookupEntry;
import com.ews.parkswift.repository.LookupEntryRepository;

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
 * Test class for the LookupEntryResource REST controller.
 *
 * @see LookupEntryResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class LookupEntryResourceTest {

    private static final String DEFAULT_VALUE = "SAMPLE_TEXT";
    private static final String UPDATED_VALUE = "UPDATED_TEXT";
    private static final String DEFAULT_DESCRIPTION = "SAMPLE_TEXT";
    private static final String UPDATED_DESCRIPTION = "UPDATED_TEXT";

    @Inject
    private LookupEntryRepository lookupEntryRepository;

    private MockMvc restLookupEntryMockMvc;

    private LookupEntry lookupEntry;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        LookupEntryResource lookupEntryResource = new LookupEntryResource();
        ReflectionTestUtils.setField(lookupEntryResource, "lookupEntryRepository", lookupEntryRepository);
        this.restLookupEntryMockMvc = MockMvcBuilders.standaloneSetup(lookupEntryResource).build();
    }

    @Before
    public void initTest() {
        lookupEntry = new LookupEntry();
        lookupEntry.setValue(DEFAULT_VALUE);
        lookupEntry.setDescription(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void createLookupEntry() throws Exception {
        int databaseSizeBeforeCreate = lookupEntryRepository.findAll().size();

        // Create the LookupEntry
        restLookupEntryMockMvc.perform(post("/api/lookupEntrys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(lookupEntry)))
                .andExpect(status().isCreated());

        // Validate the LookupEntry in the database
        List<LookupEntry> lookupEntrys = lookupEntryRepository.findAll();
        assertThat(lookupEntrys).hasSize(databaseSizeBeforeCreate + 1);
        LookupEntry testLookupEntry = lookupEntrys.get(lookupEntrys.size() - 1);
        assertThat(testLookupEntry.getValue()).isEqualTo(DEFAULT_VALUE);
        assertThat(testLookupEntry.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void checkValueIsRequired() throws Exception {
        // Validate the database is empty
        assertThat(lookupEntryRepository.findAll()).hasSize(0);
        // set the field null
        lookupEntry.setValue(null);

        // Create the LookupEntry, which fails.
        restLookupEntryMockMvc.perform(post("/api/lookupEntrys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(lookupEntry)))
                .andExpect(status().isBadRequest());

        // Validate the database is still empty
        List<LookupEntry> lookupEntrys = lookupEntryRepository.findAll();
        assertThat(lookupEntrys).hasSize(0);
    }

    @Test
    @Transactional
    public void getAllLookupEntrys() throws Exception {
        // Initialize the database
        lookupEntryRepository.saveAndFlush(lookupEntry);

        // Get all the lookupEntrys
        restLookupEntryMockMvc.perform(get("/api/lookupEntrys"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(lookupEntry.getId().intValue())))
                .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void getLookupEntry() throws Exception {
        // Initialize the database
        lookupEntryRepository.saveAndFlush(lookupEntry);

        // Get the lookupEntry
        restLookupEntryMockMvc.perform(get("/api/lookupEntrys/{id}", lookupEntry.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(lookupEntry.getId().intValue()))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingLookupEntry() throws Exception {
        // Get the lookupEntry
        restLookupEntryMockMvc.perform(get("/api/lookupEntrys/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLookupEntry() throws Exception {
        // Initialize the database
        lookupEntryRepository.saveAndFlush(lookupEntry);

		int databaseSizeBeforeUpdate = lookupEntryRepository.findAll().size();

        // Update the lookupEntry
        lookupEntry.setValue(UPDATED_VALUE);
        lookupEntry.setDescription(UPDATED_DESCRIPTION);
        restLookupEntryMockMvc.perform(put("/api/lookupEntrys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(lookupEntry)))
                .andExpect(status().isOk());

        // Validate the LookupEntry in the database
        List<LookupEntry> lookupEntrys = lookupEntryRepository.findAll();
        assertThat(lookupEntrys).hasSize(databaseSizeBeforeUpdate);
        LookupEntry testLookupEntry = lookupEntrys.get(lookupEntrys.size() - 1);
        assertThat(testLookupEntry.getValue()).isEqualTo(UPDATED_VALUE);
        assertThat(testLookupEntry.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void deleteLookupEntry() throws Exception {
        // Initialize the database
        lookupEntryRepository.saveAndFlush(lookupEntry);

		int databaseSizeBeforeDelete = lookupEntryRepository.findAll().size();

        // Get the lookupEntry
        restLookupEntryMockMvc.perform(delete("/api/lookupEntrys/{id}", lookupEntry.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<LookupEntry> lookupEntrys = lookupEntryRepository.findAll();
        assertThat(lookupEntrys).hasSize(databaseSizeBeforeDelete - 1);
    }
}
