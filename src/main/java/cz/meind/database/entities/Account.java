package cz.meind.database.entities;


import cz.meind.interfaces.Column;
import cz.meind.interfaces.Entity;

import java.math.BigDecimal;

@Entity(tableName = "account")
public class Account {

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

    @Override
    public String toString() {
        return "Account{" + "id=" + id + ", accountNumber=" + accountNumber + ", balance=" + balance + '}';
    }
}