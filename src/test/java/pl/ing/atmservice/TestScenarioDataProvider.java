package pl.ing.atmservice;

import org.junit.jupiter.params.provider.Arguments;
import pl.ing.atmservice.domain.Order;
import pl.ing.atmservice.domain.RequestType;
import pl.ing.atmservice.domain.Task;

import java.util.Arrays;
import java.util.stream.Stream;

public class TestScenarioDataProvider {

    public static Stream<Arguments> generateData() {
        return Stream.of(
                // Scenario I
                Arguments.of(
                        // GIVEN a list of tasks:
                        Arrays.asList(
                                new Task(1, RequestType.STANDARD, 1),
                                new Task(2, RequestType.STANDARD, 1),
                                new Task(1, RequestType.PRIORITY, 1)
                        ),
                        // THEN expect orders:
                        Arrays.asList(
                                new Order(1, 1),
                                new Order(2, 1)
                        )
                ),
                // Scenario II
                Arguments.of(
                        // GIVEN a list of tasks:
                        Arrays.asList(
                                new Task(1, RequestType.STANDARD, 1),
                                new Task(1, RequestType.SIGNAL_LOW, 2),
                                new Task(1, RequestType.PRIORITY, 3),
                                new Task(1, RequestType.FAILURE_RESTART, 4)
                        ),
                        // THEN expect orders:
                        Arrays.asList(
                                new Order(1, 4),
                                new Order(1, 3),
                                new Order(1, 2),
                                new Order(1, 1)
                        )
                ),
                // Scenario III
                Arguments.of(
                        // GIVEN a list of tasks:
                        Arrays.asList(
                                new Task(4, RequestType.STANDARD, 1),
                                new Task(3, RequestType.SIGNAL_LOW, 2),
                                new Task(2, RequestType.PRIORITY, 3),
                                new Task(1, RequestType.FAILURE_RESTART, 4)
                        ),
                        // THEN expect orders:
                        Arrays.asList(
                                new Order(1, 4),
                                new Order(2, 3),
                                new Order(3, 2),
                                new Order(4, 1)
                        )
                ),
                // Scenario IV
                Arguments.of(
                        // GIVEN a list of tasks:
                        Arrays.asList(
                                new Task(1, RequestType.STANDARD, 2),
                                new Task(1, RequestType.STANDARD, 1),
                                new Task(2, RequestType.PRIORITY, 3),
                                new Task(3, RequestType.STANDARD, 4),
                                new Task(4, RequestType.STANDARD, 5),
                                new Task(5, RequestType.PRIORITY, 2),
                                new Task(5, RequestType.STANDARD, 1),
                                new Task(3, RequestType.SIGNAL_LOW, 2),
                                new Task(2, RequestType.SIGNAL_LOW, 1),
                                new Task(3, RequestType.FAILURE_RESTART, 1)
                        ),
                        // THEN expect orders:
                        Arrays.asList(
                                new Order(1, 2),
                                new Order(1, 1),
                                new Order(2, 3),
                                new Order(2, 1),
                                new Order(3, 1),
                                new Order(3, 2),
                                new Order(3, 4),
                                new Order(4, 5),
                                new Order(5, 2),
                                new Order(5, 1)
                        )
                )
        );
    }

}
