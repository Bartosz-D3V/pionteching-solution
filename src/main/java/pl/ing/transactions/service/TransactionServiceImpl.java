package pl.ing.transactions.service;

import jakarta.inject.Singleton;
import java.util.Collection;
import java.util.HashMap;
import pl.ing.transactions.domain.Account;
import pl.ing.transactions.domain.Transaction;

@Singleton
public class TransactionServiceImpl implements TransactionService {
    @Override
    public Collection<Account> processTransactions(Collection<Transaction> transactions) {
        var map = new HashMap<String, Account>();
        transactions.forEach(transaction -> {
            var creditAccount = transaction.creditAccount();
            var debitAccount = transaction.debitAccount();
            var amount = transaction.amount();

            map.compute(creditAccount, (key, value) -> {
                if (value == null) {
                    return new Account(creditAccount, 0, 1, amount);
                }
                value.increaseCreditCount();
                value.addBalance(amount);
                return value;
            });

            map.compute(debitAccount, (key, value) -> {
                if (value == null) {
                    return new Account(debitAccount, 1, 0, amount.negate());
                }
                value.increaseDebitCount();
                value.subtractBalance(amount);
                return value;
            });
        });

        return map.values().stream().sorted().toList();
    }
}
