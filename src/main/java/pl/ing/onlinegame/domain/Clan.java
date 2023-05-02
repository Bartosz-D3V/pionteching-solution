package pl.ing.onlinegame.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Objects;

public class Clan implements Comparable<Clan> {
    private int numberOfPlayers;
    private int points;

    @JsonIgnore
    private boolean visited;

    public Clan() {
        this.visited = false;
    }

    public Clan(int numberOfPlayers, int points) {
        this.numberOfPlayers = numberOfPlayers;
        this.points = points;
        this.visited = false;
    }

    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    public void setNumberOfPlayers(int numberOfPlayers) {
        this.numberOfPlayers = numberOfPlayers;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public boolean isVisited() {
        return visited;
    }

    public void markVisited() {
        this.visited = true;
    }

    @Override
    public int compareTo(Clan clan) {
        var pointsComparator = -1 * Integer.compare(points, clan.points);
        return pointsComparator == 0 ? Integer.compare(numberOfPlayers, clan.numberOfPlayers) : pointsComparator;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Clan clan = (Clan) o;
        return numberOfPlayers == clan.numberOfPlayers && points == clan.points;
    }

    @Override
    public int hashCode() {
        return Objects.hash(numberOfPlayers, points);
    }
}
