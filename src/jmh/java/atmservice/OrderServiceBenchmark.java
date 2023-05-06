package atmservice;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import pl.ing.atmservice.domain.RequestType;
import pl.ing.atmservice.domain.Task;
import pl.ing.atmservice.service.OrderService;
import pl.ing.atmservice.service.OrderServiceImpl;

public class OrderServiceBenchmark {
    private static final int NUMBER_OF_TASKS = 20000;

    @State(Scope.Thread)
    public static class BenchState {
        private final List<Task> tasks = new ArrayList<>(NUMBER_OF_TASKS);
        private final OrderService orderService = new OrderServiceImpl();

        @Setup(Level.Trial)
        public void setup() {
            var rand = new SecureRandom();
            for (int i = 0; i < NUMBER_OF_TASKS - 1; i++) {
                tasks.add(new Task(rand.nextInt(1000) + 1, randomRequestType(rand), rand.nextInt(1000) + 1));
            }
        }

        private static RequestType randomRequestType(Random rand) {
            return RequestType.values()[rand.nextInt(RequestType.values().length)];
        }
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(MILLISECONDS)
    @Warmup(iterations = 5, time = 50, timeUnit = MILLISECONDS)
    @Measurement(iterations = 10, time = 50, timeUnit = MILLISECONDS)
    public void calculateOrderBench(BenchState state) {
        state.orderService.calculateOrder(state.tasks);
    }
}
