package com.ews.parkswift.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
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

import com.ews.parkswift.Application;
import com.ews.parkswift.config.Constants;
import com.ews.parkswift.domain.Payment;
import com.ews.parkswift.repository.PaymentRepository;

/**
 * Test class for the PaymentResource REST controller.
 *
 * @see PaymentResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class PaymentResourceTest {

    private static final DateTimeFormatter dateTimeFormatter = Constants.DATETIMEFORMATTER;


    private static final BigDecimal DEFAULT_AMOUNT_PAID = new BigDecimal(0);
    private static final BigDecimal UPDATED_AMOUNT_PAID = new BigDecimal(1);

    private static final DateTime DEFAULT_TRANSACTION_DATE_TIME = new DateTime(0L, DateTimeZone.UTC);
    private static final DateTime UPDATED_TRANSACTION_DATE_TIME = new DateTime(DateTimeZone.UTC).withMillisOfSecond(0);
    private static final String DEFAULT_TRANSACTION_DATE_TIME_STR = dateTimeFormatter.print(DEFAULT_TRANSACTION_DATE_TIME);
    private static final String DEFAULT_STATUS = "SAMPLE_TEXT";
    private static final String UPDATED_STATUS = "UPDATED_TEXT";
    private static final String DEFAULT_PAYPALL_PAYMENT_RESPONSE = "SAMPLE_TEXT";
    private static final String UPDATED_PAYPALL_PAYMENT_RESPONSE = "UPDATED_TEXT";

    @Inject
    private PaymentRepository paymentRepository;

    private MockMvc restPaymentMockMvc;

    private Payment payment;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PaymentResource paymentResource = new PaymentResource();
        ReflectionTestUtils.setField(paymentResource, "paymentRepository", paymentRepository);
        this.restPaymentMockMvc = MockMvcBuilders.standaloneSetup(paymentResource).build();
    }

    @Before
    public void initTest() {
        payment = new Payment();
        payment.setAmountPaid(DEFAULT_AMOUNT_PAID);
        payment.setTransactionDateTime(DEFAULT_TRANSACTION_DATE_TIME);
        payment.setStatus(DEFAULT_STATUS);
        payment.setPaypallPaymentResponse(DEFAULT_PAYPALL_PAYMENT_RESPONSE);
    }

    @Test
    @Transactional
    public void createPayment() throws Exception {
        int databaseSizeBeforeCreate = paymentRepository.findAll().size();

        // Create the Payment
        restPaymentMockMvc.perform(post("/api/payments")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(payment)))
                .andExpect(status().isCreated());

        // Validate the Payment in the database
        List<Payment> payments = paymentRepository.findAll();
        assertThat(payments).hasSize(databaseSizeBeforeCreate + 1);
        Payment testPayment = payments.get(payments.size() - 1);
        assertThat(testPayment.getAmountPaid()).isEqualTo(DEFAULT_AMOUNT_PAID);
        assertThat(testPayment.getTransactionDateTime().toDateTime(DateTimeZone.UTC)).isEqualTo(DEFAULT_TRANSACTION_DATE_TIME);
        assertThat(testPayment.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testPayment.getPaypallPaymentResponse()).isEqualTo(DEFAULT_PAYPALL_PAYMENT_RESPONSE);
    }

    @Test
    @Transactional
    public void checkAmountPaidIsRequired() throws Exception {
        // Validate the database is empty
        assertThat(paymentRepository.findAll()).hasSize(0);
        // set the field null
        payment.setAmountPaid(null);

        // Create the Payment, which fails.
        restPaymentMockMvc.perform(post("/api/payments")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(payment)))
                .andExpect(status().isBadRequest());

        // Validate the database is still empty
        List<Payment> payments = paymentRepository.findAll();
        assertThat(payments).hasSize(0);
    }

    @Test
    @Transactional
    public void getAllPayments() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the payments
        restPaymentMockMvc.perform(get("/api/payments"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(payment.getId().intValue())))
                .andExpect(jsonPath("$.[*].amountPaid").value(hasItem(DEFAULT_AMOUNT_PAID.intValue())))
                .andExpect(jsonPath("$.[*].transactionDateTime").value(hasItem(DEFAULT_TRANSACTION_DATE_TIME_STR)))
                .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
                .andExpect(jsonPath("$.[*].paypallPaymentResponse").value(hasItem(DEFAULT_PAYPALL_PAYMENT_RESPONSE.toString())));
    }

    @Test
    @Transactional
    public void getPayment() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get the payment
        restPaymentMockMvc.perform(get("/api/payments/{id}", payment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(payment.getId().intValue()))
            .andExpect(jsonPath("$.amountPaid").value(DEFAULT_AMOUNT_PAID.intValue()))
            .andExpect(jsonPath("$.transactionDateTime").value(DEFAULT_TRANSACTION_DATE_TIME_STR))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.paypallPaymentResponse").value(DEFAULT_PAYPALL_PAYMENT_RESPONSE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPayment() throws Exception {
        // Get the payment
        restPaymentMockMvc.perform(get("/api/payments/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePayment() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

		int databaseSizeBeforeUpdate = paymentRepository.findAll().size();

        // Update the payment
        payment.setAmountPaid(UPDATED_AMOUNT_PAID);
        payment.setTransactionDateTime(UPDATED_TRANSACTION_DATE_TIME);
        payment.setStatus(UPDATED_STATUS);
        payment.setPaypallPaymentResponse(UPDATED_PAYPALL_PAYMENT_RESPONSE);
        restPaymentMockMvc.perform(put("/api/payments")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(payment)))
                .andExpect(status().isOk());

        // Validate the Payment in the database
        List<Payment> payments = paymentRepository.findAll();
        assertThat(payments).hasSize(databaseSizeBeforeUpdate);
        Payment testPayment = payments.get(payments.size() - 1);
        assertThat(testPayment.getAmountPaid()).isEqualTo(UPDATED_AMOUNT_PAID);
        assertThat(testPayment.getTransactionDateTime().toDateTime(DateTimeZone.UTC)).isEqualTo(UPDATED_TRANSACTION_DATE_TIME);
        assertThat(testPayment.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testPayment.getPaypallPaymentResponse()).isEqualTo(UPDATED_PAYPALL_PAYMENT_RESPONSE);
    }

    @Test
    @Transactional
    public void deletePayment() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

		int databaseSizeBeforeDelete = paymentRepository.findAll().size();

        // Get the payment
        restPaymentMockMvc.perform(delete("/api/payments/{id}", payment.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Payment> payments = paymentRepository.findAll();
        assertThat(payments).hasSize(databaseSizeBeforeDelete - 1);
    }
}
