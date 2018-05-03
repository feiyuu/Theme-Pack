package com.xyf.common.util;

import javax.annotation.Nullable;
import java.io.File;

public class FileUtils2 {

    public static boolean isFile(@Nullable File file) {
        return file != null && file.exists() && file.isFile();
    }

    public static boolean isDirectory(@Nullable File directory) {
        return directory != null && directory.exists() && directory.isDirectory();
    }

}
