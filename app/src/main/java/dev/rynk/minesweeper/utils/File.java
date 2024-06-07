/*package dev.rynk.minesweeper.utils;

import androidx.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;

public enum File {
    CURRENT_NAME("current_name", 0),
    PERSONAL_BEST("personal_best", 1),
    LEADERBOARD("leaderboard", 2),
    PERSONAL_RECENT("personal_recent", 3);
    private static final Map<String, File> BY_NAME = new HashMap<>();
    private static final Map<Integer, File> BY_INDEX = new HashMap<>();

    static {
        for (File d : values()) {
            BY_NAME.put(d.name, d);
            BY_INDEX.put(d.index, d);
        }
    }

    public final String name;
    public final int index;

    public static int size(){
        return values().length;
    }

    File(String name, int index) {
        this.name = name;
        this.index = index;
    }
    @NonNull
    @Override
    public String toString(){
        return name;
    }
    public static File fileByName(String name) {
        return BY_NAME.get(name);
    }

    public static File fileByIndex(int index) {
        return BY_INDEX.get(index);
    }

    public static String fileNameByIndex(int index) {
        return fileByIndex(index).toString();
    }
}*/
