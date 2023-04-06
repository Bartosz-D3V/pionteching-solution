package pl.ing.atmservice.service;

import io.reactivex.rxjava3.core.Flowable;
import jakarta.inject.Singleton;
import java.util.Collection;
import java.util.Comparator;
import pl.ing.atmservice.domain.Order;
import pl.ing.atmservice.domain.Task;

@Singleton
public class OrderServiceImpl implements OrderService {

	@Override
	public Flowable<Order> calculateOrder(Collection<Task> tasks) {
		return Flowable.fromIterable(tasks).sorted(Comparator.comparing(Task::region).thenComparing(Task::requestType))
				.map(task -> new Order(task.region(), task.atmId())).distinct();
	}
}
