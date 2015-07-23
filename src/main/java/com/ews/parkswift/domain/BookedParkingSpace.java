package com.ews.parkswift.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A BookedParkingSpace.
 */
@Entity
@Table(name = "BOOKED_PARKING_SPACE")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class BookedParkingSpace implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private ParkingSpace parkingSpace;

    @ManyToOne
    private CustomerBooking customerBooking;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ParkingSpace getParkingSpace() {
        return parkingSpace;
    }

    public void setParkingSpace(ParkingSpace parkingSpace) {
        this.parkingSpace = parkingSpace;
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

        BookedParkingSpace bookedParkingSpace = (BookedParkingSpace) o;

        if ( ! Objects.equals(id, bookedParkingSpace.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "BookedParkingSpace{" +
                "id=" + id +
                '}';
    }
}
