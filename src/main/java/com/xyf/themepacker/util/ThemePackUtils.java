package com.xyf.themepacker.util;

import com.google.common.base.Preconditions;
import com.google.gson.Gson;
import com.xyf.common.annotation.WorkThread;
import com.xyf.common.util.FileUtils2;
import com.xyf.common.util.ImageUtils;
import com.xyf.themepacker.bean.FixConfigBean;
import com.xyf.themepacker.bean.PackTaskListBean;
import io.reactivex.annotations.NonNull;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.NameFileFilter;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zeroturnaround.zip.ZipUtil;

import javax.annotation.Nonnull;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class ThemePackUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(ThemePackUtils.class);

    @WorkThread
    private static void checkDirectory(@Nonnull File directory) throws IOException {
        Preconditions.checkArgument(FileUtils2.isDirectory(directory));
        clearHiddenFile(directory);

        File[] files = directory.listFiles((FileFilter) DirectoryFileFilter.INSTANCE);
        if (files == null) {
            return;
        }

        for (File file : files) {
            if (file.isDirectory()) {
                throw new IOException(directory + " should not contain directory");
            }
        }
    }

    @WorkThread
    private static void fixPreviewDirectory(@NonNull File directory) throws IOException {
        Preconditions.checkArgument(FileUtils2.isDirectory(directory));
        clearHiddenFile(directory);
        final FixConfigBean fixConfigBean;
        try (FileReader reader = new FileReader("config.json")) {
            fixConfigBean = new Gson().fromJson(reader, FixConfigBean.class);
        }

        rename(directory, fixConfigBean.preview1, "preview1.jpg");
        rename(directory, fixConfigBean.preview2, "preview2.jpg");
    }

    @WorkThread
    private static void fixIconDirectory(@NonNull File directory, PackTaskListBean.PackTask task) throws IOException {
        Preconditions.checkArgument(FileUtils2.isDirectory(directory));
        clearHiddenFile(directory);

        rename(directory, Arrays.asList("泊车影像.png", "launcher_icon_泊车.png"), "ecarx.camera.calibration.MainActivity@ecarx.camera.calibration.middle.png");
        rename(directory, "e路通.png", "com.ecarx.eline.png");
        rename(directory, "ecarx.hvac.png", "ecarx.hvac.app.png");
        copy(directory, "com.ecarx.store.png", "com.ecarx.appstore.png");
        copy(directory, "com.tpopration.roprocam.png", "ecarx.camera.dvr.png");
        copy(directory, "ecarx.camera.dvr.png", "com.tpopration.roprocam.png");

        //
        if (task.scale600p) {
            final Collection<File> files = FileUtils.listFiles(directory, new NameFileFilter(ImageUtils.IMAGE_SUFFIX), null);
            for (File file : files) {
                scale(file, 0.5);
            }
        }
    }

    @WorkThread
    private static void clearHiddenFile(@NonNull File directory) {
        FileUtils.deleteQuietly(new File(directory, ".DS_Store"));
    }

    @WorkThread
    private static void rename(@NonNull File directory, @NonNull List<String> fromList, @NonNull String to) throws IOException {
        for (String from : fromList) {
            rename(directory, from, to);
        }
    }

    @WorkThread
    private static void copy(@NonNull File directory, @NonNull String from, @NonNull String to) throws IOException {
        File fromFile = new File(directory, from);
        if (!fromFile.exists()) {
            return;
        }

        File toFile = new File(directory, to);

        if (toFile.exists()) {
            return;
        }

        FileUtils.copyFile(fromFile, toFile);
    }

    @WorkThread
    private static void rename(@NonNull File directory, @NonNull String from, @NonNull String to) throws IOException {
        final File toFile = new File(directory, to);
        File fromFile = new File(directory, from);
        if (fromFile.exists()) {
            if (toFile.exists()) {
                FileUtils.deleteQuietly(fromFile);
            } else {
                FileUtils.moveFile(fromFile, toFile);
            }
        }
    }

    @WorkThread
    public static boolean checkTaskOK(@NonNull PackTaskListBean.PackTask task) {
        try {
            checkDirectory(new File(task.iconDirectory));
            checkDirectory(new File(task.previewDirectory));
            checkDirectory(new File(task.appDirectory));
        } catch (IOException ignored) {
            return false;
        }

        if (!FileUtils2.isFile(new File(task.wallpaperFile))) {
            return false;
        }

        if (!FileUtils2.isFile(new File(task.descriptionFile))) {
            return false;
        }

        if (!StringUtils.isEmpty(task.darkWallpaperFile)) {
            return FileUtils2.isFile(new File(task.darkWallpaperFile));
        }

        return true;
    }


    @WorkThread
    public static void packTheme(@NonNull PackTaskListBean.PackTask task) throws IOException {
        Preconditions.checkArgument(checkTaskOK(task));

        final File rootDirectory = new File(FileUtils.getTempDirectory(), "theme_packer");
        final File tempDirectory = new File(rootDirectory, "pack_theme_" + System.currentTimeMillis());
        FileUtils.forceMkdir(tempDirectory);

        {
            final File iconDirectory = new File(task.iconDirectory);
            final File iconTempDirectory = new File(tempDirectory, "icon_directory");
            FileUtils.forceMkdir(iconTempDirectory);
            FileUtils.copyDirectory(iconDirectory, iconTempDirectory);
            fixIconDirectory(iconTempDirectory, task);
            final File iconTempFile = new File(tempDirectory, "icons");
            ZipUtil.pack(iconTempDirectory, iconTempFile, name -> "res/drawable-xhdpi/" + name);
            FileUtils.deleteDirectory(iconTempDirectory);
        }

        {
            final File previewDirectory = new File(task.previewDirectory);
            File previewTempDirectory = new File(tempDirectory, "preview");
            FileUtils.forceMkdir(previewTempDirectory);
            FileUtils.copyDirectory(previewDirectory, previewTempDirectory);
            fixPreviewDirectory(previewTempDirectory);
        }

        {
            File wallpaperTempDirectory = new File(tempDirectory, "wallpaper");
            FileUtils.forceMkdir(wallpaperTempDirectory);
            {
                final File wallpaperFile = new File(task.wallpaperFile);
                File wallpaperTempFile = new File(wallpaperTempDirectory, "default_wallpaper.jpg");
                FileUtils.copyFile(wallpaperFile, wallpaperTempFile);
            }
            if (!StringUtils.isEmpty(task.darkWallpaperFile)) {
                final File darkWallpaperFile = new File(task.darkWallpaperFile);
                File darkWallpaperTempFile = new File(wallpaperTempDirectory, "default_wallpaper_dark.jpg");
                FileUtils.copyFile(darkWallpaperFile, darkWallpaperTempFile);
            }
        }

        {
            final File appDirectory = new File(task.appDirectory);
            FileUtils.copyDirectory(appDirectory, tempDirectory);
        }

        {
            final File description = new File(task.descriptionFile);
            final File descriptionTempFile = new File(tempDirectory, "description.xml");
            FileUtils.copyFile(description, descriptionTempFile);
        }

        {
            clearHiddenFile(tempDirectory);
            File zip = new File(task.outputFile);
            ZipUtil.pack(tempDirectory, zip);
        }
    }

    @NonNull
    public static PackTaskListBean loadTaskList(@NonNull File file) {
        try (FileReader fileReader = new FileReader(file)) {
            return new Gson().fromJson(fileReader, PackTaskListBean.class);
        } catch (Exception e) {
            LOGGER.error(file + " load failed", e);
        }

        PackTaskListBean packTaskListBean = new PackTaskListBean();
        packTaskListBean.list = new ArrayList<>();
        return packTaskListBean;
    }

    public static void scale(@NonNull File fromImage, double scale) throws IOException {
        BufferedImage original = ImageIO.read(fromImage);
        BufferedImage fixImage = new BufferedImage(original.getWidth(), original.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D canvas = (Graphics2D) fixImage.getGraphics();
        canvas.translate((1 - scale) * fixImage.getWidth() / 2, 0);
        canvas.scale(scale, 1);
        canvas.drawImage(original, 0, 0, fixImage.getWidth(), fixImage.getHeight(), null);
        ImageIO.write(fixImage, FilenameUtils.getExtension(fromImage.getName()), fromImage);
    }

}
