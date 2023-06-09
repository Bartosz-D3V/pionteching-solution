package pl.ing.atmservice.controller;

import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Consumes;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Produces;
import java.util.Collection;
import java.util.List;
import pl.ing.atmservice.domain.Order;
import pl.ing.atmservice.domain.Task;
import pl.ing.atmservice.service.OrderService;

@Controller("/atms")
public class TaskController {
    private final OrderService orderService;

    public TaskController(OrderService orderService) {
        this.orderService = orderService;
    }

    @Post("/calculateOrder")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<Order> calculateOrder(@Body List<Task> tasks) {
        return orderService.calculateOrder(tasks);
    }
}
