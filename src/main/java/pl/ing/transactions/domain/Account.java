package pl.ing.transactions.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public class Account implements Comparable<Account> {
    @JsonProperty("account")
    private String number;

    private int debitCount;
    private int creditCount;
    private BigDecimal balance;

    public Account() {}

    public Account(String number, int debitCount, int creditCount, BigDecimal balance) {
        this.number = number;
        this.debitCount = debitCount;
        this.creditCount = creditCount;
        this.balance = balance.setScale(2, RoundingMode.HALF_UP);
    }

    public String getNumber() {
        return number;
    }

    public int getDebitCount() {
        return debitCount;
    }

    public void setDebitCount(int debitCount) {
        this.debitCount = debitCount;
    }

    public int getCreditCount() {
        return creditCount;
    }

    public void setCreditCount(int creditCount) {
        this.creditCount = creditCount;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public void increaseDebitCount() {
        this.debitCount++;
    }

    public void increaseCreditCount() {
        this.creditCount++;
    }

    public void addBalance(BigDecimal amount) {
        this.balance = this.balance.add(amount);
    }

    public void subtractBalance(BigDecimal amount) {
        this.balance = this.balance.subtract(amount);
    }

    @Override
    public int compareTo(Account account) {
        return this.number.compareTo(account.getNumber());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account1 = (Account) o;
        return debitCount == account1.debitCount
                && creditCount == account1.creditCount
                && Objects.equals(number, account1.number)
                && Objects.equals(balance, account1.balance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(number, debitCount, creditCount, balance);
    }
}
