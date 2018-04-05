package com.xyf.common.util;

import com.google.common.base.Preconditions;
import javafx.scene.image.Image;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class R {


    private static final Logger LOGGER = LoggerFactory.getLogger(R.class);

    public static URL getLayout(@Nonnull String name) {
        return R.class.getClassLoader().getResource("layout/" + name);
    }

    private static InputStream getDrawable(@Nonnull String name) {
        return R.class.getClassLoader().getResourceAsStream("drawable/" + name);
    }

    @Nullable
    public static Image getImage(String name) {
        try (InputStream inputStream = getDrawable(name)) {
            return new Image(inputStream);
        } catch (IOException e) {
            LOGGER.error("", e);
        }

        return null;
    }

    public static Image getImage(File file) {
        Preconditions.checkArgument(FileUtils2.isFile(file));
        try (InputStream inputStream = new FileInputStream(file)) {
            return new Image(inputStream);
        } catch (IOException e) {
            LOGGER.error("", e);
        }

        return null;
    }

}
