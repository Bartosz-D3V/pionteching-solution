package atmservice;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import pl.ing.atmservice.domain.RequestType;
import pl.ing.atmservice.domain.Task;
import pl.ing.atmservice.service.OrderService;
import pl.ing.atmservice.service.OrderServiceImpl;

public class OrderServiceBenchmark {
    private static final int NUMBER_OF_TASKS = 20000;

    @State(Scope.Thread)
    public static class MyState {
        Collection<Task> tasks = new ArrayList<>(NUMBER_OF_TASKS);
        OrderService orderService = new OrderServiceImpl();

        @Setup(Level.Trial)
        public void setup() {
            var rand = new SecureRandom();
            for (int i = 0; i < NUMBER_OF_TASKS; i++) {
                tasks.add(new Task(rand.nextInt(1000) + 1, randomRequestType(rand), rand.nextInt(1000) + 1));
            }
        }

        private static RequestType randomRequestType(Random rand) {
            return RequestType.values()[rand.nextInt(RequestType.values().length)];
        }
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void calculateGroupsBench(MyState state) {
        state.orderService.calculateOrder(state.tasks).toList().blockingGet();
    }
}
