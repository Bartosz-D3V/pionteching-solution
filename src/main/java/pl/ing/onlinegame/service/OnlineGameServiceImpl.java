package pl.ing.onlinegame.service;

import jakarta.inject.Singleton;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Stack;
import java.util.TreeMap;
import java.util.TreeSet;
import pl.ing.onlinegame.domain.Clan;
import pl.ing.onlinegame.domain.ClanVisitor;
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
        var clansTreeSet = groupedClans.second();

        var tempGroup = new ArrayList<Clan>();
        var tempGroupSize = 0;
        var skippedClans = new Stack<ClanVisitor>();
        for (ClanVisitor clanVisitor : clansTreeSet) {
            if (clanVisitor.isVisited()) continue;

            while (!skippedClans.isEmpty()) {
                ClanVisitor anotherClanVisitor = skippedClans.pop();
                tempGroup.add(anotherClanVisitor.getClan());
                tempGroupSize += anotherClanVisitor.getNumberOfPlayers();
                anotherClanVisitor.markVisited();
            }

            var clan = clanVisitor.getClan();
            if (clanFitsIn(clanVisitor, tempGroupSize, maxGroupSize)) {
                tempGroup.add(clan);
                tempGroupSize += clan.numberOfPlayers();
                clanVisitor.markVisited();
            } else {
                skippedClans.add(clanVisitor);

                ClanVisitor weakerClanVisitor = findWeakerClanVisitor(maxGroupSize - tempGroupSize, clansTreeMap);
                while (weakerClanVisitor != null && weakerClanVisitor.isVisited()) {
                    weakerClanVisitor = findWeakerClanVisitor(maxGroupSize - tempGroupSize, clansTreeMap);
                }
                while (weakerClanVisitor != null) {
                    tempGroup.add(weakerClanVisitor.getClan());
                    tempGroupSize += weakerClanVisitor.getNumberOfPlayers();
                    weakerClanVisitor.markVisited();
                    weakerClanVisitor = findWeakerClanVisitor(maxGroupSize - tempGroupSize, clansTreeMap);
                }

                result.add(tempGroup);
                tempGroup = new ArrayList<>();
                tempGroupSize = 0;
            }
        }

        result.addAll(getOrphanClans(tempGroup, skippedClans));
        return result;
    }

    private Pair<TreeMap<Integer, PriorityQueue<ClanVisitor>>, TreeSet<ClanVisitor>> groupClans(Players players) {
        var clansTreeMap = new TreeMap<Integer, PriorityQueue<ClanVisitor>>(Comparator.reverseOrder());
        var clansTreeSet = new TreeSet<ClanVisitor>();
        for (Clan clan : players.clans()) {
            ClanVisitor clanVisitor = new ClanVisitor(clan);
            if (clanVisitor.getNumberOfPlayers() > players.groupCount()) continue;
            clansTreeSet.add(clanVisitor);
            clansTreeMap.compute(clan.numberOfPlayers(), (key, value) -> {
                if (value == null) {
                    var pq = new PriorityQueue<ClanVisitor>();
                    pq.add(clanVisitor);
                    return pq;
                } else {
                    value.add(clanVisitor);
                    return value;
                }
            });
        }
        return new Pair<>(clansTreeMap, clansTreeSet);
    }

    private ClanVisitor findWeakerClanVisitor(int freeSpace, TreeMap<Integer, PriorityQueue<ClanVisitor>> treeMap) {
        var maxPoints = 0;
        Integer key = null;
        for (Map.Entry<Integer, PriorityQueue<ClanVisitor>> weakerClans :
                treeMap.tailMap(freeSpace, true).entrySet()) {
            var weakerClansQueue = weakerClans.getValue();
            var weakerClanVisitor = weakerClansQueue.peek();

            if (weakerClanVisitor != null) {
                int numberOfPlayersInWeakerClan = weakerClanVisitor.getNumberOfPlayers();
                int pointsInWeakerClan = weakerClanVisitor.getClan().points();
                if (pointsInWeakerClan >= maxPoints) {
                    key = numberOfPlayersInWeakerClan;
                    maxPoints = pointsInWeakerClan;
                }
            }
        }
        return key != null ? treeMap.get(key).poll() : null;
    }

    private boolean clanFitsIn(ClanVisitor clanVisitor, int groupSize, int maxGroupSize) {
        return clanVisitor.getNumberOfPlayers() + groupSize <= maxGroupSize;
    }

    private Collection<Collection<Clan>> getOrphanClans(ArrayList<Clan> tempGroup, Stack<ClanVisitor> skippedClans) {
        var result = new ArrayList<Collection<Clan>>();

        var lastTempGroup = new ArrayList<Clan>();
        for (ClanVisitor clanVisitor : skippedClans) {
            var clan = clanVisitor.getClan();
            lastTempGroup.add(clan);
        }

        if (!tempGroup.isEmpty()) {
            result.add(tempGroup);
        }
        if (!lastTempGroup.isEmpty()) {
            result.add(lastTempGroup);
        }

        return result;
    }
}
