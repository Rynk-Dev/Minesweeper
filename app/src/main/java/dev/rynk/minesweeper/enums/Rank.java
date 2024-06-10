package dev.rynk.minesweeper.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents leaderboard ranks, key names for the leaderboard preference name and time values,
 * and index.
 */
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
    // Populates BY_RANK map
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

    /**
     * Gets the number of leaderboard ranks there are, not including "unranked".
     * @return int representing number of leaderboard ranks.
     */
    public static int numRanks(){
        return values().length - UNRANKED_OFFSET;
    }

    /**
     * Get a rank by its index.
     * @param index int representing rank's index.
     * @return Rank at index
     */
    public static Rank rankByIndex(int index) {
        return BY_RANK.get(index);
    }

    /**
     * Get the rank 1 rank lower than the given currentRank.
     * @param currentRank Rank for which the rank directly under is desired.
     * @return Rank 1 rank lower than the given rank.
     */
    public static Rank nextRank(Rank currentRank) {
        return BY_RANK.get(currentRank.index + DOWNRANK_INTERVAL);
    }
}
