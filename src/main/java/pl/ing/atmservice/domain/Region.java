package pl.ing.atmservice.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;

public class Region {
    private final int regionNumber;
    private final Collection<Integer> failureRestartQueue = new ArrayList<>();
    private final Collection<Integer> priorityQueue = new ArrayList<>();
    private final Collection<Integer> signalLowQueue = new ArrayList<>();
    private final Collection<Integer> standardQueue = new ArrayList<>();

    public Region(int regionNumber) {
        this.regionNumber = regionNumber;
    }

    public void add(Task task) {
        switch (task.requestType()) {
            case FAILURE_RESTART -> failureRestartQueue.add(task.atmId());
            case PRIORITY -> priorityQueue.add(task.atmId());
            case SIGNAL_LOW -> signalLowQueue.add(task.atmId());
            case STANDARD -> standardQueue.add(task.atmId());
        }
    }

    public Collection<Order> getOrderedAtms() {
        var orders = new LinkedHashSet<Order>();

        for (Integer i : failureRestartQueue) {
            orders.add(new Order(regionNumber, i));
        }
        for (Integer i : priorityQueue) {
            orders.add(new Order(regionNumber, i));
        }
        for (Integer i : signalLowQueue) {
            orders.add(new Order(regionNumber, i));
        }
        for (Integer i : standardQueue) {
            orders.add(new Order(regionNumber, i));
        }

        return orders;
    }
}
