package pl.ing.transactions.service;

import io.reactivex.rxjava3.core.Flowable;
import jakarta.inject.Singleton;
import java.util.Collection;
import java.util.TreeMap;
import pl.ing.transactions.domain.Account;
import pl.ing.transactions.domain.Transaction;

@Singleton
public class TransactionServiceImpl implements TransactionService {
  @Override
  public Flowable<Account> processTransactions(Collection<Transaction> transactions) {
    return Flowable.fromIterable(transactions)
        .reduce(
            new TreeMap<String, Account>(),
            (acc, transaction) -> {
              var creditAccount = transaction.creditAccount();
              var debitAccount = transaction.debitAccount();
              var amount = transaction.amount();

              acc.compute(
                  creditAccount,
                  (key, value) ->
                      (value == null)
                          ? new Account(creditAccount, 0, 1, amount)
                          : new Account(
                              creditAccount,
                              value.debitCount(),
                              value.creditCount() + 1,
                              value.balance().add(amount)));
              acc.compute(
                  debitAccount,
                  (key, value) ->
                      (value == null)
                          ? new Account(debitAccount, 1, 0, amount.negate())
                          : new Account(
                              debitAccount,
                              value.debitCount() + 1,
                              value.creditCount(),
                              value.balance().subtract(amount)));

              return acc;
            })
        .flattenAsFlowable(TreeMap::values);
  }
}
