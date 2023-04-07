package pl.ing.atmservice.service;

import java.util.Collection;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import pl.ing.atmservice.domain.Order;
import pl.ing.atmservice.domain.Task;

class OrderServiceTest {
    private final OrderService orderService = new OrderServiceImpl();

    @ParameterizedTest
    @MethodSource("pl.ing.atmservice.TestScenarioDataProvider#generateData")
    void calculateOrderShouldReturnOrderedOrders(Collection<Task> tasks, Collection<Order> expected) {
        orderService
                .calculateOrder(tasks)
                .test()
                .assertComplete()
                .assertNoErrors()
                .assertValueSequence(expected);
    }
}
