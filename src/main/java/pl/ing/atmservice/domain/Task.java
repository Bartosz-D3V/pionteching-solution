package pl.ing.atmservice.domain;

import java.util.Comparator;

public record Task(int region, RequestType requestType, int atmId) implements Comparable<Task> {
    @Override
    public int compareTo(Task task) {
        return Comparator.comparing(Task::region)
                .thenComparing(Task::requestType)
                .compare(this, task);
    }
}
