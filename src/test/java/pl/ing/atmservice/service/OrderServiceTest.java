package pl.ing.atmservice.service;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;

import java.util.Collection;
import java.util.List;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import pl.ing.atmservice.domain.Order;
import pl.ing.atmservice.domain.Task;

class OrderServiceTest {
    private final OrderService orderService = new OrderServiceImpl();

    @ParameterizedTest
    @MethodSource("pl.ing.atmservice.TestScenarioDataProvider#generateData")
    void calculateOrderShouldReturnOrdersTest(List<Task> tasks, Collection<Order> expected) {
        assertIterableEquals(
                expected, orderService.calculateOrder(tasks).stream().toList());
    }
}
