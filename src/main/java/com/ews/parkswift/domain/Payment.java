package com.ews.parkswift.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.ews.parkswift.domain.util.CustomDateTimeDeserializer;
import com.ews.parkswift.domain.util.CustomDateTimeSerializer;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Payment.
 */
@Entity
@Table(name = "PAYMENT")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Payment implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "amount_paid", precision=10, scale=2, nullable = false)
    private BigDecimal amountPaid;

    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @JsonSerialize(using = CustomDateTimeSerializer.class)
    @JsonDeserialize(using = CustomDateTimeDeserializer.class)
    @Column(name = "transaction_date_time", nullable = false)
    private DateTime transactionDateTime;

    @Column(name = "status")
    private String status;

    @Column(name = "paypall_payment_response")
    private String paypallPaymentResponse;

    @ManyToOne
    private User user;

    @ManyToOne
    private CustomerBooking customerBooking;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(BigDecimal amountPaid) {
        this.amountPaid = amountPaid;
    }

    public DateTime getTransactionDateTime() {
        return transactionDateTime;
    }

    public void setTransactionDateTime(DateTime transactionDateTime) {
        this.transactionDateTime = transactionDateTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPaypallPaymentResponse() {
        return paypallPaymentResponse;
    }

    public void setPaypallPaymentResponse(String paypallPaymentResponse) {
        this.paypallPaymentResponse = paypallPaymentResponse;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public CustomerBooking getCustomerBooking() {
        return customerBooking;
    }

    public void setCustomerBooking(CustomerBooking customerBooking) {
        this.customerBooking = customerBooking;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Payment payment = (Payment) o;

        if ( ! Objects.equals(id, payment.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Payment{" +
                "id=" + id +
                ", amountPaid='" + amountPaid + "'" +
                ", transactionDateTime='" + transactionDateTime + "'" +
                ", status='" + status + "'" +
                ", paypallPaymentResponse='" + paypallPaymentResponse + "'" +
                '}';
    }
}
