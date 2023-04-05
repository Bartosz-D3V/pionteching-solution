package pl.ing.atmservice.service;

import java.util.List;
import org.junit.jupiter.api.Test;
import pl.ing.atmservice.domain.Order;
import pl.ing.atmservice.domain.RequestType;
import pl.ing.atmservice.domain.Task;

class OrderServiceTest {
    private final OrderService orderService = new OrderServiceImpl();

    @Test
    void calculateOrderShouldReturnUniqueElementsBasedOnOrder() {
        var task1 = new Task(1, RequestType.STANDARD, 1);
        var task3 = new Task(2, RequestType.STANDARD, 1);
        var task2 = new Task(1, RequestType.PRIORITY, 1);
        var tasks = List.of(task1, task2, task3);

        orderService
                .calculateOrder(tasks)
                .test()
                .assertComplete()
                .assertNoErrors()
                .assertValueCount(2)
                .assertValueAt(0, new Order(1, 1))
                .assertValueAt(1, new Order(2, 1));
    }

    @Test
    void calculateOrderShouldReturnOrdersBasedOnPriority() {
        var task1 = new Task(1, RequestType.STANDARD, 1);
        var task2 = new Task(1, RequestType.SIGNAL_LOW, 2);
        var task3 = new Task(1, RequestType.PRIORITY, 3);
        var task4 = new Task(1, RequestType.FAILURE_RESTART, 4);
        var tasks = List.of(task1, task2, task3, task4);

        orderService
                .calculateOrder(tasks)
                .test()
                .assertComplete()
                .assertNoErrors()
                .assertValueCount(4)
                .assertValueAt(0, new Order(1, 4))
                .assertValueAt(1, new Order(1, 3))
                .assertValueAt(2, new Order(1, 2))
                .assertValueAt(3, new Order(1, 1));
    }

    @Test
    void calculateOrderShouldReturnOrdersBasedOnRegion() {
        var task1 = new Task(4, RequestType.STANDARD, 1);
        var task2 = new Task(3, RequestType.SIGNAL_LOW, 2);
        var task3 = new Task(2, RequestType.PRIORITY, 3);
        var task4 = new Task(1, RequestType.FAILURE_RESTART, 4);
        var tasks = List.of(task1, task2, task3, task4);

        orderService
                .calculateOrder(tasks)
                .test()
                .assertComplete()
                .assertNoErrors()
                .assertValueCount(4)
                .assertValueAt(0, new Order(1, 4))
                .assertValueAt(1, new Order(2, 3))
                .assertValueAt(2, new Order(3, 2))
                .assertValueAt(3, new Order(4, 1));
    }
}
