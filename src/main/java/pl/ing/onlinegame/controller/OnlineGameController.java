package pl.ing.onlinegame.controller;

import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Consumes;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Produces;
import java.util.Collection;
import pl.ing.onlinegame.domain.Clan;
import pl.ing.onlinegame.domain.Players;
import pl.ing.onlinegame.service.OnlineGameService;

@Controller("/onlinegame")
public class OnlineGameController {
    private final OnlineGameService onlineGameService;

    public OnlineGameController(OnlineGameService onlineGameService) {
        this.onlineGameService = onlineGameService;
    }

    @Post("/calculate")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<Collection<Clan>> calculateGroups(@Body Players players) {
        return onlineGameService.calculateGroups(players);
    }
}
