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
 * A AvailableParkingRepeatOn.
 */
@Entity
@Table(name = "PARKING_SPACE_AVAILABILITY_REPEAT_ON")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class AvailableParkingRepeatOn implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "day_of_month")
    private Integer dayOfMonth;

    @Column(name = "date_of_week")
    private String dateOfWeek;

    @ManyToOne
    private AvailableParking availableParking;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getDayOfMonth() {
        return dayOfMonth;
    }

    public void setDayOfMonth(Integer dayOfMonth) {
        this.dayOfMonth = dayOfMonth;
    }

    public String getDateOfWeek() {
        return dateOfWeek;
    }

    public void setDateOfWeek(String dateOfWeek) {
        this.dateOfWeek = dateOfWeek;
    }

    public AvailableParking getAvailableParking() {
        return availableParking;
    }

    public void setAvailableParking(AvailableParking availableParking) {
        this.availableParking = availableParking;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        AvailableParkingRepeatOn availableParkingRepeatOn = (AvailableParkingRepeatOn) o;

        if ( ! Objects.equals(id, availableParkingRepeatOn.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "AvailableParkingRepeatOn{" +
                "id=" + id +
                ", dayOfMonth='" + dayOfMonth + "'" +
                ", dateOfWeek='" + dateOfWeek + "'" +
                '}';
    }
}
