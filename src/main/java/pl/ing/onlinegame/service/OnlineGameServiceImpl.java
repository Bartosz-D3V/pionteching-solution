package pl.ing.onlinegame.service;

import jakarta.inject.Singleton;
import pl.ing.onlinegame.domain.Clan;
import pl.ing.onlinegame.domain.ClanVisitor;
import pl.ing.onlinegame.domain.Players;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.PriorityBlockingQueue;

@Singleton
public class OnlineGameServiceImpl implements OnlineGameService {
//    public Object calculateGroupsTest(Players players) {
//        var maxGroupSize = players.groupCount();
//        var treeMap = new TreeMap<Integer, PriorityQueue<Clan>>((integer, t1) ->-1* Integer.compare(integer, t1));
//        var sortedQueue = new ConcurrentSkipListSet<Clan>();
//        var result = new ArrayList<ArrayList<Clan>>();
//        for (Clan clan : players.clans()) {
//            sortedQueue.add(clan);
//            treeMap.compute(clan.numberOfPlayers(), (key, value) -> {
//                if (value == null) {
//                    var pq = new PriorityQueue<Clan>();
//                    pq.add(clan);
//                    return pq;
//                } else {
//                    value.add(clan);
//                    return value;
//                }
//            });
//        }
//
//        var tempGroupSize = 0;
//        var tempGroup = new ArrayList<Clan>();
//        for (Clan clan : sortedQueue) {
//            if (clan.numberOfPlayers() + tempGroupSize <= maxGroupSize) {
//                tempGroup.add(clan);
//                tempGroupSize += clan.numberOfPlayers();
//                sortedQueue.remove(clan);
//            } else {
//                Clan weakerClan = findWeakerClan( maxGroupSize- tempGroupSize, treeMap);
//                while (weakerClan != null) {
//                    tempGroup.add(weakerClan);
//                    tempGroupSize+=weakerClan.numberOfPlayers();
//                    sortedQueue.remove(weakerClan);
//                    weakerClan = findWeakerClan( maxGroupSize- tempGroupSize, treeMap);
//                }
//                if (!tempGroup.isEmpty()) {
//                    result.add(tempGroup);
//                }
//                tempGroup = new ArrayList<>();
//            }
//        }
//
//        return result;
//    }

    public ArrayList<ArrayList<Clan>> calculateGroupsTest(Players players) {
        if (players == null || players.clans() == null || players.clans().isEmpty()) return new ArrayList<>();

        var maxGroupSize = players.groupCount();
        var treeMap = new TreeMap<Integer, PriorityQueue<ClanVisitor>>((integer, t1) ->-1 * Integer.compare(integer, t1));
        var sortedQueue = new TreeSet<ClanVisitor>();
        var anotherSortedQueue = new PriorityQueue<ClanVisitor>();
        var result = new ArrayList<ArrayList<Clan>>();
        for (Clan clan : players.clans()) {
            ClanVisitor clanVisitor = new ClanVisitor(clan);
            if (clanVisitor.getClan().numberOfPlayers() > maxGroupSize) continue;
            sortedQueue.add(clanVisitor);
            treeMap.compute(clan.numberOfPlayers(), (key, value) -> {
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

        var tempGroupSize = 0;
        var tempGroup = new ArrayList<Clan>();
        for (ClanVisitor clanVisitor : sortedQueue) {
            if (clanVisitor.isVisited()) continue;

            while(!anotherSortedQueue.isEmpty() && anotherSortedQueue.peek().getClan().numberOfPlayers() + tempGroupSize <= maxGroupSize) {
                ClanVisitor anotherClanVisitor = anotherSortedQueue.poll();
                tempGroupSize += anotherClanVisitor.getClan().numberOfPlayers();
                tempGroup.add(anotherClanVisitor.getClan());
                anotherClanVisitor.setVisited(true);
            }

            var clan = clanVisitor.getClan();
            if (clan.numberOfPlayers() + tempGroupSize <= maxGroupSize) {
                tempGroup.add(clan);
                tempGroupSize += clan.numberOfPlayers();
                clanVisitor.setVisited(true);
            } else {
                if (!clanVisitor.isVisited()) {
                    anotherSortedQueue.add(clanVisitor);
                }
                ClanVisitor weakerClanVisitor = findWeakerClanVisitor( maxGroupSize- tempGroupSize, treeMap);
                while (weakerClanVisitor != null&&weakerClanVisitor.isVisited()) {
                    weakerClanVisitor = findWeakerClanVisitor( maxGroupSize- tempGroupSize, treeMap);
                }
                while (weakerClanVisitor != null) {
                    tempGroup.add(weakerClanVisitor.getClan());
                    tempGroupSize+=weakerClanVisitor.getClan().numberOfPlayers();
                    weakerClanVisitor.setVisited(true);
                    weakerClanVisitor = findWeakerClanVisitor( maxGroupSize- tempGroupSize, treeMap);
                }


                if (!tempGroup.isEmpty()) {
                    result.add(tempGroup);
                }
                tempGroup = new ArrayList<>();
                tempGroupSize = 0;
            }
        }

        if (!tempGroup.isEmpty()) {
            result.add(tempGroup);
        }

        var lastTempGroupSize = 0;
        var lastTempGroup = new ArrayList<Clan>();
        for (ClanVisitor clanVisitor: anotherSortedQueue) {
            if (clanVisitor.getClan().numberOfPlayers() + lastTempGroupSize <= maxGroupSize){
                lastTempGroup.add(clanVisitor.getClan());
                lastTempGroupSize +=  clanVisitor.getClan().numberOfPlayers();
            } else {
                result.add(lastTempGroup);
                lastTempGroup = new ArrayList<>();
                lastTempGroupSize = 0;
            }
        }
        if (!lastTempGroup.isEmpty()) {
            result.add(lastTempGroup);
        }

        return result;
    }

    public ClanVisitor findWeakerClanVisitor(int freeSpace, TreeMap<Integer, PriorityQueue<ClanVisitor>> treeMap) {
        for (Map.Entry<Integer, PriorityQueue<ClanVisitor>> weakerClans : treeMap.tailMap(freeSpace, true).entrySet()) {
            var weakerClansQueue = weakerClans.getValue();
            var weakerClanVisitor = weakerClansQueue.peek();
            if (weakerClanVisitor != null && freeSpace >= weakerClanVisitor.getClan().numberOfPlayers()) {
                return weakerClansQueue.poll();
            }
        }
        return null;
    }
    public Clan findWeakerClan(int freeSpace, TreeMap<Integer, PriorityQueue<Clan>> treeMap) {
        for (Map.Entry<Integer, PriorityQueue<Clan>> weakerClans : treeMap.tailMap(freeSpace, true).entrySet()) {
            var weakerClansQueue = weakerClans.getValue();
            var weakerClan = weakerClansQueue.peek();
            if (weakerClan != null && freeSpace >= weakerClan.numberOfPlayers()) {
                return weakerClansQueue.poll();
            }
        }
        return null;
    }

}
