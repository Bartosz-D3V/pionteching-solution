package pl.ing.atmservice.service;

import java.util.Collection;
import java.util.List;
import pl.ing.atmservice.domain.Order;
import pl.ing.atmservice.domain.Task;

public interface OrderService {
    Collection<Order> calculateOrder(List<Task> tasks);
}
