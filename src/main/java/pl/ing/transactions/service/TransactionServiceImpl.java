package pl.ing.transactions.service;

import io.reactivex.rxjava3.core.Flowable;
import jakarta.inject.Singleton;

import java.math.RoundingMode;
import java.util.Collection;
import java.util.TreeMap;
import pl.ing.transactions.domain.Account;
import pl.ing.transactions.domain.Transaction;

@Singleton
public class TransactionServiceImpl implements TransactionService {
    @Override
    public Flowable<Account> processTransactions(Collection<Transaction> transactions) {
        if (transactions == null) return Flowable.empty();

        return Flowable.fromIterable(transactions).reduce(new TreeMap<String, Account>(), (acc, transaction) -> {
            var creditAccount = transaction.creditAccount();
            var debitAccount = transaction.debitAccount();
            var amount = transaction.amount();

            acc.compute(creditAccount,
                    (key, value) -> (value == null)
                            ? new Account(creditAccount, 0, 1, amount.setScale(2, RoundingMode.HALF_UP))
                            : new Account(creditAccount, value.debitCount(), value.creditCount() + 1,
                                    value.balance().add(amount).setScale(2, RoundingMode.HALF_UP)));
            acc.compute(debitAccount,
                    (key, value) -> (value == null)
                            ? new Account(debitAccount, 1, 0, amount.negate().setScale(2, RoundingMode.HALF_UP))
                            : new Account(debitAccount, value.debitCount() + 1, value.creditCount(),
                                    value.balance().subtract(amount).setScale(2, RoundingMode.HALF_UP)));

            return acc;
        }).flattenAsFlowable(TreeMap::values);
    }
}
