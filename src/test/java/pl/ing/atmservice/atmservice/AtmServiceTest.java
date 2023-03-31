package pl.ing.atmservice.atmservice;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import io.micronaut.core.type.Argument;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.MediaType;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import java.util.List;
import org.junit.jupiter.api.Test;
import pl.ing.atmservice.domain.Order;
import pl.ing.atmservice.domain.RequestType;
import pl.ing.atmservice.domain.Task;

@MicronautTest
public class AtmServiceTest {
  @Inject
  @Client("/atms")
  private HttpClient client;

  @Test
  public void atmServiceEndpointDuplicateTest() {
    var task1 = new Task(4, RequestType.STANDARD, 1);
    var task2 = new Task(1, RequestType.STANDARD, 1);
    var task3 = new Task(2, RequestType.STANDARD, 1);
    var task4 = new Task(3, RequestType.PRIORITY, 2);
    var task5 = new Task(3, RequestType.STANDARD, 1);
    var task6 = new Task(2, RequestType.SIGNAL_LOW, 1);
    var task7 = new Task(5, RequestType.STANDARD, 2);
    var task8 = new Task(5, RequestType.FAILURE_RESTART, 1);
    var tasks = List.of(task1, task2, task3, task4, task5, task6, task7, task8);

    var request = HttpRequest.POST("/calculateOrder", tasks);
    var orders =
        client
            .toBlocking()
            .retrieve(
                request.contentType(MediaType.APPLICATION_JSON_TYPE), Argument.listOf(Order.class));

    assertNotNull(orders);
    assertEquals(7, orders.size());

    var order1 = orders.get(0);
    assertEquals(1, order1.region());
    assertEquals(1, order1.atmId());

    var order2 = orders.get(1);
    assertEquals(2, order2.region());
    assertEquals(1, order2.atmId());

    var order3 = orders.get(2);
    assertEquals(3, order3.region());
    assertEquals(2, order3.atmId());

    var order4 = orders.get(3);
    assertEquals(3, order4.region());
    assertEquals(1, order4.atmId());

    var order5 = orders.get(4);
    assertEquals(4, order5.region());
    assertEquals(1, order5.atmId());

    var order6 = orders.get(5);
    assertEquals(5, order6.region());
    assertEquals(1, order6.atmId());

    var order7 = orders.get(6);
    assertEquals(5, order7.region());
    assertEquals(2, order7.atmId());
  }

  @Test
  public void atmServiceEndpointTest() {
    var task1 = new Task(1, RequestType.STANDARD, 2);
    var task2 = new Task(1, RequestType.STANDARD, 1);
    var task3 = new Task(2, RequestType.PRIORITY, 3);
    var task4 = new Task(3, RequestType.STANDARD, 4);
    var task5 = new Task(4, RequestType.STANDARD, 5);
    var task6 = new Task(5, RequestType.PRIORITY, 2);
    var task7 = new Task(5, RequestType.STANDARD, 1);
    var task8 = new Task(3, RequestType.SIGNAL_LOW, 2);
    var task9 = new Task(2, RequestType.SIGNAL_LOW, 1);
    var task10 = new Task(3, RequestType.FAILURE_RESTART, 1);
    var tasks = List.of(task1, task2, task3, task4, task5, task6, task7, task8, task9, task10);

    var request = HttpRequest.POST("/calculateOrder", tasks);
    var orders =
        client
            .toBlocking()
            .retrieve(
                request.contentType(MediaType.APPLICATION_JSON_TYPE), Argument.listOf(Order.class));

    assertNotNull(orders);
    assertEquals(10, orders.size());

    var order1 = orders.get(0);
    assertEquals(1, order1.region());
    assertEquals(2, order1.atmId());

    var order2 = orders.get(1);
    assertEquals(1, order2.region());
    assertEquals(1, order2.atmId());

    var order3 = orders.get(2);
    assertEquals(2, order3.region());
    assertEquals(3, order3.atmId());

    var order4 = orders.get(3);
    assertEquals(2, order4.region());
    assertEquals(1, order4.atmId());

    var order5 = orders.get(4);
    assertEquals(3, order5.region());
    assertEquals(1, order5.atmId());

    var order6 = orders.get(5);
    assertEquals(3, order6.region());
    assertEquals(2, order6.atmId());

    var order7 = orders.get(6);
    assertEquals(3, order7.region());
    assertEquals(4, order7.atmId());

    var order8 = orders.get(7);
    assertEquals(4, order8.region());
    assertEquals(5, order8.atmId());

    var order9 = orders.get(8);
    assertEquals(5, order9.region());
    assertEquals(2, order9.atmId());

    var order10 = orders.get(9);
    assertEquals(5, order10.region());
    assertEquals(1, order10.atmId());
  }
}
