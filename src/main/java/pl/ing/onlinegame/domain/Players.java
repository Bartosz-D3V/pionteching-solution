package pl.ing.onlinegame.domain;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

public record Players(int groupCount, Collection<Clan> clans) {}
