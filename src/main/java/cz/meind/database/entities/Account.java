package cz.meind.database.entities;


import cz.meind.interfaces.Column;
import cz.meind.interfaces.Entity;

import java.math.BigDecimal;
import java.util.Objects;

@Entity(tableName = "account")
public class Account implements Comparable<Account> {

    @Column(name = "id", id = true)
    private Integer id;

    @Column(name = "number")
    private Integer accountNumber;

    @Column(name = "balance")
    private BigDecimal balance;

    public int getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(Integer accountNumber) {
        this.accountNumber = accountNumber;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public Account(Integer accountNumber) {
        this.accountNumber = accountNumber;
        this.balance = BigDecimal.ZERO;
    }

    public Account() {
    }

    @Override
    public String toString() {
        return "Account{" + "id=" + id + ", accountNumber=" + accountNumber + ", balance=" + balance + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return Objects.equals(accountNumber, account.accountNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(accountNumber);
    }


    @Override
    public int compareTo(Account o) {
        return this.accountNumber.compareTo(o.accountNumber);
    }
}