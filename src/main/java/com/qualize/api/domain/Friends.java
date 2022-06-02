package com.qualize.api.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Friends.
 */
@Entity
@Table(name = "friends")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Friends implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "friend_name")
    private String friendName;

    @Column(name = "email")
    private String email;

    @Column(name = "telephone")
    private String telephone;

    @Column(name = "phone_number")
    private Integer phoneNumber;

    @Column(name = "wallet_id")
    private String walletId;

    @Column(name = "default_crypto_currency")
    private String defaultCryptoCurrency;

    @Column(name = "date_added")
    private LocalDate dateAdded;

    @Column(name = "date_modified")
    private LocalDate dateModified;

    @ManyToOne
    @JsonIgnoreProperties(value = { "paidBies", "groupName", "activities" }, allowSetters = true)
    private Expenses expense;

    @ManyToOne
    @JsonIgnoreProperties(value = { "accounts" }, allowSetters = true)
    private Settlements settlement;

    @ManyToOne
    @JsonIgnoreProperties(value = { "friendNames" }, allowSetters = true)
    private Accounts accounts;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Friends id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFriendName() {
        return this.friendName;
    }

    public Friends friendName(String friendName) {
        this.setFriendName(friendName);
        return this;
    }

    public void setFriendName(String friendName) {
        this.friendName = friendName;
    }

    public String getEmail() {
        return this.email;
    }

    public Friends email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelephone() {
        return this.telephone;
    }

    public Friends telephone(String telephone) {
        this.setTelephone(telephone);
        return this;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public Integer getPhoneNumber() {
        return this.phoneNumber;
    }

    public Friends phoneNumber(Integer phoneNumber) {
        this.setPhoneNumber(phoneNumber);
        return this;
    }

    public void setPhoneNumber(Integer phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getWalletId() {
        return this.walletId;
    }

    public Friends walletId(String walletId) {
        this.setWalletId(walletId);
        return this;
    }

    public void setWalletId(String walletId) {
        this.walletId = walletId;
    }

    public String getDefaultCryptoCurrency() {
        return this.defaultCryptoCurrency;
    }

    public Friends defaultCryptoCurrency(String defaultCryptoCurrency) {
        this.setDefaultCryptoCurrency(defaultCryptoCurrency);
        return this;
    }

    public void setDefaultCryptoCurrency(String defaultCryptoCurrency) {
        this.defaultCryptoCurrency = defaultCryptoCurrency;
    }

    public LocalDate getDateAdded() {
        return this.dateAdded;
    }

    public Friends dateAdded(LocalDate dateAdded) {
        this.setDateAdded(dateAdded);
        return this;
    }

    public void setDateAdded(LocalDate dateAdded) {
        this.dateAdded = dateAdded;
    }

    public LocalDate getDateModified() {
        return this.dateModified;
    }

    public Friends dateModified(LocalDate dateModified) {
        this.setDateModified(dateModified);
        return this;
    }

    public void setDateModified(LocalDate dateModified) {
        this.dateModified = dateModified;
    }

    public Expenses getExpense() {
        return this.expense;
    }

    public void setExpense(Expenses expenses) {
        this.expense = expenses;
    }

    public Friends expense(Expenses expenses) {
        this.setExpense(expenses);
        return this;
    }

    public Settlements getSettlement() {
        return this.settlement;
    }

    public void setSettlement(Settlements settlements) {
        this.settlement = settlements;
    }

    public Friends settlement(Settlements settlements) {
        this.setSettlement(settlements);
        return this;
    }

    public Accounts getAccounts() {
        return this.accounts;
    }

    public void setAccounts(Accounts accounts) {
        this.accounts = accounts;
    }

    public Friends accounts(Accounts accounts) {
        this.setAccounts(accounts);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Friends)) {
            return false;
        }
        return id != null && id.equals(((Friends) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Friends{" +
            "id=" + getId() +
            ", friendName='" + getFriendName() + "'" +
            ", email='" + getEmail() + "'" +
            ", telephone='" + getTelephone() + "'" +
            ", phoneNumber=" + getPhoneNumber() +
            ", walletId='" + getWalletId() + "'" +
            ", defaultCryptoCurrency='" + getDefaultCryptoCurrency() + "'" +
            ", dateAdded='" + getDateAdded() + "'" +
            ", dateModified='" + getDateModified() + "'" +
            "}";
    }
}
