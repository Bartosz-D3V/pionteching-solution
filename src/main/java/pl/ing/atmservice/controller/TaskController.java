package pl.ing.atmservice.controller;

import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Consumes;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Produces;
import io.reactivex.rxjava3.core.Flowable;
import java.util.Collection;
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
  public Flowable<Order> calculateOrder(@Body Collection<Task> tasks) {
    return orderService.calculateOrder(tasks);
  }
}
