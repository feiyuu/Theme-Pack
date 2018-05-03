package com.xyf.common.util;

import com.xyf.common.annotation.UiThread;

public class ClickHelper {

    private static final long CLICK_INTERVAL = 100;
    private static long LAST_CLICK_TIME;

    @UiThread
    public static boolean canClick() {
        if (Math.abs(System.currentTimeMillis() - LAST_CLICK_TIME) > CLICK_INTERVAL) {
            LAST_CLICK_TIME = System.currentTimeMillis();
            return true;
        } else {
            return false;
        }
    }

}
