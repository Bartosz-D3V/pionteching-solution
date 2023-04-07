package pl.ing.onlinegame.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Collection;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import pl.ing.onlinegame.domain.Clan;
import pl.ing.onlinegame.domain.Players;

class OnlineGameServiceTest {
    private final OnlineGameService onlineGameService = new OnlineGameServiceImpl();

    @ParameterizedTest
    @MethodSource("pl.ing.onlinegame.TestScenarioDataProvider#generateData")
    void calculateGroupsShouldFilterOurClansByPlayersCount(
            int maxPlayers, Collection<Clan> clans, Collection<Collection<Clan>> expected) {
        final Collection<Collection<Clan>> result = onlineGameService.calculateGroups(new Players(maxPlayers, clans));

        assertNotNull(result);
        assertEquals(expected, result);
    }
}
