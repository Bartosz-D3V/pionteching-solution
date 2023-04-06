package pl.ing.onlinegame.service;

import java.util.Collection;
import pl.ing.onlinegame.domain.Clan;
import pl.ing.onlinegame.domain.Players;

public interface OnlineGameService {
	Collection<Collection<Clan>> calculateGroups(Players players);
}
