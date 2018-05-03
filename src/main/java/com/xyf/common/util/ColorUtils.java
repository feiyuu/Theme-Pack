package com.xyf.common.util;

import com.google.common.base.Preconditions;
import javafx.scene.paint.Color;

import javax.annotation.Nonnull;
import java.util.regex.Pattern;

public class ColorUtils {

    private static final Pattern ANDROID_COLOR_PATTERN = Pattern.compile("^#([0-9a-fA-F]{8})$");

    /**
     * Android上的颜色值为#AARRGGBB
     * Web上的颜色值#RRGGBBAA
     */
    public static Color parseColor(@Nonnull String androidColor) {
        Preconditions.checkArgument(isColorString(androidColor));
        final String a = androidColor.substring(1, 3);
        final String r = androidColor.substring(3, 5);
        final String g = androidColor.substring(5, 7);
        final String b = androidColor.substring(7, 9);
        return Color.web(String.format("#%s%s%s%s", r, g, b, a));
    }

    public static boolean isColorString(@Nonnull String string) {
        return ANDROID_COLOR_PATTERN.matcher(string).matches();
    }

    @Nonnull
    public static String parseColorPressed(@Nonnull String androidColor) {
        Preconditions.checkArgument(isColorString(androidColor));
        final String r = androidColor.substring(3, 5);
        final String g = androidColor.substring(5, 7);
        final String b = androidColor.substring(7, 9);
        return String.format("#80%s%s%s", r, g, b);
    }

}
