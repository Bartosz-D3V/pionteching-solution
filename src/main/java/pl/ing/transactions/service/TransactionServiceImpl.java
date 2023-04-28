package pl.ing.transactions.service;

import jakarta.inject.Singleton;
import java.math.RoundingMode;
import java.util.Collection;
import java.util.HashMap;
import pl.ing.transactions.domain.Account;
import pl.ing.transactions.domain.Transaction;

@Singleton
public class TransactionServiceImpl implements TransactionService {
    @Override
    public Collection<Account> processTransactions(Collection<Transaction> transactions) {
        var map = new HashMap<String, Account>();
        for (Transaction transaction : transactions) {
            var creditAccount = transaction.creditAccount();
            var debitAccount = transaction.debitAccount();
            var amount = transaction.amount();

            map.compute(
                    creditAccount,
                    (key, value) -> (value == null)
                            ? new Account(creditAccount, 0, 1, amount.setScale(2, RoundingMode.HALF_UP))
                            : new Account(
                                    creditAccount,
                                    value.debitCount(),
                                    value.creditCount() + 1,
                                    value.balance().add(amount).setScale(2, RoundingMode.HALF_UP)));
            map.compute(
                    debitAccount,
                    (key, value) -> (value == null)
                            ? new Account(debitAccount, 1, 0, amount.negate().setScale(2, RoundingMode.HALF_UP))
                            : new Account(
                                    debitAccount,
                                    value.debitCount() + 1,
                                    value.creditCount(),
                                    value.balance().subtract(amount).setScale(2, RoundingMode.HALF_UP)));
        }

        return map.values().stream().sorted().toList();
    }
}
