package transactions;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import pl.ing.transactions.domain.Transaction;
import pl.ing.transactions.service.TransactionService;
import pl.ing.transactions.service.TransactionServiceImpl;

public class TransactionServiceBenchmark {
    private static final int NUMBER_OF_TRANSACTIONS = 100000;

    @State(Scope.Thread)
    public static class BenchState {
        private final Collection<Transaction> transactions = new ArrayList<>(NUMBER_OF_TRANSACTIONS);
        private final TransactionService transactionService = new TransactionServiceImpl();

        @Setup(Level.Trial)
        public void setup() {
            for (int i = 0; i < NUMBER_OF_TRANSACTIONS - 1; i++) {
                var debitAccount = UUID.randomUUID().toString().substring(0, 2);
                var creditAccount = UUID.randomUUID().toString().substring(0, 2);
                transactions.add(new Transaction(debitAccount, creditAccount, BigDecimal.valueOf(getRandomDouble())));
            }
        }

        private static double getRandomDouble() {
            final double MIN = 1.00;
            final double MAX = 100000.00;
            double random = new SecureRandom().nextDouble();
            double result = MIN + (random * (MAX - MIN));
            return Math.round(result * 100.0) / 100.0;
        }
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    @Warmup(iterations = 5, time = 50, timeUnit = MILLISECONDS)
    @Measurement(iterations = 10, time = 50, timeUnit = MILLISECONDS)
    public void processTransactionsBench(BenchState state) {
        state.transactionService.processTransactions(state.transactions);
    }
}
