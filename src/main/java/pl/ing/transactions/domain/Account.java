package pl.ing.transactions.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public class Account implements Comparable<Account> {
    private String account;
    private int debitCount;
    private int creditCount;
    private BigDecimal balance;

    public Account() {}

    public Account(String account, int debitCount, int creditCount, BigDecimal balance) {
        this.account = account;
        this.debitCount = debitCount;
        this.creditCount = creditCount;
        this.balance = balance.setScale(2, RoundingMode.HALF_UP);
    }

    public String getAccount() {
        return account;
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
        return this.account.compareTo(account.getAccount());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account1 = (Account) o;
        return debitCount == account1.debitCount
                && creditCount == account1.creditCount
                && Objects.equals(account, account1.account)
                && Objects.equals(balance, account1.balance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(account, debitCount, creditCount, balance);
    }
}
