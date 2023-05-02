package pl.ing.onlinegame.service;

import jakarta.inject.Singleton;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Deque;
import java.util.PriorityQueue;
import java.util.TreeMap;
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
        var clansTreeMap = groupedClans.first();
        var clansSorted = groupedClans.second();

        var tempGroup = new ArrayList<Clan>();
        var tempGroupSize = 0;
        var skippedClans = new ArrayDeque<Clan>();
        for (Clan clan : clansSorted) {
            if (clan.isVisited()) continue;

            while (!skippedClans.isEmpty()) {
                var skippedClan = skippedClans.pop();
                tempGroup.add(skippedClan);
                tempGroupSize += skippedClan.getNumberOfPlayers();
                skippedClan.markVisited();
            }

            if (clanFitsIn(clan, tempGroupSize, maxGroupSize)) {
                tempGroup.add(clan);
                tempGroupSize += clan.getNumberOfPlayers();
                clan.markVisited();
            } else {
                skippedClans.add(clan);

                Clan weakerClan = findWeakerClan(maxGroupSize - tempGroupSize, clansTreeMap);
                while (weakerClan != null && weakerClan.isVisited()) {
                    weakerClan = findWeakerClan(maxGroupSize - tempGroupSize, clansTreeMap);
                }
                while (weakerClan != null) {
                    tempGroup.add(weakerClan);
                    tempGroupSize += weakerClan.getNumberOfPlayers();
                    weakerClan.markVisited();

                    weakerClan = findWeakerClan(maxGroupSize - tempGroupSize, clansTreeMap);
                }

                result.add(tempGroup);
                tempGroup = new ArrayList<>();
                tempGroupSize = 0;
            }
        }

        result.addAll(getOrphanClans(tempGroup, skippedClans));
        return result;
    }

    private static Pair<TreeMap<Integer, PriorityQueue<Clan>>, Collection<Clan>> groupClans(Players players) {
        var clansTreeMap = new TreeMap<Integer, PriorityQueue<Clan>>(Comparator.reverseOrder());
        var clansSorted = new ArrayList<Clan>();

        for (Clan clan : players.clans()) {
            if (clan.getNumberOfPlayers() > players.groupCount()) continue;

            clansSorted.add(clan);
            clansTreeMap.compute(clan.getNumberOfPlayers(), (key, value) -> {
                if (value != null) {
                    value.add(clan);
                    return value;
                }
                return new PriorityQueue<>(Collections.singletonList(clan));
            });
        }
        clansSorted.sort(Clan::compareTo);

        return new Pair<>(clansTreeMap, clansSorted);
    }

    private static Clan findWeakerClan(int freeSpace, TreeMap<Integer, PriorityQueue<Clan>> treeMap) {
        var maxPoints = 0;
        Integer key = null;
        for (PriorityQueue<Clan> weakerClansQueue :
                treeMap.tailMap(freeSpace, true).values()) {
            var weakerClan = weakerClansQueue.peek();

            if (weakerClan == null) continue;

            int pointsInWeakerClan = weakerClan.getPoints();
            if (pointsInWeakerClan >= maxPoints) {
                key = weakerClan.getNumberOfPlayers();
                maxPoints = pointsInWeakerClan;
            }
        }
        return key != null ? treeMap.get(key).poll() : null;
    }

    private static boolean clanFitsIn(Clan clanVisitor, int groupSize, int maxGroupSize) {
        return clanVisitor.getNumberOfPlayers() + groupSize <= maxGroupSize;
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
