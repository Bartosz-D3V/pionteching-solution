package pl.ing.transactions.service;

import io.reactivex.rxjava3.core.Flowable;
import java.util.Collection;
import pl.ing.transactions.domain.Account;
import pl.ing.transactions.domain.Transaction;

public interface TransactionService {
  Flowable<Account> processTransactions(Collection<Transaction> transactions);
}
