package pl.ing.transactions;

import org.junit.jupiter.params.provider.Arguments;
import pl.ing.onlinegame.domain.Clan;
import pl.ing.transactions.domain.Account;
import pl.ing.transactions.domain.Transaction;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

public class TestScenarioDataProvider {
        public static Stream<Arguments> generateData() {
                return Stream.of(
                        // Scenario I
                        Arguments.of(
                                // GIVEN transactions:
                                List.of(
                                        new Transaction("account-1", "account-2", bigDecimalOf(100))
                                ),
                                // THEN expect report:
                                Arrays.asList(
                                        new Account("account-1", 1 ,0, bigDecimalOf(-100)),
                                        new Account("account-2", 0, 1, bigDecimalOf(100))
                                )
                        ),
                        // Scenario II
                        Arguments.of(
                                // GIVEN transactions:
                                Arrays.asList(
                                        new Transaction("account-1", "account-2", bigDecimalOf(100)),
                                        new Transaction("account-2", "account-1", bigDecimalOf(100))
                                ),
                                // THEN expect report:
                                Arrays.asList(
                                        new Account("account-1", 1 ,1, bigDecimalOf(0)),
                                        new Account("account-2", 1, 1, bigDecimalOf(0))
                                )
                        ),
                        // Scenario III
                        Arguments.of(
                                // GIVEN transactions:
                                Arrays.asList(
                                        new Transaction("32309111922661937852684864", "06105023389842834748547303", bigDecimalOf(10.90)),
                                        new Transaction("31074318698137062235845814", "66105036543749403346524547", bigDecimalOf(200.90)),
                                        new Transaction("66105036543749403346524547", "32309111922661937852684864", bigDecimalOf(50.10)),
                                        new Transaction("31074318698137062235845814", "66105036543749403346524547", bigDecimalOf(100.99)),
                                        new Transaction("06105023389842834748547303", "99105023389842834748547321", bigDecimalOf(100.99))
                                ),
                                // THEN expect report:
                                Arrays.asList(
                                        new Account("06105023389842834748547303", 1 ,1, bigDecimalOf(-90.09)),
                                        new Account("31074318698137062235845814", 2, 0, bigDecimalOf(-301.89)),
                                        new Account("32309111922661937852684864", 1, 1, bigDecimalOf(39.20)),
                                        new Account("66105036543749403346524547", 1, 2, bigDecimalOf(251.79)),
                                        new Account("99105023389842834748547321", 0, 1, bigDecimalOf(100.99))
                                )
                        ),
                        // Scenario IV
                        Arguments.of(
                                // GIVEN transactions:
                                Collections.emptyList(),
                                // THEN expect report:
                                Collections.emptyList()
                        )
                );
        }

        private static BigDecimal bigDecimalOf(double val) {
                return BigDecimal.valueOf(val).setScale(2, RoundingMode.HALF_UP);
        }
}
