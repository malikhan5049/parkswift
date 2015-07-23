package com.ews.parkswift.web.rest;

import com.ews.parkswift.Application;
import com.ews.parkswift.domain.PaymentCharged;
import com.ews.parkswift.repository.PaymentChargedRepository;

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
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the PaymentChargedResource REST controller.
 *
 * @see PaymentChargedResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class PaymentChargedResourceTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");


    private static final BigDecimal DEFAULT_AMOUNT_CHARGED = new BigDecimal(5);
    private static final BigDecimal UPDATED_AMOUNT_CHARGED = new BigDecimal(4);

    private static final DateTime DEFAULT_TRANSACTION_DATE_TIME = new DateTime(0L, DateTimeZone.UTC);
    private static final DateTime UPDATED_TRANSACTION_DATE_TIME = new DateTime(DateTimeZone.UTC).withMillisOfSecond(0);
    private static final String DEFAULT_TRANSACTION_DATE_TIME_STR = dateTimeFormatter.print(DEFAULT_TRANSACTION_DATE_TIME);
    private static final String DEFAULT_STATUS = "SAMPLE_TEXT";
    private static final String UPDATED_STATUS = "UPDATED_TEXT";
    private static final String DEFAULT_PAYPALL_PAYMENT_RESPONSE = "SAMPLE_TEXT";
    private static final String UPDATED_PAYPALL_PAYMENT_RESPONSE = "UPDATED_TEXT";
    private static final String DEFAULT_PAYMENT_REFERENCE_NUMBER = "SAMPLE_TEXT";
    private static final String UPDATED_PAYMENT_REFERENCE_NUMBER = "UPDATED_TEXT";

    private static final DateTime DEFAULT_TRANSFER_TO_OWNER_ACCOUNT_DATE_TIME = new DateTime(0L, DateTimeZone.UTC);
    private static final DateTime UPDATED_TRANSFER_TO_OWNER_ACCOUNT_DATE_TIME = new DateTime(DateTimeZone.UTC).withMillisOfSecond(0);
    private static final String DEFAULT_TRANSFER_TO_OWNER_ACCOUNT_DATE_TIME_STR = dateTimeFormatter.print(DEFAULT_TRANSFER_TO_OWNER_ACCOUNT_DATE_TIME);

    private static final DateTime DEFAULT_TRANSFER_TO_PARK_SWIFT_ACCOUNT_DATE_TIME = new DateTime(0L, DateTimeZone.UTC);
    private static final DateTime UPDATED_TRANSFER_TO_PARK_SWIFT_ACCOUNT_DATE_TIME = new DateTime(DateTimeZone.UTC).withMillisOfSecond(0);
    private static final String DEFAULT_TRANSFER_TO_PARK_SWIFT_ACCOUNT_DATE_TIME_STR = dateTimeFormatter.print(DEFAULT_TRANSFER_TO_PARK_SWIFT_ACCOUNT_DATE_TIME);

    private static final BigDecimal DEFAULT_AMOUNT_TO_TRANSFER_OWNER_ACCOUNT = new BigDecimal(0);
    private static final BigDecimal UPDATED_AMOUNT_TO_TRANSFER_OWNER_ACCOUNT = new BigDecimal(1);

    private static final BigDecimal DEFAULT_AMOUNT_TO_TRANSFER_PARK_SWIFT_ACCOUNT = new BigDecimal(0);
    private static final BigDecimal UPDATED_AMOUNT_TO_TRANSFER_PARK_SWIFT_ACCOUNT = new BigDecimal(1);
    private static final String DEFAULT_TODEL = "SAMPLE_TEXT";
    private static final String UPDATED_TODEL = "UPDATED_TEXT";

    @Inject
    private PaymentChargedRepository paymentChargedRepository;

    private MockMvc restPaymentChargedMockMvc;

    private PaymentCharged paymentCharged;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PaymentChargedResource paymentChargedResource = new PaymentChargedResource();
        ReflectionTestUtils.setField(paymentChargedResource, "paymentChargedRepository", paymentChargedRepository);
        this.restPaymentChargedMockMvc = MockMvcBuilders.standaloneSetup(paymentChargedResource).build();
    }

    @Before
    public void initTest() {
        paymentCharged = new PaymentCharged();
        paymentCharged.setAmountCharged(DEFAULT_AMOUNT_CHARGED);
        paymentCharged.setTransactionDateTime(DEFAULT_TRANSACTION_DATE_TIME);
        paymentCharged.setStatus(DEFAULT_STATUS);
        paymentCharged.setPaypallPaymentResponse(DEFAULT_PAYPALL_PAYMENT_RESPONSE);
        paymentCharged.setPaymentReferenceNumber(DEFAULT_PAYMENT_REFERENCE_NUMBER);
        paymentCharged.setTransferToOwnerAccountDateTime(DEFAULT_TRANSFER_TO_OWNER_ACCOUNT_DATE_TIME);
        paymentCharged.setTransferToParkSwiftAccountDateTime(DEFAULT_TRANSFER_TO_PARK_SWIFT_ACCOUNT_DATE_TIME);
        paymentCharged.setAmountToTransferOwnerAccount(DEFAULT_AMOUNT_TO_TRANSFER_OWNER_ACCOUNT);
        paymentCharged.setAmountToTransferParkSwiftAccount(DEFAULT_AMOUNT_TO_TRANSFER_PARK_SWIFT_ACCOUNT);
        paymentCharged.setTodel(DEFAULT_TODEL);
    }

    @Test
    @Transactional
    public void createPaymentCharged() throws Exception {
        int databaseSizeBeforeCreate = paymentChargedRepository.findAll().size();

        // Create the PaymentCharged
        restPaymentChargedMockMvc.perform(post("/api/paymentChargeds")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(paymentCharged)))
                .andExpect(status().isCreated());

        // Validate the PaymentCharged in the database
        List<PaymentCharged> paymentChargeds = paymentChargedRepository.findAll();
        assertThat(paymentChargeds).hasSize(databaseSizeBeforeCreate + 1);
        PaymentCharged testPaymentCharged = paymentChargeds.get(paymentChargeds.size() - 1);
        assertThat(testPaymentCharged.getAmountCharged()).isEqualTo(DEFAULT_AMOUNT_CHARGED);
        assertThat(testPaymentCharged.getTransactionDateTime().toDateTime(DateTimeZone.UTC)).isEqualTo(DEFAULT_TRANSACTION_DATE_TIME);
        assertThat(testPaymentCharged.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testPaymentCharged.getPaypallPaymentResponse()).isEqualTo(DEFAULT_PAYPALL_PAYMENT_RESPONSE);
        assertThat(testPaymentCharged.getPaymentReferenceNumber()).isEqualTo(DEFAULT_PAYMENT_REFERENCE_NUMBER);
        assertThat(testPaymentCharged.getTransferToOwnerAccountDateTime().toDateTime(DateTimeZone.UTC)).isEqualTo(DEFAULT_TRANSFER_TO_OWNER_ACCOUNT_DATE_TIME);
        assertThat(testPaymentCharged.getTransferToParkSwiftAccountDateTime().toDateTime(DateTimeZone.UTC)).isEqualTo(DEFAULT_TRANSFER_TO_PARK_SWIFT_ACCOUNT_DATE_TIME);
        assertThat(testPaymentCharged.getAmountToTransferOwnerAccount()).isEqualTo(DEFAULT_AMOUNT_TO_TRANSFER_OWNER_ACCOUNT);
        assertThat(testPaymentCharged.getAmountToTransferParkSwiftAccount()).isEqualTo(DEFAULT_AMOUNT_TO_TRANSFER_PARK_SWIFT_ACCOUNT);
        assertThat(testPaymentCharged.getTodel()).isEqualTo(DEFAULT_TODEL);
    }

    @Test
    @Transactional
    public void checkTransactionDateTimeIsRequired() throws Exception {
        // Validate the database is empty
        assertThat(paymentChargedRepository.findAll()).hasSize(0);
        // set the field null
        paymentCharged.setTransactionDateTime(null);

        // Create the PaymentCharged, which fails.
        restPaymentChargedMockMvc.perform(post("/api/paymentChargeds")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(paymentCharged)))
                .andExpect(status().isBadRequest());

        // Validate the database is still empty
        List<PaymentCharged> paymentChargeds = paymentChargedRepository.findAll();
        assertThat(paymentChargeds).hasSize(0);
    }

    @Test
    @Transactional
    public void getAllPaymentChargeds() throws Exception {
        // Initialize the database
        paymentChargedRepository.saveAndFlush(paymentCharged);

        // Get all the paymentChargeds
        restPaymentChargedMockMvc.perform(get("/api/paymentChargeds"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(paymentCharged.getId().intValue())))
                .andExpect(jsonPath("$.[*].amountCharged").value(hasItem(DEFAULT_AMOUNT_CHARGED.intValue())))
                .andExpect(jsonPath("$.[*].transactionDateTime").value(hasItem(DEFAULT_TRANSACTION_DATE_TIME_STR)))
                .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
                .andExpect(jsonPath("$.[*].paypallPaymentResponse").value(hasItem(DEFAULT_PAYPALL_PAYMENT_RESPONSE.toString())))
                .andExpect(jsonPath("$.[*].paymentReferenceNumber").value(hasItem(DEFAULT_PAYMENT_REFERENCE_NUMBER.toString())))
                .andExpect(jsonPath("$.[*].transferToOwnerAccountDateTime").value(hasItem(DEFAULT_TRANSFER_TO_OWNER_ACCOUNT_DATE_TIME_STR)))
                .andExpect(jsonPath("$.[*].transferToParkSwiftAccountDateTime").value(hasItem(DEFAULT_TRANSFER_TO_PARK_SWIFT_ACCOUNT_DATE_TIME_STR)))
                .andExpect(jsonPath("$.[*].amountToTransferOwnerAccount").value(hasItem(DEFAULT_AMOUNT_TO_TRANSFER_OWNER_ACCOUNT.intValue())))
                .andExpect(jsonPath("$.[*].amountToTransferParkSwiftAccount").value(hasItem(DEFAULT_AMOUNT_TO_TRANSFER_PARK_SWIFT_ACCOUNT.intValue())))
                .andExpect(jsonPath("$.[*].todel").value(hasItem(DEFAULT_TODEL.toString())));
    }

    @Test
    @Transactional
    public void getPaymentCharged() throws Exception {
        // Initialize the database
        paymentChargedRepository.saveAndFlush(paymentCharged);

        // Get the paymentCharged
        restPaymentChargedMockMvc.perform(get("/api/paymentChargeds/{id}", paymentCharged.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(paymentCharged.getId().intValue()))
            .andExpect(jsonPath("$.amountCharged").value(DEFAULT_AMOUNT_CHARGED.intValue()))
            .andExpect(jsonPath("$.transactionDateTime").value(DEFAULT_TRANSACTION_DATE_TIME_STR))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.paypallPaymentResponse").value(DEFAULT_PAYPALL_PAYMENT_RESPONSE.toString()))
            .andExpect(jsonPath("$.paymentReferenceNumber").value(DEFAULT_PAYMENT_REFERENCE_NUMBER.toString()))
            .andExpect(jsonPath("$.transferToOwnerAccountDateTime").value(DEFAULT_TRANSFER_TO_OWNER_ACCOUNT_DATE_TIME_STR))
            .andExpect(jsonPath("$.transferToParkSwiftAccountDateTime").value(DEFAULT_TRANSFER_TO_PARK_SWIFT_ACCOUNT_DATE_TIME_STR))
            .andExpect(jsonPath("$.amountToTransferOwnerAccount").value(DEFAULT_AMOUNT_TO_TRANSFER_OWNER_ACCOUNT.intValue()))
            .andExpect(jsonPath("$.amountToTransferParkSwiftAccount").value(DEFAULT_AMOUNT_TO_TRANSFER_PARK_SWIFT_ACCOUNT.intValue()))
            .andExpect(jsonPath("$.todel").value(DEFAULT_TODEL.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPaymentCharged() throws Exception {
        // Get the paymentCharged
        restPaymentChargedMockMvc.perform(get("/api/paymentChargeds/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePaymentCharged() throws Exception {
        // Initialize the database
        paymentChargedRepository.saveAndFlush(paymentCharged);

		int databaseSizeBeforeUpdate = paymentChargedRepository.findAll().size();

        // Update the paymentCharged
        paymentCharged.setAmountCharged(UPDATED_AMOUNT_CHARGED);
        paymentCharged.setTransactionDateTime(UPDATED_TRANSACTION_DATE_TIME);
        paymentCharged.setStatus(UPDATED_STATUS);
        paymentCharged.setPaypallPaymentResponse(UPDATED_PAYPALL_PAYMENT_RESPONSE);
        paymentCharged.setPaymentReferenceNumber(UPDATED_PAYMENT_REFERENCE_NUMBER);
        paymentCharged.setTransferToOwnerAccountDateTime(UPDATED_TRANSFER_TO_OWNER_ACCOUNT_DATE_TIME);
        paymentCharged.setTransferToParkSwiftAccountDateTime(UPDATED_TRANSFER_TO_PARK_SWIFT_ACCOUNT_DATE_TIME);
        paymentCharged.setAmountToTransferOwnerAccount(UPDATED_AMOUNT_TO_TRANSFER_OWNER_ACCOUNT);
        paymentCharged.setAmountToTransferParkSwiftAccount(UPDATED_AMOUNT_TO_TRANSFER_PARK_SWIFT_ACCOUNT);
        paymentCharged.setTodel(UPDATED_TODEL);
        restPaymentChargedMockMvc.perform(put("/api/paymentChargeds")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(paymentCharged)))
                .andExpect(status().isOk());

        // Validate the PaymentCharged in the database
        List<PaymentCharged> paymentChargeds = paymentChargedRepository.findAll();
        assertThat(paymentChargeds).hasSize(databaseSizeBeforeUpdate);
        PaymentCharged testPaymentCharged = paymentChargeds.get(paymentChargeds.size() - 1);
        assertThat(testPaymentCharged.getAmountCharged()).isEqualTo(UPDATED_AMOUNT_CHARGED);
        assertThat(testPaymentCharged.getTransactionDateTime().toDateTime(DateTimeZone.UTC)).isEqualTo(UPDATED_TRANSACTION_DATE_TIME);
        assertThat(testPaymentCharged.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testPaymentCharged.getPaypallPaymentResponse()).isEqualTo(UPDATED_PAYPALL_PAYMENT_RESPONSE);
        assertThat(testPaymentCharged.getPaymentReferenceNumber()).isEqualTo(UPDATED_PAYMENT_REFERENCE_NUMBER);
        assertThat(testPaymentCharged.getTransferToOwnerAccountDateTime().toDateTime(DateTimeZone.UTC)).isEqualTo(UPDATED_TRANSFER_TO_OWNER_ACCOUNT_DATE_TIME);
        assertThat(testPaymentCharged.getTransferToParkSwiftAccountDateTime().toDateTime(DateTimeZone.UTC)).isEqualTo(UPDATED_TRANSFER_TO_PARK_SWIFT_ACCOUNT_DATE_TIME);
        assertThat(testPaymentCharged.getAmountToTransferOwnerAccount()).isEqualTo(UPDATED_AMOUNT_TO_TRANSFER_OWNER_ACCOUNT);
        assertThat(testPaymentCharged.getAmountToTransferParkSwiftAccount()).isEqualTo(UPDATED_AMOUNT_TO_TRANSFER_PARK_SWIFT_ACCOUNT);
        assertThat(testPaymentCharged.getTodel()).isEqualTo(UPDATED_TODEL);
    }

    @Test
    @Transactional
    public void deletePaymentCharged() throws Exception {
        // Initialize the database
        paymentChargedRepository.saveAndFlush(paymentCharged);

		int databaseSizeBeforeDelete = paymentChargedRepository.findAll().size();

        // Get the paymentCharged
        restPaymentChargedMockMvc.perform(delete("/api/paymentChargeds/{id}", paymentCharged.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<PaymentCharged> paymentChargeds = paymentChargedRepository.findAll();
        assertThat(paymentChargeds).hasSize(databaseSizeBeforeDelete - 1);
    }
}
