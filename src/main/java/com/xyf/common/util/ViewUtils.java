package com.xyf.common.util;

import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Window;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;

public class ViewUtils {

    private static Window window;
    private static Sp sp = new Sp(ViewUtils.class);

    public static void init(@Nonnull Window window) {
        ViewUtils.window = window;
    }

    @Nullable
    public static File openDirectory(@Nonnull String title) {
        final DirectoryChooser directoryChooser = new DirectoryChooser();
        final String KEY_LAST_OPEN_DIRECTORY = "last_open_directory";
        final File lastOpenDirectory = new File(sp.get(KEY_LAST_OPEN_DIRECTORY, ""));
        directoryChooser.setTitle(title);
        directoryChooser.setInitialDirectory(FileUtils2.isDirectory(lastOpenDirectory) ? lastOpenDirectory : null);
        File file = directoryChooser.showDialog(window);
        if (file != null) {
            sp.set(KEY_LAST_OPEN_DIRECTORY, file.getAbsolutePath());
        }

        return file;
    }

    @Nullable
    public static File openFile(@Nonnull String title, @Nonnull String extensionsDescription, @Nonnull String... extensions) {
        final String KEY_LAST_OPEN_DIRECTORY = "last_open_file_directory";
        final FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(title);
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(extensionsDescription, extensions));
        final File lastOpenDirectory = new File(sp.get(KEY_LAST_OPEN_DIRECTORY, ""));
        fileChooser.setInitialDirectory(FileUtils2.isDirectory(lastOpenDirectory) ? lastOpenDirectory : null);
        File file = fileChooser.showOpenDialog(window);
        if (file != null) {
            sp.set(KEY_LAST_OPEN_DIRECTORY, file.getParentFile().getAbsolutePath());
        }
        return file;
    }

    @Nullable
    public static File openImageFile(@Nonnull String title) {
        return openFile(title, "图片文件( *.png)( *.jpg)", " *.jpg ", " *.png");
    }

    @Nullable
    public static File openPngFile(@Nonnull String title) {
        return openFile(title, "图片文件( *.png)", " *.png");
    }

    @Nullable
    public static File saveFile(@Nonnull String title, @Nonnull String extensionsDescription, @Nonnull String... extensions) {
        final String KEY_LAST_SAVE_DIRECTORY = "last_save_file_directory";
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(title);
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(extensionsDescription, extensions));
        final File laseSaveDirectory = new File(sp.get(KEY_LAST_SAVE_DIRECTORY, ""));
        fileChooser.setInitialDirectory(FileUtils2.isDirectory(laseSaveDirectory) ? laseSaveDirectory : null);
        File file = fileChooser.showSaveDialog(window);
        if (file != null) {
            sp.set(KEY_LAST_SAVE_DIRECTORY, file.getParentFile().getAbsolutePath());
        }

        return file;
    }

}
