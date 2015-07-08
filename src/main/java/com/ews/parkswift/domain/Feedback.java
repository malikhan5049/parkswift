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
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Feedback.
 */
@Entity
@Table(name = "FEEDBACK")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Feedback implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "rating", nullable = false)
    private Integer rating;

    @NotNull
    @Size(max = 500)
    @Column(name = "comments", length = 500, nullable = false)
    private String comments;

    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @JsonSerialize(using = CustomDateTimeSerializer.class)
    @JsonDeserialize(using = CustomDateTimeDeserializer.class)
    @Column(name = "posted_on", nullable = false)
    private DateTime posted_on;

    @Column(name = "feedback_by")
    private Long feedback_by;

    @ManyToOne
    private User user;

    @ManyToOne
    private ParkingLocation parkingLocation;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public DateTime getPosted_on() {
        return posted_on;
    }

    public void setPosted_on(DateTime posted_on) {
        this.posted_on = posted_on;
    }

    public Long getFeedback_by() {
        return feedback_by;
    }

    public void setFeedback_by(Long feedback_by) {
        this.feedback_by = feedback_by;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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

        Feedback feedback = (Feedback) o;

        if ( ! Objects.equals(id, feedback.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Feedback{" +
                "id=" + id +
                ", rating='" + rating + "'" +
                ", comments='" + comments + "'" +
                ", posted_on='" + posted_on + "'" +
                ", feedback_by='" + feedback_by + "'" +
                '}';
    }
}
