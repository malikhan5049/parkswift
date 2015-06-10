package com.ews.parkswift.web.rest;

import com.ews.parkswift.Application;
import com.ews.parkswift.domain.LookupHeader;
import com.ews.parkswift.repository.LookupHeaderRepository;

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
 * Test class for the LookupHeaderResource REST controller.
 *
 * @see LookupHeaderResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class LookupHeaderResourceTest {

    private static final String DEFAULT_CODE = "SAMPLE_TEXT";
    private static final String UPDATED_CODE = "UPDATED_TEXT";
    private static final String DEFAULT_DESCRIPTION = "SAMPLE_TEXT";
    private static final String UPDATED_DESCRIPTION = "UPDATED_TEXT";

    @Inject
    private LookupHeaderRepository lookupHeaderRepository;

    private MockMvc restLookupHeaderMockMvc;

    private LookupHeader lookupHeader;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        LookupHeaderResource lookupHeaderResource = new LookupHeaderResource();
        ReflectionTestUtils.setField(lookupHeaderResource, "lookupHeaderRepository", lookupHeaderRepository);
        this.restLookupHeaderMockMvc = MockMvcBuilders.standaloneSetup(lookupHeaderResource).build();
    }

    @Before
    public void initTest() {
        lookupHeader = new LookupHeader();
        lookupHeader.setCode(DEFAULT_CODE);
        lookupHeader.setDescription(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void createLookupHeader() throws Exception {
        int databaseSizeBeforeCreate = lookupHeaderRepository.findAll().size();

        // Create the LookupHeader
        restLookupHeaderMockMvc.perform(post("/api/lookupHeaders")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(lookupHeader)))
                .andExpect(status().isCreated());

        // Validate the LookupHeader in the database
        List<LookupHeader> lookupHeaders = lookupHeaderRepository.findAll();
        assertThat(lookupHeaders).hasSize(databaseSizeBeforeCreate + 1);
        LookupHeader testLookupHeader = lookupHeaders.get(lookupHeaders.size() - 1);
        assertThat(testLookupHeader.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testLookupHeader.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void checkCodeIsRequired() throws Exception {
        // Validate the database is empty
        assertThat(lookupHeaderRepository.findAll()).hasSize(0);
        // set the field null
        lookupHeader.setCode(null);

        // Create the LookupHeader, which fails.
        restLookupHeaderMockMvc.perform(post("/api/lookupHeaders")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(lookupHeader)))
                .andExpect(status().isBadRequest());

        // Validate the database is still empty
        List<LookupHeader> lookupHeaders = lookupHeaderRepository.findAll();
        assertThat(lookupHeaders).hasSize(0);
    }

    @Test
    @Transactional
    public void getAllLookupHeaders() throws Exception {
        // Initialize the database
        lookupHeaderRepository.saveAndFlush(lookupHeader);

        // Get all the lookupHeaders
        restLookupHeaderMockMvc.perform(get("/api/lookupHeaders"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(lookupHeader.getId().intValue())))
                .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void getLookupHeader() throws Exception {
        // Initialize the database
        lookupHeaderRepository.saveAndFlush(lookupHeader);

        // Get the lookupHeader
        restLookupHeaderMockMvc.perform(get("/api/lookupHeaders/{id}", lookupHeader.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(lookupHeader.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingLookupHeader() throws Exception {
        // Get the lookupHeader
        restLookupHeaderMockMvc.perform(get("/api/lookupHeaders/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLookupHeader() throws Exception {
        // Initialize the database
        lookupHeaderRepository.saveAndFlush(lookupHeader);

		int databaseSizeBeforeUpdate = lookupHeaderRepository.findAll().size();

        // Update the lookupHeader
        lookupHeader.setCode(UPDATED_CODE);
        lookupHeader.setDescription(UPDATED_DESCRIPTION);
        restLookupHeaderMockMvc.perform(put("/api/lookupHeaders")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(lookupHeader)))
                .andExpect(status().isOk());

        // Validate the LookupHeader in the database
        List<LookupHeader> lookupHeaders = lookupHeaderRepository.findAll();
        assertThat(lookupHeaders).hasSize(databaseSizeBeforeUpdate);
        LookupHeader testLookupHeader = lookupHeaders.get(lookupHeaders.size() - 1);
        assertThat(testLookupHeader.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testLookupHeader.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void deleteLookupHeader() throws Exception {
        // Initialize the database
        lookupHeaderRepository.saveAndFlush(lookupHeader);

		int databaseSizeBeforeDelete = lookupHeaderRepository.findAll().size();

        // Get the lookupHeader
        restLookupHeaderMockMvc.perform(delete("/api/lookupHeaders/{id}", lookupHeader.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<LookupHeader> lookupHeaders = lookupHeaderRepository.findAll();
        assertThat(lookupHeaders).hasSize(databaseSizeBeforeDelete - 1);
    }
}
