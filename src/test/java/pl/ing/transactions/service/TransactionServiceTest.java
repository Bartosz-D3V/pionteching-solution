package pl.ing.transactions.service;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import pl.ing.transactions.domain.Account;
import pl.ing.transactions.domain.Transaction;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class TransactionServiceTest {
    private final TransactionService transactionService = new TransactionServiceImpl();

    @ParameterizedTest
    @MethodSource("pl.ing.transactions.TestScenarioDataProvider#generateData")
    void processTransactionsShouldReturnAccountsFromSingleTransaction(Collection<Transaction> transactions, Collection<Account> expected) {
        var result = transactionService.processTransactions(transactions).toList().blockingGet();

        assertNotNull(result);
        assertEquals(result, expected);
    }
}
