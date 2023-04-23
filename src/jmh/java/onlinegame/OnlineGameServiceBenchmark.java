package onlinegame;

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
import pl.ing.onlinegame.domain.Clan;
import pl.ing.onlinegame.domain.Players;
import pl.ing.onlinegame.service.OnlineGameService;
import pl.ing.onlinegame.service.OnlineGameServiceImpl;

public class OnlineGameServiceBenchmark {
    private static final int NUMBER_OF_CLANS = 20000;

    @State(Scope.Thread)
    public static class MyState {
        private Players players;
        private final OnlineGameService onlineGameService = new OnlineGameServiceImpl();

        @Setup(Level.Trial)
        public void setup() {
            Collection<Clan> clans = new ArrayList<>(NUMBER_OF_CLANS);
            var rand = new Random();
            for (int i = 0; i < NUMBER_OF_CLANS - 1; i++) {
                clans.add(new Clan(rand.nextInt(1000) + 1, rand.nextInt(1000) + 1));
            }
            players = new Players(rand.nextInt(100) + 1, clans);
        }
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void calculateGroupsBench(MyState state) {
        state.onlineGameService.calculateGroups(state.players);
    }
}
