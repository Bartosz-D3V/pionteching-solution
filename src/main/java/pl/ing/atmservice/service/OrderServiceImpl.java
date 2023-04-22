package pl.ing.atmservice.service;

import jakarta.inject.Singleton;
import java.util.Collection;
import pl.ing.atmservice.domain.Order;
import pl.ing.atmservice.domain.Task;

@Singleton
public class OrderServiceImpl implements OrderService {

    @Override
    public Collection<Order> calculateOrder(Collection<Task> tasks) {
        return tasks.stream().sorted().map(Order::new).distinct().toList();
    }
}
