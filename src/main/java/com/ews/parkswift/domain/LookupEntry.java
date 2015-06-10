package com.ews.parkswift.domain;

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
 * A LookupEntry.
 */
@Entity
@Table(name = "LOOKUPENTRY")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class LookupEntry implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "value", nullable = false)
    private String value;

    @Column(name = "description")
    private String description;

    @ManyToOne
    private LookupHeader lookupHeader;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LookupHeader getLookupHeader() {
        return lookupHeader;
    }

    public void setLookupHeader(LookupHeader lookupHeader) {
        this.lookupHeader = lookupHeader;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        LookupEntry lookupEntry = (LookupEntry) o;

        if ( ! Objects.equals(id, lookupEntry.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "LookupEntry{" +
                "id=" + id +
                ", value='" + value + "'" +
                ", description='" + description + "'" +
                '}';
    }
}
