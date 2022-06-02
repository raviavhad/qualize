package com.qualize.api.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Expenses.
 */
@Entity
@Table(name = "expenses")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Expenses implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "paid_by", precision = 21, scale = 2)
    private BigDecimal paidBy;

    @Column(name = "amount", precision = 21, scale = 2)
    private BigDecimal amount;

    @Column(name = "crypto_currency")
    private String cryptoCurrency;

    @Column(name = "sort_order")
    private Integer sortOrder;

    @Column(name = "date_added")
    private LocalDate dateAdded;

    @Column(name = "date_modified")
    private LocalDate dateModified;

    @OneToMany(mappedBy = "expense")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "expense", "settlement", "accounts" }, allowSetters = true)
    private Set<Friends> paidBies = new HashSet<>();

    @JsonIgnoreProperties(value = { "expenses" }, allowSetters = true)
    @OneToOne(mappedBy = "expenses")
    private Groups groupName;

    @JsonIgnoreProperties(value = { "expenseDetails" }, allowSetters = true)
    @OneToOne(mappedBy = "expenseDetails")
    private Activities activities;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Expenses id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return this.description;
    }

    public Expenses description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPaidBy() {
        return this.paidBy;
    }

    public Expenses paidBy(BigDecimal paidBy) {
        this.setPaidBy(paidBy);
        return this;
    }

    public void setPaidBy(BigDecimal paidBy) {
        this.paidBy = paidBy;
    }

    public BigDecimal getAmount() {
        return this.amount;
    }

    public Expenses amount(BigDecimal amount) {
        this.setAmount(amount);
        return this;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCryptoCurrency() {
        return this.cryptoCurrency;
    }

    public Expenses cryptoCurrency(String cryptoCurrency) {
        this.setCryptoCurrency(cryptoCurrency);
        return this;
    }

    public void setCryptoCurrency(String cryptoCurrency) {
        this.cryptoCurrency = cryptoCurrency;
    }

    public Integer getSortOrder() {
        return this.sortOrder;
    }

    public Expenses sortOrder(Integer sortOrder) {
        this.setSortOrder(sortOrder);
        return this;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    public LocalDate getDateAdded() {
        return this.dateAdded;
    }

    public Expenses dateAdded(LocalDate dateAdded) {
        this.setDateAdded(dateAdded);
        return this;
    }

    public void setDateAdded(LocalDate dateAdded) {
        this.dateAdded = dateAdded;
    }

    public LocalDate getDateModified() {
        return this.dateModified;
    }

    public Expenses dateModified(LocalDate dateModified) {
        this.setDateModified(dateModified);
        return this;
    }

    public void setDateModified(LocalDate dateModified) {
        this.dateModified = dateModified;
    }

    public Set<Friends> getPaidBies() {
        return this.paidBies;
    }

    public void setPaidBies(Set<Friends> friends) {
        if (this.paidBies != null) {
            this.paidBies.forEach(i -> i.setExpense(null));
        }
        if (friends != null) {
            friends.forEach(i -> i.setExpense(this));
        }
        this.paidBies = friends;
    }

    public Expenses paidBies(Set<Friends> friends) {
        this.setPaidBies(friends);
        return this;
    }

    public Expenses addPaidBy(Friends friends) {
        this.paidBies.add(friends);
        friends.setExpense(this);
        return this;
    }

    public Expenses removePaidBy(Friends friends) {
        this.paidBies.remove(friends);
        friends.setExpense(null);
        return this;
    }

    public Groups getGroupName() {
        return this.groupName;
    }

    public void setGroupName(Groups groups) {
        if (this.groupName != null) {
            this.groupName.setExpenses(null);
        }
        if (groups != null) {
            groups.setExpenses(this);
        }
        this.groupName = groups;
    }

    public Expenses groupName(Groups groups) {
        this.setGroupName(groups);
        return this;
    }

    public Activities getActivities() {
        return this.activities;
    }

    public void setActivities(Activities activities) {
        if (this.activities != null) {
            this.activities.setExpenseDetails(null);
        }
        if (activities != null) {
            activities.setExpenseDetails(this);
        }
        this.activities = activities;
    }

    public Expenses activities(Activities activities) {
        this.setActivities(activities);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Expenses)) {
            return false;
        }
        return id != null && id.equals(((Expenses) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Expenses{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            ", paidBy=" + getPaidBy() +
            ", amount=" + getAmount() +
            ", cryptoCurrency='" + getCryptoCurrency() + "'" +
            ", sortOrder=" + getSortOrder() +
            ", dateAdded='" + getDateAdded() + "'" +
            ", dateModified='" + getDateModified() + "'" +
            "}";
    }
}
