package pl.ing.onlinegame.service;

import jakarta.inject.Singleton;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Deque;
import java.util.List;
import java.util.PriorityQueue;
import java.util.TreeMap;
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
        var clanVisitorsTreeMap = groupedClans.first();
        var clanVisitorsSorted = groupedClans.second();

        var tempGroup = new ArrayList<Clan>();
        var tempGroupSize = 0;
        var skippedClans = new ArrayDeque<ClanVisitor>();
        for (ClanVisitor clanVisitor : clanVisitorsSorted) {
            if (clanVisitor.isVisited()) continue;

            while (!skippedClans.isEmpty()) {
                var skippedClanVisitor = skippedClans.pop();
                tempGroup.add(skippedClanVisitor.getClan());
                tempGroupSize += skippedClanVisitor.getNumberOfPlayers();
                skippedClanVisitor.markVisited();
            }

            var clan = clanVisitor.getClan();
            if (clanFitsIn(clanVisitor, tempGroupSize, maxGroupSize)) {
                tempGroup.add(clan);
                tempGroupSize += clan.numberOfPlayers();
                clanVisitor.markVisited();
            } else {
                skippedClans.add(clanVisitor);

                ClanVisitor weakerClanVisitor = findWeakerClanVisitor(maxGroupSize - tempGroupSize, clanVisitorsTreeMap);
                while (weakerClanVisitor != null && weakerClanVisitor.isVisited()) {
                    weakerClanVisitor = findWeakerClanVisitor(maxGroupSize - tempGroupSize, clanVisitorsTreeMap);
                }
                while (weakerClanVisitor != null) {
                    tempGroup.add(weakerClanVisitor.getClan());
                    tempGroupSize += weakerClanVisitor.getNumberOfPlayers();
                    weakerClanVisitor.markVisited();

                    weakerClanVisitor = findWeakerClanVisitor(maxGroupSize - tempGroupSize, clanVisitorsTreeMap);
                }

                result.add(tempGroup);
                tempGroup = new ArrayList<>();
                tempGroupSize = 0;
            }
        }

        result.addAll(getOrphanClans(tempGroup, skippedClans));
        return result;
    }

    private static Pair<TreeMap<Integer, PriorityQueue<ClanVisitor>>, List<ClanVisitor>> groupClans(Players players) {
        var clanVisitorsTreeMap = new TreeMap<Integer, PriorityQueue<ClanVisitor>>(Comparator.reverseOrder());
        var clanVisitorsSorted = new ArrayList<ClanVisitor>();
        for (Clan clan : players.clans()) {
            if (clan.numberOfPlayers() > players.groupCount()) continue;

            var clanVisitor = new ClanVisitor(clan);
            clanVisitorsSorted.add(clanVisitor);

            clanVisitorsTreeMap.compute(clan.numberOfPlayers(), (key, value) -> {
                if (value != null) {
                    value.add(clanVisitor);
                    return value;
                }
                return new PriorityQueue<>(Collections.singletonList(clanVisitor));
            });
        }
        clanVisitorsSorted.sort(ClanVisitor::compareTo);

        return new Pair<>(clanVisitorsTreeMap, clanVisitorsSorted);
    }

    private static ClanVisitor findWeakerClanVisitor(
            int freeSpace, TreeMap<Integer, PriorityQueue<ClanVisitor>> treeMap) {
        var maxPoints = 0;
        Integer key = null;
        for (PriorityQueue<ClanVisitor> weakerClansQueue :
                treeMap.tailMap(freeSpace, true).values()) {
            var weakerClanVisitor = weakerClansQueue.peek();

            if (weakerClanVisitor == null) continue;

            int pointsInWeakerClan = weakerClanVisitor.getClan().points();
            if (pointsInWeakerClan >= maxPoints) {
                key = weakerClanVisitor.getNumberOfPlayers();
                maxPoints = pointsInWeakerClan;
            }
        }
        return key != null ? treeMap.get(key).poll() : null;
    }

    private static boolean clanFitsIn(ClanVisitor clanVisitor, int groupSize, int maxGroupSize) {
        return clanVisitor.getNumberOfPlayers() + groupSize <= maxGroupSize;
    }

    private static Collection<Collection<Clan>> getOrphanClans(
            ArrayList<Clan> tempGroup, Deque<ClanVisitor> skippedClans) {
        var result = new ArrayList<Collection<Clan>>();

        var lastTempGroup = new ArrayList<Clan>();
        for (ClanVisitor clanVisitor : skippedClans) {
            lastTempGroup.add(clanVisitor.getClan());
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
