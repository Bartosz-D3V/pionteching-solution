package pl.ing.atmservice.domain;

public record Order(int region, int atmId) {
    public Order(Task task) {
        this(task.region(), task.atmId());
    }
}
