package com.qualize.api.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.qualize.api.domain.enumeration.SettlementStatus;
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
 * A Settlements.
 */
@Entity
@Table(name = "settlements")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Settlements implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "amount_you_owe", precision = 21, scale = 2)
    private BigDecimal amountYouOwe;

    @Enumerated(EnumType.STRING)
    @Column(name = "settlement_status")
    private SettlementStatus settlementStatus;

    @Column(name = "crypto_currency")
    private String cryptoCurrency;

    @Column(name = "currency_value", precision = 21, scale = 2)
    private BigDecimal currencyValue;

    @Column(name = "crypto_receivable_payable", precision = 21, scale = 2)
    private BigDecimal cryptoReceivablePayable;

    @Column(name = "sort_order")
    private Integer sortOrder;

    @Column(name = "transaction_date")
    private LocalDate transactionDate;

    @Column(name = "date_modified")
    private LocalDate dateModified;

    @OneToMany(mappedBy = "settlement")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "expense", "settlement", "accounts" }, allowSetters = true)
    private Set<Friends> accounts = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Settlements id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return this.description;
    }

    public Settlements description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getAmountYouOwe() {
        return this.amountYouOwe;
    }

    public Settlements amountYouOwe(BigDecimal amountYouOwe) {
        this.setAmountYouOwe(amountYouOwe);
        return this;
    }

    public void setAmountYouOwe(BigDecimal amountYouOwe) {
        this.amountYouOwe = amountYouOwe;
    }

    public SettlementStatus getSettlementStatus() {
        return this.settlementStatus;
    }

    public Settlements settlementStatus(SettlementStatus settlementStatus) {
        this.setSettlementStatus(settlementStatus);
        return this;
    }

    public void setSettlementStatus(SettlementStatus settlementStatus) {
        this.settlementStatus = settlementStatus;
    }

    public String getCryptoCurrency() {
        return this.cryptoCurrency;
    }

    public Settlements cryptoCurrency(String cryptoCurrency) {
        this.setCryptoCurrency(cryptoCurrency);
        return this;
    }

    public void setCryptoCurrency(String cryptoCurrency) {
        this.cryptoCurrency = cryptoCurrency;
    }

    public BigDecimal getCurrencyValue() {
        return this.currencyValue;
    }

    public Settlements currencyValue(BigDecimal currencyValue) {
        this.setCurrencyValue(currencyValue);
        return this;
    }

    public void setCurrencyValue(BigDecimal currencyValue) {
        this.currencyValue = currencyValue;
    }

    public BigDecimal getCryptoReceivablePayable() {
        return this.cryptoReceivablePayable;
    }

    public Settlements cryptoReceivablePayable(BigDecimal cryptoReceivablePayable) {
        this.setCryptoReceivablePayable(cryptoReceivablePayable);
        return this;
    }

    public void setCryptoReceivablePayable(BigDecimal cryptoReceivablePayable) {
        this.cryptoReceivablePayable = cryptoReceivablePayable;
    }

    public Integer getSortOrder() {
        return this.sortOrder;
    }

    public Settlements sortOrder(Integer sortOrder) {
        this.setSortOrder(sortOrder);
        return this;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    public LocalDate getTransactionDate() {
        return this.transactionDate;
    }

    public Settlements transactionDate(LocalDate transactionDate) {
        this.setTransactionDate(transactionDate);
        return this;
    }

    public void setTransactionDate(LocalDate transactionDate) {
        this.transactionDate = transactionDate;
    }

    public LocalDate getDateModified() {
        return this.dateModified;
    }

    public Settlements dateModified(LocalDate dateModified) {
        this.setDateModified(dateModified);
        return this;
    }

    public void setDateModified(LocalDate dateModified) {
        this.dateModified = dateModified;
    }

    public Set<Friends> getAccounts() {
        return this.accounts;
    }

    public void setAccounts(Set<Friends> friends) {
        if (this.accounts != null) {
            this.accounts.forEach(i -> i.setSettlement(null));
        }
        if (friends != null) {
            friends.forEach(i -> i.setSettlement(this));
        }
        this.accounts = friends;
    }

    public Settlements accounts(Set<Friends> friends) {
        this.setAccounts(friends);
        return this;
    }

    public Settlements addAccount(Friends friends) {
        this.accounts.add(friends);
        friends.setSettlement(this);
        return this;
    }

    public Settlements removeAccount(Friends friends) {
        this.accounts.remove(friends);
        friends.setSettlement(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Settlements)) {
            return false;
        }
        return id != null && id.equals(((Settlements) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Settlements{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            ", amountYouOwe=" + getAmountYouOwe() +
            ", settlementStatus='" + getSettlementStatus() + "'" +
            ", cryptoCurrency='" + getCryptoCurrency() + "'" +
            ", currencyValue=" + getCurrencyValue() +
            ", cryptoReceivablePayable=" + getCryptoReceivablePayable() +
            ", sortOrder=" + getSortOrder() +
            ", transactionDate='" + getTransactionDate() + "'" +
            ", dateModified='" + getDateModified() + "'" +
            "}";
    }
}
