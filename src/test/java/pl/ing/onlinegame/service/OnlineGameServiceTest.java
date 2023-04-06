package pl.ing.onlinegame.service;

import io.reactivex.rxjava3.core.Flowable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import pl.ing.onlinegame.domain.Clan;
import pl.ing.onlinegame.domain.Players;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;
import java.util.TreeMap;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class OnlineGameServiceTest {
    private final OnlineGameService onlineGameService = new OnlineGameServiceImpl();

    @ParameterizedTest
    @MethodSource("generateData")
    void calculateGroupsShouldFilterOurClansByPlayersCount(int maxPlayers, List<Clan> clans, List<List<Clan>> expected) {
        final ArrayList<ArrayList<Clan>> result = onlineGameService.calculateGroups(new Players(maxPlayers, clans));
        onlineGameService.calculateGroups(new Players(maxPlayers, clans));
        assertNotNull(result);
        assertEquals(expected, result);
    }

    @ParameterizedTest
    @MethodSource("generateData")
    void testAsync(int maxPlayers, List<Clan> clans, List<List<Clan>> expected) {
        var result = onlineGameService.calculateGroupsAsync(new Players(maxPlayers, clans)).toList().blockingGet();
//        onlineGameService.calculateGroupsAsync(new Players(maxPlayers, clans)).toList().blockingGet();
        assertNotNull(result);
        assertEquals(expected, result);
    }

    static Stream<Arguments> generateData() {
        return Stream.of(
                // Scenario I
                Arguments.of(
                        // GIVEN group of expected size:
                        6,
                        // WHEN clans are:
                        Arrays.asList(
                                new Clan(4, 50),
                                new Clan(2, 70),
                                new Clan(6, 60),
                                new Clan(1, 15),
                                new Clan(5, 40),
                                new Clan(3, 45),
                                new Clan(1, 12),
                                new Clan(4, 40)
                        ),
                        // THEN expect groups as follows:
                        Arrays.asList(
                                Arrays.asList(
                                        new Clan(2, 70),
                                        new Clan(4, 50)
                                ),
                                List.of(
                                        new Clan(6, 60)
                                ),
                                Arrays.asList(
                                        new Clan(3, 45),
                                        new Clan(1, 15),
                                        new Clan(1, 12)
                                ),
                                List.of(
                                        new Clan(4, 40)
                                ),
                                List.of(
                                        new Clan(5, 40)
                                )
                        )
                ),
                // Scenario II
                Arguments.of(
                        // GIVEN group of expected size:
                        8,
                        // WHEN clans are:
                        Arrays.asList(
                                new Clan(1, 40),
                                new Clan(1, 40),
                                new Clan(3, 80),
                                new Clan(5, 5),
                                new Clan(4, 40),
                                new Clan(10, 100)
                        ),
                        // THEN expect groups as follows:
                        Arrays.asList(
                                Arrays.asList(
                                        new Clan(3, 80),
                                        new Clan(1, 40),
                                        new Clan(1, 40)
                                ),
                                List.of(
                                        new Clan(4, 40)
                                ),
                                List.of(
                                        new Clan(5, 5)
                                )
                        )
                ),
                // Scenario III
                Arguments.of(
                        // GIVEN group of expected size:
                        3,
                        // WHEN clans are:
                        Arrays.asList(
                                new Clan(8, 40),
                                new Clan(1, 12),
                                new Clan(1, 30),
                                new Clan(2, 20),
                                new Clan(5, 400),
                                new Clan(3, 15)
                        ),
                        // THEN expect groups as follows:
                        Arrays.asList(
                                Arrays.asList(
                                        new Clan(1, 30),
                                        new Clan(2, 20)
                                ),
                                List.of(
                                        new Clan(3, 15)
                                ),
                                List.of(
                                        new Clan(1, 12)
                                )
                        )
                ),
                // Scenario IV
                Arguments.of(
                        // GIVEN group of expected size:
                        5,
                        // WHEN clans are:
                        Arrays.asList(
                                new Clan(5, 40),
                                new Clan(5, 40),
                                new Clan(5, 40)
                        ),
                        // THEN expect groups as follows:
                        Arrays.asList(
                                List.of(
                                        new Clan(5, 40)
                                ),
                                List.of(
                                        new Clan(5, 40)
                                ),
                                List.of(
                                        new Clan(5, 40)
                                )
                        )
                ),
                // Scenario V
                Arguments.of(
                        // GIVEN group of expected size:
                        3,
                        // WHEN clans are:
                        Arrays.asList(
                                new Clan(20, 33),
                                new Clan(25, 50),
                                new Clan(4, 90)
                        ),
                        // THEN expect groups as follows:
                        Collections.EMPTY_LIST
                ),
                // Scenario VI
                Arguments.of(
                        // GIVEN group of expected size:
                        4,
                        // WHEN clans are:
                        Arrays.asList(
                                new Clan(20, 33),
                                new Clan(25, 50),
                                new Clan(4, 90)
                        ),
                        // THEN expect groups as follows:
                        List.of(
                                List.of(
                                        new Clan(4, 90)
                                )
                        )
                ),
                // Scenario VII
                Arguments.of(
                        // GIVEN group of expected size:
                        4,
                        // WHEN clans are:
                        Collections.EMPTY_LIST,
                        // THEN expect groups as follows:
                        Collections.EMPTY_LIST
                ),
                // Scenario VIII
                Arguments.of(
                        // GIVEN group of expected size:
                        4,
                        // WHEN clans are:
                        null,
                        // THEN expect groups as follows:
                        Collections.EMPTY_LIST
                ),
                // Scenario IX
                Arguments.of(
                        // GIVEN group of expected size:
                        4,
                        // WHEN clans are:
                        Arrays.asList(
                                new Clan(4, 0),
                                new Clan(3, 0),
                                new Clan(2, 0)
                        ),
                        // THEN expect groups as follows:
                        List.of(
                                List.of(
                                        new Clan(2, 0)
                                ),
                                List.of(
                                        new Clan(3, 0)
                                ),
                                List.of(
                                        new Clan(4, 0)
                                )
                        )
                ),
                // Scenario X
                Arguments.of(
                        // GIVEN group of expected size:
                        5,
                        // WHEN clans are:
                        Arrays.asList(
                                new Clan(4, 80),
                                new Clan(5, 90),
                                new Clan(3, 20),
                                new Clan(1, 10),
                                new Clan(1, 10),
                                new Clan(2, 20),
                                new Clan(3, 5),
                                new Clan(10, 4),
                                new Clan(5, 2)
                        ),
                        // THEN expect groups as follows:
                        List.of(
                                List.of(
                                        new Clan(5, 90)
                                ),
                                Arrays.asList(
                                        new Clan(4, 80),
                                        new Clan(1, 10)
                                ),
                                Arrays.asList(
                                        new Clan(2, 20),
                                        new Clan(3, 20)
                                ),
                                Arrays.asList(
                                        new Clan(1, 10),
                                        new Clan(3, 5)
                                ),
                                List.of(
                                        new Clan(5, 2)
                                )
                        )
                ),
                // Scenario XI
                Arguments.of(
                        // GIVEN group of expected size:
                        6,
                        // WHEN clans are:
                        Arrays.asList(
                                new Clan(3, 30),
                                new Clan(4, 40),
                                new Clan(2, 60),
                                new Clan(6, 55),
                                new Clan(1, 15),
                                new Clan(3, 40),
                                new Clan(3, 39),
                                new Clan(1, 12),
                                new Clan(6, 50),
                                new Clan(2, 5),
                                new Clan(5, 40),
                                new Clan(6, 2)
                        ),
                        // THEN expect groups as follows:
                        List.of(
                                Arrays.asList(
                                        new Clan(2, 60),
                                        new Clan(4, 40)
                                ),
                                List.of(
                                        new Clan(6, 55)
                                ),
                                List.of(
                                        new Clan(6, 50)
                                ),
                                Arrays.asList(
                                        new Clan(3, 40),
                                        new Clan(3, 39)
                                ),
                                Arrays.asList(
                                        new Clan(5, 40),
                                        new Clan(1, 15)
                                ),
                                Arrays.asList(
                                        new Clan(3, 30),
                                        new Clan(1, 12),
                                        new Clan(2, 5)
                                ),
                                List.of(
                                        new Clan(6, 2)
                                )
                        )
                )
        );
    }
}
