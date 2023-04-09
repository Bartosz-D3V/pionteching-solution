import atmservice.OrderServiceBenchmark;
import onlinegame.OnlineGameServiceBenchmark;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import transactions.TransactionServiceBenchmark;

public class Main {
    public static void main(String[] args) throws Exception {
        var options = new OptionsBuilder()
                .include(OnlineGameServiceBenchmark.class.getSimpleName())
                .include(OrderServiceBenchmark.class.getSimpleName())
                .include(TransactionServiceBenchmark.class.getSimpleName())
                .build();
        new Runner(options).run();
    }
}
