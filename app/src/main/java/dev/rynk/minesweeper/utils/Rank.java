package dev.rynk.minesweeper.utils;

import java.util.HashMap;
import java.util.Map;

public enum Rank {
    GOLD("gold_name", "gold_time", 0),
    SILVER("silver_name", "silver_time", 1),
    BRONZE("bronze_name", "bronze_time", 2),
    UNRANKED("unranked_name", "unranked_time", 3);

    public final String name;
    public final String time;
    public final int index;
    private static final int UNRANKED_OFFSET = 1;
    private static final int DOWNRANK_INTERVAL = 1;
    private static final Map<Integer, Rank> BY_RANK = new HashMap<>();

    static {
        for (Rank r : values()){
            BY_RANK.put(r.index, r);
        }
    }
    Rank(String name, String time, int index){
        this.name = name;
        this.time = time;
        this.index = index;
    }
    public static int size(){
        return values().length;
    }
    public static int numRanks(){
        return values().length - UNRANKED_OFFSET;
    }
    public static Rank rankByIndex(int index) {
        return BY_RANK.get(index);
    }
    public static Rank nextRank(Rank currentRank) {
        return BY_RANK.get(currentRank.index + DOWNRANK_INTERVAL);
    }
}
