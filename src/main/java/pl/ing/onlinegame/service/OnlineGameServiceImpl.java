package pl.ing.onlinegame.service;

import io.reactivex.rxjava3.core.BackpressureStrategy;
import io.reactivex.rxjava3.core.Flowable;
import jakarta.inject.Singleton;
import pl.ing.onlinegame.domain.Clan;
import pl.ing.onlinegame.domain.ClanVisitor;
import pl.ing.onlinegame.domain.Players;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Stack;
import java.util.TreeMap;
import java.util.TreeSet;

@Singleton
public class OnlineGameServiceImpl implements OnlineGameService {
//> mean response time                                   725 (OK=725    KO=-     )
//  > std deviation                                         65 (OK=65     KO=-     )
//  > response time 50th percentile                        719 (OK=719    KO=-     )
//  > response time 75th percentile                        755 (OK=755    KO=-     )
//  > response time 95th percentile                        821 (OK=821    KO=-     )
//  > response time 99th percentile                        892 (OK=892    KO=-     )

    @Override
    public ArrayList<ArrayList<Clan>> calculateGroups(Players players) {
        if (players == null || players.clans() == null || players.clans().isEmpty()) return new ArrayList<>();
        var result = new ArrayList<ArrayList<Clan>>();

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


//    > request count                                       4377 (OK=4377   KO=0     )
//            > min response time                                    442 (OK=442    KO=-     )
//            > max response time                                   1032 (OK=1032   KO=-     )
//            > mean response time                                   686 (OK=686    KO=-     )
//            > std deviation                                         52 (OK=52     KO=-     )
//            > response time 50th percentile                        682 (OK=682    KO=-     )
//            > response time 75th percentile                        717 (OK=717    KO=-     )
//            > response time 95th percentile                        776 (OK=776    KO=-     )
//            > response time 99th percentile                        834 (OK=834    KO=-     )
//            > mean requests/sec                                 14.542 (OK=14.542 KO=-     )

    @Override
    public Flowable<ArrayList<Clan>> calculateGroupsAsync(Players players) {
        if (players.clans() == null) return Flowable.empty();
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

        return Flowable.create(emitter -> {
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
                        emitter.onNext(tempGroup);
                        tempGroup = new ArrayList<>();
                        tempGroupSize = 0;
                    }
                }
            }
            getOrphanClans(maxGroupSize, tempGroup, skippedClans).forEach(emitter::onNext);
            emitter.onComplete();
        }, BackpressureStrategy.MISSING);
    }

    public ClanVisitor findWeakerClanVisitor(int freeSpace, TreeMap<Integer, PriorityQueue<ClanVisitor>> treeMap) {
        for (Map.Entry<Integer, PriorityQueue<ClanVisitor>> weakerClans : treeMap.tailMap(freeSpace, true).entrySet()) {
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

    private ArrayList<ArrayList<Clan>> getOrphanClans(int maxGroupSize, ArrayList<Clan> tempGroup, Stack<ClanVisitor> skippedClans) {
        var result = new ArrayList<ArrayList<Clan>>();

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
