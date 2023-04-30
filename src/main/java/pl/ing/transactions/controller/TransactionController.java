package pl.ing.transactions.controller;

import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Consumes;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Produces;
import java.util.Collection;
import pl.ing.transactions.domain.Account;
import pl.ing.transactions.domain.Transaction;
import pl.ing.transactions.service.TransactionService;

@Controller("/transactions")
public class TransactionController {
    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @Post("/report")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<Account> processTransactions(@Body Collection<Transaction> transactions) {
        return transactionService.processTransactions(transactions);
    }
}
