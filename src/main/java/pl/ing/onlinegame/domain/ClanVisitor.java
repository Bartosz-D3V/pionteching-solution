package pl.ing.onlinegame.domain;

import java.util.Objects;
import java.util.UUID;

public class ClanVisitor implements Comparable<ClanVisitor> {
    private final Clan clan;
    private boolean visited;
    private final UUID id;

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

    public void setVisited(boolean visited) {
        this.visited = visited;
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
        return id.equals(((ClanVisitor) o).id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(clan, visited, id);
    }
}
