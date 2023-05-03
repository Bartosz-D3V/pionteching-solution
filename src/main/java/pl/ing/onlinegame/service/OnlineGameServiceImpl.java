package pl.ing.onlinegame.service;

import jakarta.inject.Singleton;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.PriorityQueue;
import pl.ing.onlinegame.domain.Clan;
import pl.ing.onlinegame.domain.Players;
import pl.ing.util.Pair;

@Singleton
public class OnlineGameServiceImpl implements OnlineGameService {
    @Override
    public Collection<Collection<Clan>> calculateGroups(Players players) {
        var result = new ArrayList<Collection<Clan>>();
        if (players.clans() == null || players.clans().isEmpty()) return result;

        var maxGroupSize = players.groupCount();
        var groupedClans = groupClans(players);
        var clansMap = groupedClans.first();
        var clansSorted = groupedClans.second();

        var tempGroup = new ArrayList<Clan>();
        var tempGroupSize = 0;
        var skippedClans = new ArrayDeque<Clan>();
        for (Clan clan : clansSorted) {
            if (clan.isVisited()) continue;

            tempGroupSize = takeSkippedClans(tempGroup, tempGroupSize, skippedClans);

            if (clanFitsIn(clan, tempGroupSize, maxGroupSize)) {
                tempGroupSize = addClanToGroup(tempGroup, tempGroupSize, clan);
            } else {
                skippedClans.add(clan);

                Clan weakerClan = findWeakerClan(maxGroupSize - tempGroupSize, clansMap);
                while (weakerClan != null) {
                    tempGroupSize = addClanToGroup(tempGroup, tempGroupSize, weakerClan);

                    weakerClan = findWeakerClan(maxGroupSize - tempGroupSize, clansMap);
                }

                result.add(tempGroup);
                tempGroup = new ArrayList<>();
                tempGroupSize = 0;
            }
        }

        result.addAll(getOrphanClans(tempGroup, skippedClans));
        return result;
    }

    private static int takeSkippedClans(Collection<Clan> tempGroup, int tempGroupSize, Deque<Clan> skippedClans) {
        while (!skippedClans.isEmpty()) {
            var skippedClan = skippedClans.pop();
            tempGroupSize = addClanToGroup(tempGroup, tempGroupSize, skippedClan);
        }
        return tempGroupSize;
    }

    private static int addClanToGroup(Collection<Clan> tempGroup, int tempGroupSize, Clan clan) {
        tempGroup.add(clan);
        tempGroupSize += clan.getNumberOfPlayers();
        clan.markVisited();
        return tempGroupSize;
    }

    private static Pair<PriorityQueue<Clan>[], Collection<Clan>> groupClans(Players players) {
        @SuppressWarnings("unchecked")
        var clansMap = (PriorityQueue<Clan>[]) new PriorityQueue<?>[players.groupCount() + 1];
        var clansSorted = new ArrayList<Clan>();

        for (Clan clan : players.clans()) {
            if (clan.getNumberOfPlayers() > players.groupCount()) continue;

            clansSorted.add(clan);

            PriorityQueue<Clan> pq = clansMap[clan.getNumberOfPlayers()];
            if (pq != null) {
                pq.add(clan);
                clansMap[clan.getNumberOfPlayers()] = pq;
            } else {
                clansMap[clan.getNumberOfPlayers()] = new PriorityQueue<>(Collections.singletonList(clan));
            }
        }
        clansSorted.sort(Clan::compareTo);

        return new Pair<>(clansMap, clansSorted);
    }

    private static Clan findWeakerClan(int freeSpace, PriorityQueue<Clan>[] clansMap) {
        var maxPoints = 0;
        var maxKey = -1;
        for (int i = freeSpace; i >= 0; i--) {
            PriorityQueue<Clan> weakerClansQueue = clansMap[i];

            if (weakerClansQueue == null || weakerClansQueue.peek() == null) continue;
            Clan weakerClan = weakerClansQueue.peek();

            int pointsInWeakerClan = weakerClan.getPoints();
            if (pointsInWeakerClan >= maxPoints) {
                if (!weakerClan.isVisited()) {
                    maxKey = weakerClan.getNumberOfPlayers();
                    maxPoints = pointsInWeakerClan;
                } else weakerClansQueue.poll();
            }
        }

        return maxKey == -1 ? null : clansMap[maxKey].poll();
    }

    private static boolean clanFitsIn(Clan clan, int groupSize, int maxGroupSize) {
        return clan.getNumberOfPlayers() + groupSize <= maxGroupSize;
    }

    private static Collection<Collection<Clan>> getOrphanClans(ArrayList<Clan> tempGroup, Deque<Clan> skippedClans) {
        var result = new ArrayList<Collection<Clan>>();

        var lastTempGroup = new ArrayList<>(skippedClans);

        if (!tempGroup.isEmpty()) {
            result.add(tempGroup);
        }
        if (!lastTempGroup.isEmpty()) {
            result.add(lastTempGroup);
        }

        return result;
    }
}
