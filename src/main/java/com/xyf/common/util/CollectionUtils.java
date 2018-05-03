package com.xyf.common.util;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

public class CollectionUtils {

    public static boolean isEmpty(@Nullable Collection<?> list) {
        return list == null || list.isEmpty();
    }

    public static boolean isEmpty(@Nullable Map<?, ?> map) {
        return map == null || map.isEmpty();
    }

    public static boolean isEmpty(@Nullable Object[] directoryList) {
        return directoryList == null || directoryList.length == 0;
    }

    @Nonnull
    public static <T> List<T> asList(@Nullable T[] array) {
        if (array == null) {
            return Collections.emptyList();
        }

        return Arrays.asList(array);
    }

}
