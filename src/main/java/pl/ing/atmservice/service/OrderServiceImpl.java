package pl.ing.atmservice.service;

import jakarta.inject.Singleton;
import java.util.Collection;
import java.util.List;
import pl.ing.atmservice.domain.Order;
import pl.ing.atmservice.domain.Task;

@Singleton
public class OrderServiceImpl implements OrderService {

    @Override
    public Collection<Order> calculateOrder(List<Task> tasks) {
        tasks.sort(Task::compareTo);
        return tasks.stream().map(Order::new).distinct().toList();
    }
}
