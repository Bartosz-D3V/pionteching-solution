package pl.ing.transactions.domain;

import java.math.BigDecimal;

public record Account(String account, int debitCount, int creditCount,
		BigDecimal balance) implements Comparable<Account> {

	@Override
	public int compareTo(Account account) {
		return balance.compareTo(account.balance());
	}
}
