package com.ews.parkswift.integration.web.rest;

import com.ews.parkswift.Application;
import com.ews.parkswift.domain.PaypallAccount;
import com.ews.parkswift.repository.PaypallAccountRepository;
import com.ews.parkswift.web.rest.PaypallAccountResource;

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
 * Test class for the PaypallAccountResource REST controller.
 *
 * @see PaypallAccountResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class PaypallAccountResourceTest {

    private static final String DEFAULT_EMAIL = "SAMPLE_TEXT";
    private static final String UPDATED_EMAIL = "UPDATED_TEXT";

    private static final Boolean DEFAULT_IS_DEFAULT = false;
    private static final Boolean UPDATED_IS_DEFAULT = true;

    @Inject
    private PaypallAccountRepository paypallAccountRepository;

    private MockMvc restPaypallAccountMockMvc;

    private PaypallAccount paypallAccount;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PaypallAccountResource paypallAccountResource = new PaypallAccountResource();
        ReflectionTestUtils.setField(paypallAccountResource, "paypallAccountRepository", paypallAccountRepository);
        this.restPaypallAccountMockMvc = MockMvcBuilders.standaloneSetup(paypallAccountResource).build();
    }

    @Before
    public void initTest() {
        paypallAccount = new PaypallAccount();
        paypallAccount.setEmail(DEFAULT_EMAIL);
        paypallAccount.setIsDefault(DEFAULT_IS_DEFAULT);
    }

    @Test
    @Transactional
    public void createPaypallAccount() throws Exception {
        int databaseSizeBeforeCreate = paypallAccountRepository.findAll().size();

        // Create the PaypallAccount
        restPaypallAccountMockMvc.perform(post("/api/paypallAccounts")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(paypallAccount)))
                .andExpect(status().isCreated());

        // Validate the PaypallAccount in the database
        List<PaypallAccount> paypallAccounts = paypallAccountRepository.findAll();
        assertThat(paypallAccounts).hasSize(databaseSizeBeforeCreate + 1);
        PaypallAccount testPaypallAccount = paypallAccounts.get(paypallAccounts.size() - 1);
        assertThat(testPaypallAccount.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testPaypallAccount.getIsDefault()).isEqualTo(DEFAULT_IS_DEFAULT);
    }

    @Test
    @Transactional
    public void checkEmailIsRequired() throws Exception {
        // Validate the database is empty
        assertThat(paypallAccountRepository.findAll()).hasSize(0);
        // set the field null
        paypallAccount.setEmail(null);

        // Create the PaypallAccount, which fails.
        restPaypallAccountMockMvc.perform(post("/api/paypallAccounts")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(paypallAccount)))
                .andExpect(status().isBadRequest());

        // Validate the database is still empty
        List<PaypallAccount> paypallAccounts = paypallAccountRepository.findAll();
        assertThat(paypallAccounts).hasSize(0);
    }

    @Test
    @Transactional
    public void getAllPaypallAccounts() throws Exception {
        // Initialize the database
        paypallAccountRepository.saveAndFlush(paypallAccount);

        // Get all the paypallAccounts
        restPaypallAccountMockMvc.perform(get("/api/paypallAccounts"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(paypallAccount.getId().intValue())))
                .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
                .andExpect(jsonPath("$.[*].isDefault").value(hasItem(DEFAULT_IS_DEFAULT.booleanValue())));
    }

    @Test
    @Transactional
    public void getPaypallAccount() throws Exception {
        // Initialize the database
        paypallAccountRepository.saveAndFlush(paypallAccount);

        // Get the paypallAccount
        restPaypallAccountMockMvc.perform(get("/api/paypallAccounts/{id}", paypallAccount.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(paypallAccount.getId().intValue()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.toString()))
            .andExpect(jsonPath("$.isDefault").value(DEFAULT_IS_DEFAULT.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingPaypallAccount() throws Exception {
        // Get the paypallAccount
        restPaypallAccountMockMvc.perform(get("/api/paypallAccounts/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePaypallAccount() throws Exception {
        // Initialize the database
        paypallAccountRepository.saveAndFlush(paypallAccount);

		int databaseSizeBeforeUpdate = paypallAccountRepository.findAll().size();

        // Update the paypallAccount
        paypallAccount.setEmail(UPDATED_EMAIL);
        paypallAccount.setIsDefault(UPDATED_IS_DEFAULT);
        restPaypallAccountMockMvc.perform(put("/api/paypallAccounts")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(paypallAccount)))
                .andExpect(status().isOk());

        // Validate the PaypallAccount in the database
        List<PaypallAccount> paypallAccounts = paypallAccountRepository.findAll();
        assertThat(paypallAccounts).hasSize(databaseSizeBeforeUpdate);
        PaypallAccount testPaypallAccount = paypallAccounts.get(paypallAccounts.size() - 1);
        assertThat(testPaypallAccount.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testPaypallAccount.getIsDefault()).isEqualTo(UPDATED_IS_DEFAULT);
    }

    @Test
    @Transactional
    public void deletePaypallAccount() throws Exception {
        // Initialize the database
        paypallAccountRepository.saveAndFlush(paypallAccount);

		int databaseSizeBeforeDelete = paypallAccountRepository.findAll().size();

        // Get the paypallAccount
        restPaypallAccountMockMvc.perform(delete("/api/paypallAccounts/{id}", paypallAccount.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<PaypallAccount> paypallAccounts = paypallAccountRepository.findAll();
        assertThat(paypallAccounts).hasSize(databaseSizeBeforeDelete - 1);
    }
}
