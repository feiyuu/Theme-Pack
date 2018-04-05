package com.xyf.themepacker.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PackTaskListBean {

    @SerializedName("list")
    public List<PackTask> list;

    public static class PackTask {

        @SerializedName("name")
        public String name;
        @SerializedName("scale600p")
        public boolean scale600p;
        @SerializedName("previewDirectory")
        public String previewDirectory;
        @SerializedName("wallpaperFile")
        public String wallpaperFile;
        @SerializedName("darkWallpaperFile")
        public String darkWallpaperFile;
        @SerializedName("descriptionFile")
        public String descriptionFile;
        @SerializedName("iconDirectory")
        public String iconDirectory;
        @SerializedName("appDirectory")
        public String appDirectory;
        @SerializedName("outputFile")
        public String outputFile;

    }

}
