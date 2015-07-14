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

import com.ews.parkswift.startup.ApplicationStartup.LookupHeaderCode;
import com.ews.parkswift.validation.InLookupHeader;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * A availabilityScheduleRepeatOn.
 */
@Entity
@Table(name = "AVAILABILITY_SCHEDULE_REPEAT_ON")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class AvailabilityScheduleRepeatOn implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    
    @Column(name = "date_of_week")
    @InLookupHeader(code=LookupHeaderCode.WEEKDAYS)
    private String dayOfWeek;

    @ManyToOne
    @JsonIgnore
    private AvailabilitySchedule availabilitySchedule;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

   

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public AvailabilitySchedule getAvailabilitySchedule() {
        return availabilitySchedule;
    }

    public void setAvailabilitySchedule(AvailabilitySchedule availabilitySchedule) {
        this.availabilitySchedule = availabilitySchedule;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        AvailabilityScheduleRepeatOn availabilityScheduleRepeatOn = (AvailabilityScheduleRepeatOn) o;

        if ( (dayOfWeek!=null && ! Objects.equals(dayOfWeek, availabilityScheduleRepeatOn.dayOfWeek))) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(dayOfWeek);
    }

    @Override
    public String toString() {
        return "AvailabilityScheduleRepeatOn{" +
                "id=" + id +
                ", dateOfWeek='" + dayOfWeek + "'" +
                '}';
    }
}
