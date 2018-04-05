package com.xyf.common.util;

import javax.annotation.Nonnull;
import java.io.File;

public class FileUtils2 {

    public static boolean isFile(@Nonnull File file) {
        return file.exists() && file.isFile();
    }

    public static boolean isDirectory(@Nonnull File directory) {
        return directory.exists() && directory.isDirectory();
    }

}
