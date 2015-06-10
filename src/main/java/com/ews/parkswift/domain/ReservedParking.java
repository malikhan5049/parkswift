package com.ews.parkswift.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.ews.parkswift.domain.util.CustomLocalDateSerializer;
import com.ews.parkswift.domain.util.ISO8601LocalDateDeserializer;
import com.ews.parkswift.domain.util.CustomDateTimeDeserializer;
import com.ews.parkswift.domain.util.CustomDateTimeSerializer;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;
import org.joda.time.LocalDate;
import org.joda.time.DateTime;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A ReservedParking.
 */
@Entity
@Table(name = "RESERVEDPARKING")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ReservedParking implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "description")
    private String description;

    @NotNull
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDate")
    @JsonSerialize(using = CustomLocalDateSerializer.class)
    @JsonDeserialize(using = ISO8601LocalDateDeserializer.class)
    @Column(name = "date_start", nullable = false)
    private LocalDate dateStart;

    @NotNull
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDate")
    @JsonSerialize(using = CustomLocalDateSerializer.class)
    @JsonDeserialize(using = ISO8601LocalDateDeserializer.class)
    @Column(name = "date_end", nullable = false)
    private LocalDate dateEnd;

    @NotNull
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @JsonSerialize(using = CustomDateTimeSerializer.class)
    @JsonDeserialize(using = CustomDateTimeDeserializer.class)
    @Column(name = "time_start", nullable = false)
    private DateTime timeStart;

    @NotNull
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @JsonSerialize(using = CustomDateTimeSerializer.class)
    @JsonDeserialize(using = CustomDateTimeDeserializer.class)
    @Column(name = "time_end", nullable = false)
    private DateTime timeEnd;

    @Column(name = "repeat_basis")
    private String repeatBasis;

    @Column(name = "repeat_occurrences")
    private Integer repeatOccurrences;

    @Column(name = "status")
    private String status;

    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @JsonSerialize(using = CustomDateTimeSerializer.class)
    @JsonDeserialize(using = CustomDateTimeDeserializer.class)
    @Column(name = "reserved_on", nullable = false)
    private DateTime reservedOn;

    @Column(name = "parent_id")
    private Integer parentId;

    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @JsonSerialize(using = CustomDateTimeSerializer.class)
    @JsonDeserialize(using = CustomDateTimeDeserializer.class)
    @Column(name = "created_at", nullable = false)
    private DateTime createdAt;

    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @JsonSerialize(using = CustomDateTimeSerializer.class)
    @JsonDeserialize(using = CustomDateTimeDeserializer.class)
    @Column(name = "modified_at", nullable = false)
    private DateTime modifiedAt;

    @ManyToOne
    private ParkingSpace parkingSpace;

    @OneToMany(mappedBy = "reservedParking")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<ReservedParkingRepeatOn> reservedParkingRepeatOns = new HashSet<>();

    @OneToOne(mappedBy = "reservedParking")
    @JsonIgnore
    private Payment payment;

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

    public LocalDate getDateStart() {
        return dateStart;
    }

    public void setDateStart(LocalDate dateStart) {
        this.dateStart = dateStart;
    }

    public LocalDate getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(LocalDate dateEnd) {
        this.dateEnd = dateEnd;
    }

    public DateTime getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(DateTime timeStart) {
        this.timeStart = timeStart;
    }

    public DateTime getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(DateTime timeEnd) {
        this.timeEnd = timeEnd;
    }

    public String getRepeatBasis() {
        return repeatBasis;
    }

    public void setRepeatBasis(String repeatBasis) {
        this.repeatBasis = repeatBasis;
    }

    public Integer getRepeatOccurrences() {
        return repeatOccurrences;
    }

    public void setRepeatOccurrences(Integer repeatOccurrences) {
        this.repeatOccurrences = repeatOccurrences;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public DateTime getReservedOn() {
        return reservedOn;
    }

    public void setReservedOn(DateTime reservedOn) {
        this.reservedOn = reservedOn;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
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

    public ParkingSpace getParkingSpace() {
        return parkingSpace;
    }

    public void setParkingSpace(ParkingSpace parkingSpace) {
        this.parkingSpace = parkingSpace;
    }

    public Set<ReservedParkingRepeatOn> getReservedParkingRepeatOns() {
        return reservedParkingRepeatOns;
    }

    public void setReservedParkingRepeatOns(Set<ReservedParkingRepeatOn> reservedParkingRepeatOns) {
        this.reservedParkingRepeatOns = reservedParkingRepeatOns;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ReservedParking reservedParking = (ReservedParking) o;

        if ( ! Objects.equals(id, reservedParking.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ReservedParking{" +
                "id=" + id +
                ", description='" + description + "'" +
                ", dateStart='" + dateStart + "'" +
                ", dateEnd='" + dateEnd + "'" +
                ", timeStart='" + timeStart + "'" +
                ", timeEnd='" + timeEnd + "'" +
                ", repeatBasis='" + repeatBasis + "'" +
                ", repeatOccurrences='" + repeatOccurrences + "'" +
                ", status='" + status + "'" +
                ", reservedOn='" + reservedOn + "'" +
                ", parentId='" + parentId + "'" +
                ", createdAt='" + createdAt + "'" +
                ", modifiedAt='" + modifiedAt + "'" +
                '}';
    }
}
