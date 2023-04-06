package pl.ing.onlinegame.service;

import io.reactivex.rxjava3.core.Flowable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.PriorityQueue;
import java.util.TreeMap;

import io.reactivex.rxjava3.core.Single;
import pl.ing.onlinegame.domain.Clan;
import pl.ing.onlinegame.domain.Players;

public interface OnlineGameService {
    ArrayList<ArrayList<Clan>> calculateGroups(Players players);
    Flowable<ArrayList<Clan>> calculateGroupsAsync(Players players);
}
