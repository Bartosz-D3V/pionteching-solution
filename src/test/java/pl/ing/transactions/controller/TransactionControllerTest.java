package pl.ing.transactions.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import io.micronaut.core.type.Argument;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.MediaType;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import pl.ing.transactions.domain.Account;
import pl.ing.transactions.domain.Transaction;

@MicronautTest
class TransactionControllerTest {
    @Inject
    @Client("/transactions")
    private HttpClient client;

    @Test
    void processTransactionsShouldReturnListOfAccounts() {
        var transaction1 =
                new Transaction("32309111922661937852684864", "06105023389842834748547303", BigDecimal.valueOf(10.90));
        var transaction2 =
                new Transaction("31074318698137062235845814", "66105036543749403346524547", BigDecimal.valueOf(200.90));
        var transaction3 =
                new Transaction("66105036543749403346524547", "32309111922661937852684864", BigDecimal.valueOf(50.10));
        var transactions = List.of(transaction1, transaction2, transaction3);

        var request = HttpRequest.POST("/report", transactions);
        var results = client.toBlocking()
                .retrieve(request.contentType(MediaType.APPLICATION_JSON_TYPE), Argument.listOf(Account.class));

        assertNotNull(results);
        assertEquals(4, results.size());

        var accounts =
                results.stream().collect(Collectors.toMap(Account::account, s -> s, (s1, s2) -> s1, HashMap::new));

        var account1 = accounts.get("06105023389842834748547303");
        assertNotNull(account1);
        assertEquals(0, account1.debitCount());
        assertEquals(1, account1.creditCount());
        assertEquals(BigDecimal.valueOf(10.90), account1.balance());

        var account2 = accounts.get("31074318698137062235845814");
        assertNotNull(account2);
        assertEquals(1, account2.debitCount());
        assertEquals(0, account2.creditCount());
        assertEquals(BigDecimal.valueOf(-200.90), account2.balance());

        var account3 = accounts.get("32309111922661937852684864");
        assertNotNull(account3);
        assertEquals(1, account3.debitCount());
        assertEquals(1, account3.creditCount());
        assertEquals(BigDecimal.valueOf(39.20), account3.balance());

        var account4 = accounts.get("66105036543749403346524547");
        assertNotNull(account4);
        assertEquals(1, account4.debitCount());
        assertEquals(1, account4.creditCount());
        assertEquals(BigDecimal.valueOf(150.80), account4.balance());
    }
}
