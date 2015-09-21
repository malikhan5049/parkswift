package com.ews.parkswift.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import com.ews.parkswift.domain.util.CustomDateTimeDeserializer;
import com.ews.parkswift.domain.util.CustomDateTimeSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * A PaymentCharged.
 */
@Entity
@Table(name = "PAYMENTCHARGED")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class PaymentCharged implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "amount_charged", precision=10, scale=2)
    private BigDecimal amountCharged;

    @NotNull
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @JsonSerialize(using = CustomDateTimeSerializer.class)
    @JsonDeserialize(using = CustomDateTimeDeserializer.class)
    @Column(name = "transaction_date_time", nullable = false)
    private DateTime transactionDateTime;

    @Column(name = "status")
    private String status;

    @Column(name = "paypall_payment_response")
    private String paypallPaymentResponse;

    @Column(name = "payment_reference_number")
    private String paymentReferenceNumber;

    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @JsonSerialize(using = CustomDateTimeSerializer.class)
    @JsonDeserialize(using = CustomDateTimeDeserializer.class)
    @Column(name = "transfer_to_owner_account_date_time", nullable = false)
    private DateTime transferToOwnerAccountDateTime;

    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @JsonSerialize(using = CustomDateTimeSerializer.class)
    @JsonDeserialize(using = CustomDateTimeDeserializer.class)
    @Column(name = "transfer_to_park_swift_account_date_time", nullable = false)
    private DateTime transferToParkSwiftAccountDateTime;

    @Column(name = "amount_to_transfer_owner_account", precision=10, scale=2, nullable = false)
    private BigDecimal amountToTransferOwnerAccount;

    @Column(name = "amount_to_transfer_park_swift_account", precision=10, scale=2, nullable = false)
    private BigDecimal amountToTransferParkSwiftAccount;

    @Column(name = "todel")
    private String todel;
    
    @Column(name = "paykey")
    private String payKey;

    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @JsonSerialize(using = CustomDateTimeSerializer.class)
    @JsonDeserialize(using = CustomDateTimeDeserializer.class)
    @Column(name = "paykey_expiry_date", nullable = true)
    private DateTime paykeyExpiryDate;
    
    @Column(name = "sender_paypal_account")
    private String senderPaypalEmail;;
    
    @ManyToOne
    private CustomerBooking customerBooking;
    
    public String getSenderPaypalEmail() {
		return senderPaypalEmail;
	}

	public void setSenderPaypalEmail(String senderPaypalEmail) {
		this.senderPaypalEmail = senderPaypalEmail;
	}

	public String getPayKey() {
		return payKey;
	}

	public void setPayKey(String payKey) {
		this.payKey = payKey;
	}

	public DateTime getPaykeyExpiryDate() {
		return paykeyExpiryDate;
	}

	public void setPaykeyExpiryDate(DateTime paykeyExpiryDate) {
		this.paykeyExpiryDate = paykeyExpiryDate;
	}

	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getAmountCharged() {
        return amountCharged;
    }

    public void setAmountCharged(BigDecimal amountCharged) {
        this.amountCharged = amountCharged;
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

    public String getPaymentReferenceNumber() {
        return paymentReferenceNumber;
    }

    public void setPaymentReferenceNumber(String paymentReferenceNumber) {
        this.paymentReferenceNumber = paymentReferenceNumber;
    }

    public DateTime getTransferToOwnerAccountDateTime() {
        return transferToOwnerAccountDateTime;
    }

    public void setTransferToOwnerAccountDateTime(DateTime transferToOwnerAccountDateTime) {
        this.transferToOwnerAccountDateTime = transferToOwnerAccountDateTime;
    }

    public DateTime getTransferToParkSwiftAccountDateTime() {
        return transferToParkSwiftAccountDateTime;
    }

    public void setTransferToParkSwiftAccountDateTime(DateTime transferToParkSwiftAccountDateTime) {
        this.transferToParkSwiftAccountDateTime = transferToParkSwiftAccountDateTime;
    }

    public BigDecimal getAmountToTransferOwnerAccount() {
        return amountToTransferOwnerAccount;
    }

    public void setAmountToTransferOwnerAccount(BigDecimal amountToTransferOwnerAccount) {
        this.amountToTransferOwnerAccount = amountToTransferOwnerAccount;
    }

    public BigDecimal getAmountToTransferParkSwiftAccount() {
        return amountToTransferParkSwiftAccount;
    }

    public void setAmountToTransferParkSwiftAccount(BigDecimal amountToTransferParkSwiftAccount) {
        this.amountToTransferParkSwiftAccount = amountToTransferParkSwiftAccount;
    }

    public String getTodel() {
        return todel;
    }

    public void setTodel(String todel) {
        this.todel = todel;
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

        PaymentCharged paymentCharged = (PaymentCharged) o;

        if ( ! Objects.equals(id, paymentCharged.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "PaymentCharged{" +
                "id=" + id +
                ", amountCharged='" + amountCharged + "'" +
                ", transactionDateTime='" + transactionDateTime + "'" +
                ", status='" + status + "'" +
                ", paypallPaymentResponse='" + paypallPaymentResponse + "'" +
                ", paymentReferenceNumber='" + paymentReferenceNumber + "'" +
                ", transferToOwnerAccountDateTime='" + transferToOwnerAccountDateTime + "'" +
                ", transferToParkSwiftAccountDateTime='" + transferToParkSwiftAccountDateTime + "'" +
                ", amountToTransferOwnerAccount='" + amountToTransferOwnerAccount + "'" +
                ", amountToTransferParkSwiftAccount='" + amountToTransferParkSwiftAccount + "'" +
                ", todel='" + todel + "'" +
                '}';
    }
}
