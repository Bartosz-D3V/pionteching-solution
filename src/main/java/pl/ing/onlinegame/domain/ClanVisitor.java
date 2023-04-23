package pl.ing.onlinegame.domain;

import java.util.Objects;
import java.util.UUID;

public class ClanVisitor implements Comparable<ClanVisitor> {
    private final Clan clan;
    private final UUID id;
    private boolean visited;

    public ClanVisitor(Clan clan) {
        this.clan = clan;
        this.visited = false;
        this.id = UUID.randomUUID();
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
        var clanCompare = clan.compareTo(clanVisitor.getClan());
        if (clanCompare == 0) {
            return id.compareTo(clanVisitor.id);
        }
        return clanCompare;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClanVisitor that = (ClanVisitor) o;
        return visited == that.visited && clan.equals(that.clan) && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(clan, id, visited);
    }
}
