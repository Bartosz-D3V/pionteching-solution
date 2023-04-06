package pl.ing.onlinegame.domain;

import java.util.Comparator;

public record Clan(int numberOfPlayers, int points) implements Comparable<Clan> {
	@Override
	public int compareTo(Clan clan) {
		return Comparator
				.comparingInt(Clan::points)
				.reversed()
				.thenComparingInt((Clan::numberOfPlayers))
				.compare(this, clan);
	}
}
