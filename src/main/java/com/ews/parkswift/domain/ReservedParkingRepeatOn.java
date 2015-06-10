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
 * A ReservedParkingRepeatOn.
 */
@Entity
@Table(name = "RESERVEDPARKINGREPEATON")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ReservedParkingRepeatOn implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "date_of_month")
    private Integer dateOfMonth;

    @Column(name = "day_of_week")
    private String dayOfWeek;

    @ManyToOne
    private ReservedParking reservedParking;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getDateOfMonth() {
        return dateOfMonth;
    }

    public void setDateOfMonth(Integer dateOfMonth) {
        this.dateOfMonth = dateOfMonth;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public ReservedParking getReservedParking() {
        return reservedParking;
    }

    public void setReservedParking(ReservedParking reservedParking) {
        this.reservedParking = reservedParking;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ReservedParkingRepeatOn reservedParkingRepeatOn = (ReservedParkingRepeatOn) o;

        if ( ! Objects.equals(id, reservedParkingRepeatOn.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ReservedParkingRepeatOn{" +
                "id=" + id +
                ", dateOfMonth='" + dateOfMonth + "'" +
                ", dayOfWeek='" + dayOfWeek + "'" +
                '}';
    }
}
