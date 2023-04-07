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

@Singleton
public class OnlineGameServiceImpl implements OnlineGameService {
    @Override
    public Collection<Collection<Clan>> calculateGroups(Players players) {
        var result = new ArrayList<Collection<Clan>>();
        if (players.clans() == null || players.clans().isEmpty()) return result;

        var maxGroupSize = players.groupCount();
        var clansTreeMap = new TreeMap<Integer, PriorityQueue<ClanVisitor>>(Comparator.reverseOrder());
        var clansTreeSet = new TreeSet<ClanVisitor>();
        for (Clan clan : players.clans()) {
            ClanVisitor clanVisitor = new ClanVisitor(clan);
            if (clanVisitor.getNumberOfPlayers() > maxGroupSize) continue;
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

        var tempGroup = new ArrayList<Clan>();
        var tempGroupSize = 0;
        var skippedClans = new Stack<ClanVisitor>();
        for (ClanVisitor clanVisitor : clansTreeSet) {
            if (clanVisitor.isVisited()) continue;

            while (!skippedClans.isEmpty() && clanFitsIn(skippedClans.peek(), tempGroupSize, maxGroupSize)) {
                ClanVisitor anotherClanVisitor = skippedClans.pop();
                tempGroupSize += anotherClanVisitor.getNumberOfPlayers();
                tempGroup.add(anotherClanVisitor.getClan());
                anotherClanVisitor.markVisited();
            }

            var clan = clanVisitor.getClan();
            if (clanFitsIn(clanVisitor, tempGroupSize, maxGroupSize)) {
                tempGroup.add(clan);
                tempGroupSize += clan.numberOfPlayers();
                clanVisitor.markVisited();
            } else {
                if (!clanVisitor.isVisited()) {
                    skippedClans.add(clanVisitor);
                }

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

                if (!tempGroup.isEmpty()) {
                    result.add(tempGroup);
                    tempGroup = new ArrayList<>();
                    tempGroupSize = 0;
                }
            }
        }

        result.addAll(getOrphanClans(maxGroupSize, tempGroup, skippedClans));
        return result;
    }

    private ClanVisitor findWeakerClanVisitor(int freeSpace, TreeMap<Integer, PriorityQueue<ClanVisitor>> treeMap) {
        for (Map.Entry<Integer, PriorityQueue<ClanVisitor>> weakerClans :
                treeMap.tailMap(freeSpace, true).entrySet()) {
            var weakerClansQueue = weakerClans.getValue();
            var weakerClanVisitor = weakerClansQueue.peek();
            if (weakerClanVisitor != null && freeSpace >= weakerClanVisitor.getNumberOfPlayers()) {
                return weakerClansQueue.poll();
            }
        }
        return null;
    }

    private boolean clanFitsIn(ClanVisitor clanVisitor, int groupSize, int maxGroupSize) {
        return clanVisitor.getNumberOfPlayers() + groupSize <= maxGroupSize;
    }

    private Collection<Collection<Clan>> getOrphanClans(
            int maxGroupSize, ArrayList<Clan> tempGroup, Stack<ClanVisitor> skippedClans) {
        var result = new ArrayList<Collection<Clan>>();

        var lastTempGroup = new ArrayList<Clan>();
        var lastTempGroupSize = 0;
        for (ClanVisitor clanVisitor : skippedClans) {
            var clan = clanVisitor.getClan();
            if (clanFitsIn(clanVisitor, lastTempGroupSize, maxGroupSize)) {
                lastTempGroup.add(clan);
                lastTempGroupSize += clan.numberOfPlayers();
            }
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
