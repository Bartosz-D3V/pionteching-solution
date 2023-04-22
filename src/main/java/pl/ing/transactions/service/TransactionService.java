package pl.ing.transactions.service;

import java.util.Collection;
import pl.ing.transactions.domain.Account;
import pl.ing.transactions.domain.Transaction;

public interface TransactionService {
    Collection<Account> processTransactions(Collection<Transaction> transactions);
}
