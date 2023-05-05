package pl.ing.transactions.service;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Collection;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import pl.ing.transactions.domain.Account;
import pl.ing.transactions.domain.Transaction;

class TransactionServiceTest {
    private final TransactionService transactionService = new TransactionServiceImpl();

    @ParameterizedTest
    @MethodSource("pl.ing.transactions.TestScenarioDataProvider#generateData")
    void processTransactionsShouldReturnAccountsFromTransactionsTest(
            Collection<Transaction> transactions, Collection<Account> expected) {
        var result = transactionService.processTransactions(transactions);

        assertNotNull(result);
        assertIterableEquals(expected, result);
    }
}
