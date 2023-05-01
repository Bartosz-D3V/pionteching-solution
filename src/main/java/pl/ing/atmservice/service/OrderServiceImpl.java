package pl.ing.atmservice.service;

import jakarta.inject.Singleton;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import pl.ing.atmservice.domain.Order;
import pl.ing.atmservice.domain.Region;
import pl.ing.atmservice.domain.Task;

@Singleton
public class OrderServiceImpl implements OrderService {
    @Override
    public Collection<Order> calculateOrder(List<Task> tasks) {
        var maxRegion = findMaxRegion(tasks);

        var regions = new Region[maxRegion + 1];
        for (Task task : tasks) {
            Region region = regions[(task.region())];
            if (region == null) {
                var emptyRegion = new Region(task.region());
                emptyRegion.add(task);
                regions[(task.region())] = emptyRegion;
            } else {
                region.add(task);
            }
        }

        return getCombinedOrders(regions);
    }

    private static Integer findMaxRegion(Collection<Task> tasks) {
        return tasks.stream().map(Task::region).reduce(Integer.MIN_VALUE, Integer::max);
    }

    private static ArrayList<Order> getCombinedOrders(Region[] regions) {
        var result = new ArrayList<Order>();
        for (Region region : regions) {
            if (region == null) continue;
            result.addAll(region.getOrderedAtms());
        }
        return result;
    }
}
