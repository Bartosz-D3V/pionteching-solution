package pl.ing.transactions.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;

public record Account(String account, int debitCount, int creditCount, BigDecimal balance)
        implements Comparable<Account> {

    public static Account empty(String account) {
        return new Account(account, 0, 0, BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP));
    }

    @Override
    public int compareTo(Account account) {
        return this.account.compareTo(account.account());
    }
}
