package com.ews.parkswift.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import com.ews.parkswift.domain.util.CustomDateTimeDeserializer;
import com.ews.parkswift.domain.util.CustomDateTimeSerializer;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * A CustomerBooking.
 */
@Entity
@Table(name = "CUSTOMER_BOOKING")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class CustomerBooking implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Size(max = 45)
    @Column(name = "booking_reference_number", length = 45, nullable = false)
    private String bookingReferenceNumber;

    @NotNull
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @JsonSerialize(using = CustomDateTimeSerializer.class)
    @JsonDeserialize(using = CustomDateTimeDeserializer.class)
    @Column(name = "booking_date_time", nullable = false)
    private DateTime bookingDateTime;

    @NotNull
    @Column(name = "number_of_spaces_booked", nullable = false)
    private Integer numberOfSpacesBooked;

    @NotNull
    @Column(name = "total_amount", precision=10, scale=2, nullable = false)
    private BigDecimal totalAmount;

    @Column(name = "payment_recursive")
    private Boolean paymentRecursive;

    @Size(max = 45)
    @Column(name = "payment_frequency", length = 45)
    private String paymentFrequency;

    @Column(name = "number_of_payments")
    private Integer numberOfPayments;

    @Column(name = "first_payment_amount", precision=10, scale=2)
    private BigDecimal firstPaymentAmount;

    @Column(name = "regular_payment_amount", precision=10, scale=2)
    private BigDecimal regularPaymentAmount;

    @Column(name = "last_payment_amount", precision=10, scale=2)
    private BigDecimal lastPaymentAmount;

    @Column(name = "number_of_payments_made")
    private Integer numberOfPaymentsMade;

    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @JsonSerialize(using = CustomDateTimeSerializer.class)
    @JsonDeserialize(using = CustomDateTimeDeserializer.class)
    @Column(name = "next_payment_date_time")
    private DateTime nextPaymentDateTime;

    @Size(max = 45)
    @Column(name = "vehicle_make", length = 45)
    private String vehicleMake;

    @Size(max = 45)
    @Column(name = "vehicle_model", length = 45)
    private String vehicleModel;

    @Column(name = "model_year")
    private String modelYear;

    @Size(max = 15)
    @Column(name = "licence_plate_number", length = 15)
    private String licencePlateNumber;

    @Size(max = 20)
    @Column(name = "status", length = 20)
    private String status;

    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @JsonSerialize(using = CustomDateTimeSerializer.class)
    @JsonDeserialize(using = CustomDateTimeDeserializer.class)
    @Column(name = "cancelation_date_time")
    private DateTime cancelationDateTime;

    @Column(name = "cancelled_by")
    private Long cancelledBy;

    @OneToMany(mappedBy = "customerBooking")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<BookedParkingSpace> bookedParkingSpaces = new HashSet<>();

    @OneToMany(mappedBy = "customerBooking")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<PaymentCharged> paymentsCharged = new HashSet<>();

    @ManyToOne
    private User user;

    @OneToOne
    private BookingSchedule bookingSchedule;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBookingReferenceNumber() {
        return bookingReferenceNumber;
    }

    public void setBookingReferenceNumber(String bookingReferenceNumber) {
        this.bookingReferenceNumber = bookingReferenceNumber;
    }

    public DateTime getBookingDateTime() {
        return bookingDateTime;
    }

    public void setBookingDateTime(DateTime bookingDateTime) {
        this.bookingDateTime = bookingDateTime;
    }

    public Integer getNumberOfSpacesBooked() {
        return numberOfSpacesBooked;
    }

    public void setNumberOfSpacesBooked(Integer numberOfSpacesBooked) {
        this.numberOfSpacesBooked = numberOfSpacesBooked;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Boolean getPaymentRecursive() {
        return paymentRecursive;
    }

    public void setPaymentRecursive(Boolean paymentRecursive) {
        this.paymentRecursive = paymentRecursive;
    }

    public String getPaymentFrequency() {
        return paymentFrequency;
    }

    public void setPaymentFrequency(String paymentFrequency) {
        this.paymentFrequency = paymentFrequency;
    }

    public Integer getNumberOfPayments() {
        return numberOfPayments;
    }

    public void setNumberOfPayments(Integer numberOfPayments) {
        this.numberOfPayments = numberOfPayments;
    }

    public BigDecimal getFirstPaymentAmount() {
        return firstPaymentAmount;
    }

    public void setFirstPaymentAmount(BigDecimal firstPaymentAmount) {
        this.firstPaymentAmount = firstPaymentAmount;
    }

    public BigDecimal getRegularPaymentAmount() {
        return regularPaymentAmount;
    }

    public void setRegularPaymentAmount(BigDecimal regularPaymentAmount) {
        this.regularPaymentAmount = regularPaymentAmount;
    }

    public BigDecimal getLastPaymentAmount() {
        return lastPaymentAmount;
    }

    public void setLastPaymentAmount(BigDecimal lastPaymentAmount) {
        this.lastPaymentAmount = lastPaymentAmount;
    }

    public Integer getNumberOfPaymentsMade() {
        return numberOfPaymentsMade;
    }

    public void setNumberOfPaymentsMade(Integer numberOfPaymentsMade) {
        this.numberOfPaymentsMade = numberOfPaymentsMade;
    }

    public DateTime getNextPaymentDateTime() {
        return nextPaymentDateTime;
    }

    public void setNextPaymentDateTime(DateTime nextPaymentDateTime) {
        this.nextPaymentDateTime = nextPaymentDateTime;
    }

    public String getVehicleMake() {
        return vehicleMake;
    }

    public void setVehicleMake(String vehicleMake) {
        this.vehicleMake = vehicleMake;
    }

    public String getVehicleModel() {
        return vehicleModel;
    }

    public void setVehicleModel(String vehicleModel) {
        this.vehicleModel = vehicleModel;
    }

    public String getModelYear() {
        return modelYear;
    }

    public void setModelYear(String modelYear) {
        this.modelYear = modelYear;
    }

    public String getLicencePlateNumber() {
        return licencePlateNumber;
    }

    public void setLicencePlateNumber(String licencePlateNumber) {
        this.licencePlateNumber = licencePlateNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public DateTime getCancelationDateTime() {
        return cancelationDateTime;
    }

    public void setCancelationDateTime(DateTime cancelationDateTime) {
        this.cancelationDateTime = cancelationDateTime;
    }

    public Long getCancelledBy() {
        return cancelledBy;
    }

    public void setCancelledBy(Long cancelledBy) {
        this.cancelledBy = cancelledBy;
    }

    public Set<BookedParkingSpace> getBookedParkingSpaces() {
        return bookedParkingSpaces;
    }

    public void setBookedParkingSpaces(Set<BookedParkingSpace> bookedParkingSpaces) {
        this.bookedParkingSpaces = bookedParkingSpaces;
    }

    public Set<PaymentCharged> getPayments() {
        return paymentsCharged;
    }

    public void setPayments(Set<PaymentCharged> payments) {
        this.paymentsCharged = payments;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public BookingSchedule getBookingSchedule() {
        return bookingSchedule;
    }

    public void setBookingSchedule(BookingSchedule bookingSchedule) {
        this.bookingSchedule = bookingSchedule;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CustomerBooking customerBooking = (CustomerBooking) o;

        if ( ! Objects.equals(id, customerBooking.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "CustomerBooking{" +
                "id=" + id +
                ", bookingReferenceNumber='" + bookingReferenceNumber + "'" +
                ", bookingDateTime='" + bookingDateTime + "'" +
                ", numberOfSpacesBooked='" + numberOfSpacesBooked + "'" +
                ", totalAmount='" + totalAmount + "'" +
                ", paymentRecursive='" + paymentRecursive + "'" +
                ", paymentFrequency='" + paymentFrequency + "'" +
                ", numberOfPayments='" + numberOfPayments + "'" +
                ", firstPaymentAmount='" + firstPaymentAmount + "'" +
                ", regularPaymentAmount='" + regularPaymentAmount + "'" +
                ", lastPaymentAmount='" + lastPaymentAmount + "'" +
                ", numberOfPaymentsMade='" + numberOfPaymentsMade + "'" +
                ", nextPaymentDateTime='" + nextPaymentDateTime + "'" +
                ", vehicleMake='" + vehicleMake + "'" +
                ", vehicleModel='" + vehicleModel + "'" +
                ", modelYear='" + modelYear + "'" +
                ", licencePlateNumber='" + licencePlateNumber + "'" +
                ", status='" + status + "'" +
                ", cancelationDateTime='" + cancelationDateTime + "'" +
                ", cancelledBy='" + cancelledBy + "'" +
                '}';
    }
}
