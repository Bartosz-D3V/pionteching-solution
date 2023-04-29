package pl.ing.atmservice.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import io.micronaut.core.type.Argument;
import io.micronaut.http.HttpHeaders;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MediaType;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import java.util.Collection;
import java.util.List;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import pl.ing.atmservice.domain.Order;
import pl.ing.atmservice.domain.Task;

@MicronautTest
public class AtmServiceTest {
    @Inject
    @Client("/atms")
    private HttpClient client;

    @ParameterizedTest
    @MethodSource("pl.ing.atmservice.TestScenarioDataProvider#generateData")
    public void atmServiceEndpointShouldReturnOrdersTest(List<Task> tasks, Collection<Order> expected) {
        var request = HttpRequest.POST("/calculateOrder", tasks);
        var response = client.toBlocking()
                .exchange(request.contentType(MediaType.APPLICATION_JSON_TYPE), Argument.listOf(Order.class));

        assertEquals(HttpStatus.OK, response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().get(HttpHeaders.CONTENT_TYPE));

        var result = response.body();
        assertNotNull(result);
        assertEquals(expected, result);
    }
}
