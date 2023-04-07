package pl.ing.onlinegame.domain;

import java.util.Collection;

public record Players(int groupCount, Collection<Clan> clans) {}
