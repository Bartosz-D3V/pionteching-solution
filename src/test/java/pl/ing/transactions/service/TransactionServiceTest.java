package pl.ing.transactions.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import pl.ing.transactions.domain.Account;
import pl.ing.transactions.domain.Transaction;

class TransactionServiceTest {
    private final TransactionService transactionService = new TransactionServiceImpl();

    @ParameterizedTest
    @MethodSource("pl.ing.onlinegame.TestScenarioDataProvider#generateData")
    void processTransactionsShouldReturnAccountsFromSingleTransaction(Collection<Transaction> transactions, Collection<Account> expected) {
        var result = transactionService.processTransactions(transactions).toList().blockingGet();

        assertNotNull(result);
        assertEquals(result, expected);
    }
}
