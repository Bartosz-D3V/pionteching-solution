package pl.ing.onlinegame.domain;

import java.util.Objects;
import java.util.UUID;

public class ClanVisitor implements Comparable<ClanVisitor> {
    private final Clan clan;
    private boolean visited;

    public ClanVisitor(Clan clan) {
        this.clan = clan;
        this.visited = false;
    }

    public Clan getClan() {
        return clan;
    }

    public boolean isVisited() {
        return visited;
    }

    public void markVisited() {
        this.visited = true;
    }

    public int getNumberOfPlayers() {
        return clan.numberOfPlayers();
    }

    @Override
    public int compareTo(ClanVisitor clanVisitor) {
        return clan.compareTo(clanVisitor.getClan());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClanVisitor that = (ClanVisitor) o;
        return visited == that.visited && Objects.equals(clan, that.clan);
    }

    @Override
    public int hashCode() {
        return Objects.hash(clan, visited);
    }
}
