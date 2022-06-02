package com.qualize.api.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.qualize.api.domain.enumeration.AccountStatus;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Accounts.
 */
@Entity
@Table(name = "accounts")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Accounts implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "amount_you_owe", precision = 21, scale = 2)
    private BigDecimal amountYouOwe;

    @Column(name = "amount_friend_owes", precision = 21, scale = 2)
    private BigDecimal amountFriendOwes;

    @Column(name = "net_receivable_payable", precision = 21, scale = 2)
    private BigDecimal netReceivablePayable;

    @Column(name = "crypto_currency")
    private String cryptoCurrency;

    @Column(name = "currency_value", precision = 21, scale = 2)
    private BigDecimal currencyValue;

    @Column(name = "crypto_receivable_payable", precision = 21, scale = 2)
    private BigDecimal cryptoReceivablePayable;

    @Column(name = "sort_order")
    private Integer sortOrder;

    @Column(name = "date_added")
    private LocalDate dateAdded;

    @Column(name = "date_modified")
    private LocalDate dateModified;

    @Enumerated(EnumType.STRING)
    @Column(name = "account_status")
    private AccountStatus accountStatus;

    @OneToMany(mappedBy = "accounts")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "expense", "settlement", "accounts" }, allowSetters = true)
    private Set<Friends> friendNames = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Accounts id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getAmountYouOwe() {
        return this.amountYouOwe;
    }

    public Accounts amountYouOwe(BigDecimal amountYouOwe) {
        this.setAmountYouOwe(amountYouOwe);
        return this;
    }

    public void setAmountYouOwe(BigDecimal amountYouOwe) {
        this.amountYouOwe = amountYouOwe;
    }

    public BigDecimal getAmountFriendOwes() {
        return this.amountFriendOwes;
    }

    public Accounts amountFriendOwes(BigDecimal amountFriendOwes) {
        this.setAmountFriendOwes(amountFriendOwes);
        return this;
    }

    public void setAmountFriendOwes(BigDecimal amountFriendOwes) {
        this.amountFriendOwes = amountFriendOwes;
    }

    public BigDecimal getNetReceivablePayable() {
        return this.netReceivablePayable;
    }

    public Accounts netReceivablePayable(BigDecimal netReceivablePayable) {
        this.setNetReceivablePayable(netReceivablePayable);
        return this;
    }

    public void setNetReceivablePayable(BigDecimal netReceivablePayable) {
        this.netReceivablePayable = netReceivablePayable;
    }

    public String getCryptoCurrency() {
        return this.cryptoCurrency;
    }

    public Accounts cryptoCurrency(String cryptoCurrency) {
        this.setCryptoCurrency(cryptoCurrency);
        return this;
    }

    public void setCryptoCurrency(String cryptoCurrency) {
        this.cryptoCurrency = cryptoCurrency;
    }

    public BigDecimal getCurrencyValue() {
        return this.currencyValue;
    }

    public Accounts currencyValue(BigDecimal currencyValue) {
        this.setCurrencyValue(currencyValue);
        return this;
    }

    public void setCurrencyValue(BigDecimal currencyValue) {
        this.currencyValue = currencyValue;
    }

    public BigDecimal getCryptoReceivablePayable() {
        return this.cryptoReceivablePayable;
    }

    public Accounts cryptoReceivablePayable(BigDecimal cryptoReceivablePayable) {
        this.setCryptoReceivablePayable(cryptoReceivablePayable);
        return this;
    }

    public void setCryptoReceivablePayable(BigDecimal cryptoReceivablePayable) {
        this.cryptoReceivablePayable = cryptoReceivablePayable;
    }

    public Integer getSortOrder() {
        return this.sortOrder;
    }

    public Accounts sortOrder(Integer sortOrder) {
        this.setSortOrder(sortOrder);
        return this;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    public LocalDate getDateAdded() {
        return this.dateAdded;
    }

    public Accounts dateAdded(LocalDate dateAdded) {
        this.setDateAdded(dateAdded);
        return this;
    }

    public void setDateAdded(LocalDate dateAdded) {
        this.dateAdded = dateAdded;
    }

    public LocalDate getDateModified() {
        return this.dateModified;
    }

    public Accounts dateModified(LocalDate dateModified) {
        this.setDateModified(dateModified);
        return this;
    }

    public void setDateModified(LocalDate dateModified) {
        this.dateModified = dateModified;
    }

    public AccountStatus getAccountStatus() {
        return this.accountStatus;
    }

    public Accounts accountStatus(AccountStatus accountStatus) {
        this.setAccountStatus(accountStatus);
        return this;
    }

    public void setAccountStatus(AccountStatus accountStatus) {
        this.accountStatus = accountStatus;
    }

    public Set<Friends> getFriendNames() {
        return this.friendNames;
    }

    public void setFriendNames(Set<Friends> friends) {
        if (this.friendNames != null) {
            this.friendNames.forEach(i -> i.setAccounts(null));
        }
        if (friends != null) {
            friends.forEach(i -> i.setAccounts(this));
        }
        this.friendNames = friends;
    }

    public Accounts friendNames(Set<Friends> friends) {
        this.setFriendNames(friends);
        return this;
    }

    public Accounts addFriendName(Friends friends) {
        this.friendNames.add(friends);
        friends.setAccounts(this);
        return this;
    }

    public Accounts removeFriendName(Friends friends) {
        this.friendNames.remove(friends);
        friends.setAccounts(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Accounts)) {
            return false;
        }
        return id != null && id.equals(((Accounts) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Accounts{" +
            "id=" + getId() +
            ", amountYouOwe=" + getAmountYouOwe() +
            ", amountFriendOwes=" + getAmountFriendOwes() +
            ", netReceivablePayable=" + getNetReceivablePayable() +
            ", cryptoCurrency='" + getCryptoCurrency() + "'" +
            ", currencyValue=" + getCurrencyValue() +
            ", cryptoReceivablePayable=" + getCryptoReceivablePayable() +
            ", sortOrder=" + getSortOrder() +
            ", dateAdded='" + getDateAdded() + "'" +
            ", dateModified='" + getDateModified() + "'" +
            ", accountStatus='" + getAccountStatus() + "'" +
            "}";
    }
}
