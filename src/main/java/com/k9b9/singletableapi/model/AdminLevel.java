package com.k9b9.singletableapi.model;

import java.util.HashMap;
import java.util.Map;

public enum AdminLevel {

    ROOT(0), ADMIN(100), MANAGER(200), VIEWER(300);

    private int value;
    private static Map<Integer, AdminLevel> map = new HashMap<Integer, AdminLevel>();

    private AdminLevel(int value) {
        this.value = value;
    }

    static {
        for (AdminLevel level : AdminLevel.values()) {
            map.put(level.value, level);
        }
    }

    public static AdminLevel valueOf(int level) {
        return (AdminLevel) map.get(level);
    }

    public int getValue() {
        return value;
    }
}