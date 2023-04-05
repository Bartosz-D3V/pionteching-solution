package pl.ing.onlinegame.domain;

import java.util.LinkedList;

public record Group(int totalPlayers, LinkedList<Clan> clans) {}
