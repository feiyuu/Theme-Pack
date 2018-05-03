package com.xyf.common.util;

import javafx.scene.image.Image;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class R {

    public static URL getLayout(@Nonnull String name) {
        return R.class.getClassLoader().getResource("layout/" + name);
    }

    private static InputStream getDrawable(@Nonnull String name) {
        return R.class.getClassLoader().getResourceAsStream("drawable/" + name);
    }

    @Nonnull
    public static Image getImage(@Nonnull String name) throws IOException {
        try (InputStream inputStream = getDrawable(name)) {
            return new Image(inputStream);
        }
    }

    @Nonnull
    public static Image getImage(@Nonnull File file) throws IOException {
        try (InputStream inputStream = new FileInputStream(file)) {
            return new Image(inputStream);
        }
    }

}
