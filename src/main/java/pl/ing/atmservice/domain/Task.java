package pl.ing.atmservice.domain;

public record Task(int region, RequestType requestType, int atmId) implements Comparable<Task> {
    @Override
    public int compareTo(Task task) {
        var regionCompare = Integer.compare(region, task.region);
        return regionCompare == 0 ? Integer.compare(requestType.ordinal(), task.requestType.ordinal()) : regionCompare;
    }
}
