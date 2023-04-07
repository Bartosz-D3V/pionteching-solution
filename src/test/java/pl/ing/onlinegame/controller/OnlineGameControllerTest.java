package pl.ing.onlinegame.controller;

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
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import pl.ing.onlinegame.domain.Clan;
import pl.ing.onlinegame.domain.Players;

@MicronautTest
class OnlineGameControllerTest {
    @Inject
    @Client("/onlinegame")
    private HttpClient client;

    @ParameterizedTest
    @MethodSource("pl.ing.onlinegame.TestScenarioDataProvider#generateData")
    void calculateGroupsListOfClanGroups(int maxPlayers, Collection<Clan> clans, Collection<Collection<Clan>> expected) {
        var players = new Players(maxPlayers, clans);

        var request = HttpRequest.POST("/calculate", players);
        var response = client.toBlocking().exchange(request.contentType(MediaType.APPLICATION_JSON_TYPE),
                Argument.listOf(Argument.listOf(Clan.class)));

        assertEquals(HttpStatus.OK, response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().get(HttpHeaders.CONTENT_TYPE));

        var result = response.body();
        assertNotNull(result);
        assertEquals(expected, result);
    }
}
