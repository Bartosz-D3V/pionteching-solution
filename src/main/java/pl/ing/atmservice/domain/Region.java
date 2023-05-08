package pl.ing.atmservice.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;

public class Region {
    private final int regionNumber;
    private final Collection<Order> failureRestartQueue = new ArrayList<>();
    private final Collection<Order> priorityQueue = new ArrayList<>();
    private final Collection<Order> signalLowQueue = new ArrayList<>();
    private final Collection<Order> standardQueue = new ArrayList<>();

    public Region(int regionNumber) {
        this.regionNumber = regionNumber;
    }

    public void add(Task task) {
        var order = new Order(regionNumber, task.atmId());

        switch (task.requestType()) {
            case FAILURE_RESTART -> failureRestartQueue.add(order);
            case PRIORITY -> priorityQueue.add(order);
            case SIGNAL_LOW -> signalLowQueue.add(order);
            case STANDARD -> standardQueue.add(order);
        }
    }

    public Collection<Order> getOrderedAtms() {
        var numOfOrders =
                failureRestartQueue.size() + priorityQueue.size() + signalLowQueue.size() + standardQueue.size() + 1;
        var orders = new LinkedHashSet<Order>(numOfOrders, 1);

        orders.addAll(failureRestartQueue);
        orders.addAll(priorityQueue);
        orders.addAll(signalLowQueue);
        orders.addAll(standardQueue);

        return orders;
    }
}
