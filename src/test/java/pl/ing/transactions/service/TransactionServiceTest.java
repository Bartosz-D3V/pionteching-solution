package pl.ing.transactions.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;
import pl.ing.transactions.domain.Account;
import pl.ing.transactions.domain.Transaction;

class TransactionServiceTest {
	private final TransactionService transactionService = new TransactionServiceImpl();

	@Test
	void processTransactionsShouldReturnAccountsFromSingleTransaction() {
		var transaction = new Transaction("account-1", "account-2", BigDecimal.valueOf(100));
		var transactions = List.of(transaction);

		var result = transactionService.processTransactions(transactions).toList().blockingGet();
		assertNotNull(result);
		assertEquals(2, result.size());

		var account1 = result.get(0);
		assertEquals("account-1", account1.account());
		assertEquals(1, account1.debitCount());
		assertEquals(0, account1.creditCount());
		assertEquals(BigDecimal.valueOf(-100), account1.balance());

		var account2 = result.get(1);
		assertEquals("account-2", account2.account());
		assertEquals(0, account2.debitCount());
		assertEquals(1, account2.creditCount());
		assertEquals(BigDecimal.valueOf(100), account2.balance());
	}

	@Test
	void processTransactionsShouldReturnAccountsFromTwoTransaction() {
		var transaction1 = new Transaction("account-1", "account-2", BigDecimal.valueOf(100));
		var transaction2 = new Transaction("account-2", "account-1", BigDecimal.valueOf(100));
		var transactions = List.of(transaction1, transaction2);

		var result = transactionService.processTransactions(transactions).toList().blockingGet();
		assertNotNull(result);
		assertEquals(2, result.size());

		var account1 = result.get(0);
		assertEquals("account-1", account1.account());
		assertEquals(1, account1.debitCount());
		assertEquals(1, account1.creditCount());
		assertEquals(BigDecimal.valueOf(0), account1.balance());

		var account2 = result.get(1);
		assertEquals("account-2", account2.account());
		assertEquals(1, account2.debitCount());
		assertEquals(1, account2.creditCount());
		assertEquals(BigDecimal.valueOf(0), account2.balance());
	}

	@Test
	void processTransactionsShouldReturnAccountsFromMultipleTransactions() {
		var transaction1 = new Transaction("32309111922661937852684864", "06105023389842834748547303",
				BigDecimal.valueOf(10.90));
		var transaction2 = new Transaction("31074318698137062235845814", "66105036543749403346524547",
				BigDecimal.valueOf(200.90));
		var transaction3 = new Transaction("66105036543749403346524547", "32309111922661937852684864",
				BigDecimal.valueOf(50.10));
		var transaction4 = new Transaction("31074318698137062235845814", "66105036543749403346524547",
				BigDecimal.valueOf(100.99));
		var transaction5 = new Transaction("06105023389842834748547303", "99105023389842834748547321",
				BigDecimal.valueOf(100.99));
		var transactions = List.of(transaction1, transaction2, transaction3, transaction4, transaction5);

		var result = transactionService.processTransactions(transactions).toMap(Account::account, account -> account)
				.blockingGet();

		assertNotNull(result);
		assertEquals(5, result.size());

		var account1 = result.get("06105023389842834748547303");
		assertNotNull(account1);
		assertEquals(1, account1.debitCount());
		assertEquals(1, account1.creditCount());
		assertEquals(BigDecimal.valueOf(-90.09), account1.balance());

		var account2 = result.get("31074318698137062235845814");
		assertNotNull(account2);
		assertEquals(2, account2.debitCount());
		assertEquals(0, account2.creditCount());
		assertEquals(BigDecimal.valueOf(-301.89), account2.balance());

		var account3 = result.get("32309111922661937852684864");
		assertNotNull(account3);
		assertEquals(1, account3.debitCount());
		assertEquals(1, account3.creditCount());
		assertEquals(BigDecimal.valueOf(39.20), account3.balance());

		var account4 = result.get("66105036543749403346524547");
		assertNotNull(account4);
		assertEquals(1, account4.debitCount());
		assertEquals(2, account4.creditCount());
		assertEquals(BigDecimal.valueOf(251.79), account4.balance());

		var account5 = result.get("99105023389842834748547321");
		assertNotNull(account5);
		assertEquals(0, account5.debitCount());
		assertEquals(1, account5.creditCount());
		assertEquals(BigDecimal.valueOf(100.99), account5.balance());
	}
}
