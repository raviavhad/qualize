package com.qualize.api.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A CryptoTracker.
 */
@Entity
@Table(name = "crypto_tracker")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class CryptoTracker implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "currency", nullable = false)
    private String currency;

    @Column(name = "value", precision = 21, scale = 2)
    private BigDecimal value;

    @Column(name = "feed_date_time")
    private LocalDate feedDateTime;

    @Column(name = "date_added")
    private LocalDate dateAdded;

    @Column(name = "date_modified")
    private LocalDate dateModified;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CryptoTracker id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCurrency() {
        return this.currency;
    }

    public CryptoTracker currency(String currency) {
        this.setCurrency(currency);
        return this;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public BigDecimal getValue() {
        return this.value;
    }

    public CryptoTracker value(BigDecimal value) {
        this.setValue(value);
        return this;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public LocalDate getFeedDateTime() {
        return this.feedDateTime;
    }

    public CryptoTracker feedDateTime(LocalDate feedDateTime) {
        this.setFeedDateTime(feedDateTime);
        return this;
    }

    public void setFeedDateTime(LocalDate feedDateTime) {
        this.feedDateTime = feedDateTime;
    }

    public LocalDate getDateAdded() {
        return this.dateAdded;
    }

    public CryptoTracker dateAdded(LocalDate dateAdded) {
        this.setDateAdded(dateAdded);
        return this;
    }

    public void setDateAdded(LocalDate dateAdded) {
        this.dateAdded = dateAdded;
    }

    public LocalDate getDateModified() {
        return this.dateModified;
    }

    public CryptoTracker dateModified(LocalDate dateModified) {
        this.setDateModified(dateModified);
        return this;
    }

    public void setDateModified(LocalDate dateModified) {
        this.dateModified = dateModified;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CryptoTracker)) {
            return false;
        }
        return id != null && id.equals(((CryptoTracker) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CryptoTracker{" +
            "id=" + getId() +
            ", currency='" + getCurrency() + "'" +
            ", value=" + getValue() +
            ", feedDateTime='" + getFeedDateTime() + "'" +
            ", dateAdded='" + getDateAdded() + "'" +
            ", dateModified='" + getDateModified() + "'" +
            "}";
    }
}
