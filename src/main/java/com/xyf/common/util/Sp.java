package com.xyf.common.util;

import javax.annotation.Nonnull;
import java.util.prefs.Preferences;

public class Sp {

    private final Preferences sp;

    public Sp(@Nonnull Class clazz) {
        sp = Preferences.userNodeForPackage(clazz);
    }

    public void set(@Nonnull String key, @Nonnull String value) {
        sp.put(key, value);
    }

    public String get(@Nonnull String key, @Nonnull String def) {
        return sp.get(key, def);
    }

}
