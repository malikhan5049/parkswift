package com.ews.parkswift.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import com.ews.parkswift.domain.util.CustomLocalDateSerializer;
import com.ews.parkswift.domain.util.CustomLocalTimeDeserializer;
import com.ews.parkswift.domain.util.CustomLocalTimeSerializer;
import com.ews.parkswift.domain.util.ISO8601LocalDateDeserializer;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * A ReservedParking.
 */
@Entity
@Table(name = "BOOKING_SCHEDULE")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class BookingSchedule implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDate")
    @JsonSerialize(using = CustomLocalDateSerializer.class)
    @JsonDeserialize(using = ISO8601LocalDateDeserializer.class)
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @NotNull
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDate")
    @JsonSerialize(using = CustomLocalDateSerializer.class)
    @JsonDeserialize(using = ISO8601LocalDateDeserializer.class)
    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @NotNull
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalTime")
    @JsonSerialize(using = CustomLocalTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalTimeDeserializer.class)
    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @NotNull
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalTime")
    @JsonSerialize(using = CustomLocalTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalTimeDeserializer.class)
    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;

    @Column(name = "repeat_basis")
    private String repeatBasis;

    @Column(name = "repeat_occurrences")
    private Integer repeatOccurrences;

    @Column(name = "status")
    private String status;



    @OneToMany(mappedBy = "bookingSchedule")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<BookingScheduleRepeatOn> bookingScheduleRepeatOns = new HashSet<>();


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public String getRepeatBasis() {
        return repeatBasis;
    }

    public void setRepeatBasis(String repeatOn) {
        this.repeatBasis = repeatOn;
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

    public Set<BookingScheduleRepeatOn> getBookingScheduleRepeatOns() {
        return bookingScheduleRepeatOns;
    }

    public void setBookingScheduleRepeatOns(Set<BookingScheduleRepeatOn> bookingScheduleRepeatOns) {
        this.bookingScheduleRepeatOns = bookingScheduleRepeatOns;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        BookingSchedule bookingSchedule = (BookingSchedule) o;

        if ( ! Objects.equals(id, bookingSchedule.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "BookingSchedule{" +
                "id=" + id +
                ", startDate='" + startDate + "'" +
                ", endDate='" + endDate + "'" +
                ", startTime='" + startTime + "'" +
                ", endTime='" + endTime + "'" +
                ", repeatOn='" + repeatBasis + "'" +
                ", repeatOccurrences='" + repeatOccurrences + "'" +
                ", status='" + status + "'" +
                '}';
    }
}
