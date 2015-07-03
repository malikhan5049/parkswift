package com.ews.parkswift.domain;

import com.ews.parkswift.startup.ApplicationStartup.LookupHeaderCode;
import com.ews.parkswift.validation.InLookupHeader;
import com.fasterxml.jackson.annotation.JsonIgnore;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A ParkingLocationFacility.
 */
@Entity
@Table(name = "PARKING_LOCATION_FACILITY")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ParkingLocationFacility implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "facility", nullable = false)
    @InLookupHeader(code=LookupHeaderCode.LOC_FACILITY)
    private String facility;

    @ManyToOne
    @JsonIgnore
    private ParkingLocation parkingLocation;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFacility() {
        return facility;
    }

    public void setFacility(String facility) {
        this.facility = facility;
    }

    public ParkingLocation getParkingLocation() {
        return parkingLocation;
    }

    public void setParkingLocation(ParkingLocation parkingLocation) {
        this.parkingLocation = parkingLocation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ParkingLocationFacility parkingLocationFacility = (ParkingLocationFacility) o;

        if ( ! Objects.equals(facility, parkingLocationFacility.facility)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(facility);
    }

    @Override
    public String toString() {
        return "ParkingLocationFacility{" +
                "id=" + id +
                ", facility='" + facility + "'" +
                '}';
    }
}