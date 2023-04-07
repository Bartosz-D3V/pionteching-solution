package pl.ing.atmservice.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import pl.ing.atmservice.domain.Order;
import pl.ing.atmservice.domain.RequestType;
import pl.ing.atmservice.domain.Task;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

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
