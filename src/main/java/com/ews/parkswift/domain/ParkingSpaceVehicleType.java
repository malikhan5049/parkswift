package com.ews.parkswift.domain;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.validator.constraints.NotEmpty;

import com.ews.parkswift.startup.ApplicationStartup.LookupHeaderCode;
import com.ews.parkswift.validation.InLookupHeader;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * A ParkingSpaceVehicleType.
 */
@Entity
@Table(name = "PARKING_SPACE_SUPPORTED_VEHICLE_TYPE")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ParkingSpaceVehicleType implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @NotEmpty
    @Column(name = "type", nullable = false)
    @InLookupHeader(code=LookupHeaderCode.PS_VT)
    private String type;

    @ManyToOne
    @JsonIgnore
    private ParkingSpace parkingSpace;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ParkingSpace getParkingSpace() {
        return parkingSpace;
    }

    public void setParkingSpace(ParkingSpace parkingSpace) {
        this.parkingSpace = parkingSpace;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ParkingSpaceVehicleType parkingSpaceVehicleType = (ParkingSpaceVehicleType) o;

        if ( ! Objects.equals(type, parkingSpaceVehicleType.type)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(type);
    }

    @Override
    public String toString() {
        return "ParkingSpaceVehicleType{" +
                "id=" + id +
                ", type='" + type + "'" +
                '}';
    }
}
