package pl.ing.onlinegame;

import org.junit.jupiter.params.provider.Arguments;
import pl.ing.onlinegame.domain.Clan;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

public class TestScenarioDataProvider {
    public static Stream<Arguments> generateData() {
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
                                        new Clan(3, 40),
                                        new Clan(1, 15)
                                ),
                                List.of(
                                        new Clan(6, 55)
                                ),
                                List.of(
                                        new Clan(6, 50)
                                ),
                                Arrays.asList(
                                        new Clan(4, 40),
                                        new Clan(1, 12)
                                ),
                                List.of(
                                        new Clan(5, 40)
                                ),
                                Arrays.asList(
                                        new Clan(3, 39),
                                        new Clan(3, 30)
                                ),
                                List.of(
                                        new Clan(2, 5)
                                ),
                                List.of(
                                        new Clan(6, 2)
                                )
                        )
                ),
                // Scenario XII
                Arguments.of(
                        // GIVEN group of expected size:
                        7,
                        // WHEN clans are:
                        Arrays.asList(
                                new Clan(6, 10),
                                new Clan(2, 10),
                                new Clan(1, 15),
                                new Clan(2, 30),
                                new Clan(2, 100),
                                new Clan(7, 100),
                                new Clan(2, 10),
                                new Clan(5, 15),
                                new Clan(4, 40),
                                new Clan(3, 40),
                                new Clan(4, 40),
                                new Clan(1, 5),
                                new Clan(3, 3),
                                new Clan(1, 1)
                        ),
                        // THEN expect groups as follows:
                        List.of(
                                Arrays.asList(
                                        new Clan(2, 100),
                                        new Clan(3, 40),
                                        new Clan(2, 30)
                                ),
                                List.of(
                                        new Clan(7, 100)
                                ),
                                Arrays.asList(
                                        new Clan(4, 40),
                                        new Clan(1, 15),
                                        new Clan(2, 10)
                                ),
                                Arrays.asList(
                                        new Clan(4, 40),
                                        new Clan(2, 10),
                                        new Clan(1, 5)
                                ),
                                Arrays.asList(
                                        new Clan(5, 15),
                                        new Clan(1, 1)
                                ),
                                List.of(
                                        new Clan(6, 10)
                                ),
                                List.of(
                                        new Clan(3, 3)
                                )
                        )
                )
        );
    }
}
