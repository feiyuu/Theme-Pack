package com.xyf.common.util;

public class ClickHelper {

    private static final long CLICK_INTERVAL = 100;
    private static long LAST_CLICK_TIME;

    public static boolean canClick() {
        try {
            return Math.abs(System.currentTimeMillis() - LAST_CLICK_TIME) > CLICK_INTERVAL;
        } finally {
            LAST_CLICK_TIME = System.currentTimeMillis();
        }
    }

}
