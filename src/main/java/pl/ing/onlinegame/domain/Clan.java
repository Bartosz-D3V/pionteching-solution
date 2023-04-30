package pl.ing.onlinegame.domain;

public record Clan(int numberOfPlayers, int points) implements Comparable<Clan> {
    @Override
    public int compareTo(Clan clan) {
        var pointsComparator = -1 * Integer.compare(points, clan.points);
        return pointsComparator == 0 ? Integer.compare(numberOfPlayers, clan.numberOfPlayers) : pointsComparator;
    }
}
