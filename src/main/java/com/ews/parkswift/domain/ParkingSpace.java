package com.ews.parkswift.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;

/**
 * A ParkingSpace.
 */
@Entity
@Table(name = "PARKING_SPACE")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ParkingSpace implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "description")
    private String description;

    @Column(name = "number_of_spaces", nullable = false)
    private Integer numberOfSpaces;

    @Column(name = "group_record")
    @JsonIgnore
    private Boolean groupRecord;

    @Column(name = "group_number")
    @JsonIgnore
    private Integer groupNumber;

    @Column(name = "full_reserved")
    @JsonIgnore
    private Boolean fullReserved;
    
    @NotNull
    @ManyToOne
    @JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
    @JsonIdentityReference(alwaysAsId=true)
    private ParkingLocation parkingLocation;
    
    @Valid
    @NotEmpty
    @OneToMany(mappedBy = "parkingSpace", fetch=FetchType.EAGER, cascade=CascadeType.ALL)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<ParkingSpaceVehicleType> parkingSpaceVehicleTypes = new HashSet<>();

    @Valid
    @NotEmpty
    @OneToMany(mappedBy = "parkingSpace", fetch=FetchType.EAGER , cascade=CascadeType.ALL)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<ParkingSpacePriceEntry> parkingSpacePriceEntrys = new HashSet<>();
    
    @Valid
    @NotEmpty
    @OneToMany(mappedBy = "parkingSpace", fetch=FetchType.EAGER , cascade=CascadeType.ALL)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<AvailableParking> availableParkings = new HashSet<>();
    
    @OneToMany(mappedBy = "parkingSpace")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<ReservedParking> reservedParkings = new HashSet<>();


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getNumberOfSpaces() {
        return numberOfSpaces;
    }

    public void setNumberOfSpaces(Integer numberOfSpaces) {
        this.numberOfSpaces = numberOfSpaces;
    }

    public Boolean getGroupRecord() {
        return groupRecord;
    }

    public void setGroupRecord(Boolean groupRecord) {
        this.groupRecord = groupRecord;
    }

    public Integer getGroupNumber() {
        return groupNumber;
    }

    public void setGroupNumber(Integer groupNumber) {
        this.groupNumber = groupNumber;
    }

    public Boolean getFullReserved() {
        return fullReserved;
    }

    public void setFullReserved(Boolean fullReserved) {
        this.fullReserved = fullReserved;
    }

    public ParkingLocation getParkingLocation() {
        return parkingLocation;
    }

    public void setParkingLocation(ParkingLocation parkingLocation) {
        this.parkingLocation = parkingLocation;
    }

    public Set<ParkingSpaceVehicleType> getParkingSpaceVehicleTypes() {
        return parkingSpaceVehicleTypes;
    }

    public void setParkingSpaceVehicleTypes(Set<ParkingSpaceVehicleType> parkingSpaceVehicleTypes) {
        this.parkingSpaceVehicleTypes = parkingSpaceVehicleTypes;
    }

    public Set<ParkingSpacePriceEntry> getParkingSpacePriceEntrys() {
        return parkingSpacePriceEntrys;
    }

    public void setParkingSpacePriceEntrys(Set<ParkingSpacePriceEntry> parkingSpacePriceEntrys) {
        this.parkingSpacePriceEntrys = parkingSpacePriceEntrys;
    }

    public Set<AvailableParking> getAvailableParkings() {
        return availableParkings;
    }

    public void setAvailableParkings(Set<AvailableParking> availableParkings) {
        this.availableParkings = availableParkings;
    }
    
    public Set<ReservedParking> getReservedParkings() {
        return reservedParkings;
    }

    public void setReservedParkings(Set<ReservedParking> reservedParkings) {
        this.reservedParkings = reservedParkings;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ParkingSpace parkingSpace = (ParkingSpace) o;

        if ( ! Objects.equals(id, parkingSpace.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ParkingSpace{" +
                "id=" + id +
                ", description='" + description + "'" +
                ", numberOfSpaces='" + numberOfSpaces + "'" +
                ", groupRecord='" + groupRecord + "'" +
                ", groupNumber='" + groupNumber + "'" +
                ", fullReserved='" + fullReserved + "'" +
                '}';
    }
}
