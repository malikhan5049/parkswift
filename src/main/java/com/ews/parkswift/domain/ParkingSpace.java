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
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A ParkingSpace.
 */
@Entity
@Table(name = "PARKINGSPACE")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ParkingSpace implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "description")
    private String description;

    @Column(name = "part_of_batch")
    private Boolean partOfBatch;

    @Column(name = "batch_number")
    private Integer batchNumber;

    @Column(name = "full_reserved")
    private Boolean fullReserved;

    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @JsonSerialize(using = CustomDateTimeSerializer.class)
    @JsonDeserialize(using = CustomDateTimeDeserializer.class)
    @Column(name = "created_at")
    private DateTime createdAt;

    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @JsonSerialize(using = CustomDateTimeSerializer.class)
    @JsonDeserialize(using = CustomDateTimeDeserializer.class)
    @Column(name = "modified_at")
    private DateTime modifiedAt;

    @ManyToOne
    private ParkingLocation parkingLocation;

    @OneToMany(mappedBy = "parkingSpace")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<ParkingSpaceVehicleType> parkingSpaceVehicleTypes = new HashSet<>();

    @OneToMany(mappedBy = "parkingSpace")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<ParkingSpacePriceEntry> parkingSpacePriceEntrys = new HashSet<>();

    @OneToMany(mappedBy = "parkingSpace")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<ParkingSpaceImage> parkingSpaceImages = new HashSet<>();

    @OneToMany(mappedBy = "parkingSpace")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<ReservedParking> reservedParkings = new HashSet<>();

    @OneToMany(mappedBy = "parkingSpace")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<AvailableParking> availableParkings = new HashSet<>();

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

    public Boolean getPartOfBatch() {
        return partOfBatch;
    }

    public void setPartOfBatch(Boolean partOfBatch) {
        this.partOfBatch = partOfBatch;
    }

    public Integer getBatchNumber() {
        return batchNumber;
    }

    public void setBatchNumber(Integer batchNumber) {
        this.batchNumber = batchNumber;
    }

    public Boolean getFullReserved() {
        return fullReserved;
    }

    public void setFullReserved(Boolean fullReserved) {
        this.fullReserved = fullReserved;
    }

    public DateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(DateTime createdAt) {
        this.createdAt = createdAt;
    }

    public DateTime getModifiedAt() {
        return modifiedAt;
    }

    public void setModifiedAt(DateTime modifiedAt) {
        this.modifiedAt = modifiedAt;
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

    public Set<ParkingSpaceImage> getParkingSpaceImages() {
        return parkingSpaceImages;
    }

    public void setParkingSpaceImages(Set<ParkingSpaceImage> parkingSpaceImages) {
        this.parkingSpaceImages = parkingSpaceImages;
    }

    public Set<ReservedParking> getReservedParkings() {
        return reservedParkings;
    }

    public void setReservedParkings(Set<ReservedParking> reservedParkings) {
        this.reservedParkings = reservedParkings;
    }

    public Set<AvailableParking> getAvailableParkings() {
        return availableParkings;
    }

    public void setAvailableParkings(Set<AvailableParking> availableParkings) {
        this.availableParkings = availableParkings;
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
                ", partOfBatch='" + partOfBatch + "'" +
                ", batchNumber='" + batchNumber + "'" +
                ", fullReserved='" + fullReserved + "'" +
                ", createdAt='" + createdAt + "'" +
                ", modifiedAt='" + modifiedAt + "'" +
                '}';
    }
}
