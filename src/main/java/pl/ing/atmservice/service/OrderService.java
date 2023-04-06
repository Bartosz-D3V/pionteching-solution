package pl.ing.atmservice.service;

import io.reactivex.rxjava3.core.Flowable;
import java.util.Collection;
import pl.ing.atmservice.domain.Order;
import pl.ing.atmservice.domain.Task;

public interface OrderService {
	Flowable<Order> calculateOrder(Collection<Task> tasks);
}
