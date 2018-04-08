package com.xyf.themepacker;

import com.xyf.common.util.Lg;
import com.xyf.common.util.ViewUtils;
import com.xyf.themepacker.bean.PackTaskListBean;
import com.xyf.themepacker.util.ThemePackUtils;
import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.disposables.Disposable;
import io.reactivex.rxjavafx.schedulers.JavaFxScheduler;
import io.reactivex.schedulers.Schedulers;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import org.controlsfx.control.NotificationPane;
import org.controlsfx.control.StatusBar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    private static final String TAG = "MainController";

    @FXML
    public Button startPackThemeView;
    @FXML
    public TreeView<String> packListView;
    @FXML
    public NotificationPane notificationPane;
    @FXML
    public StatusBar statusBar;

    @Nullable
    private File configFile;

    private static class PackProgress {

        final int count;
        final int total;
        final int successCount;
        final int failCount;

        private PackProgress(int count, int total, int successCount, int failCount) {
            this.count = count;
            this.total = total;
            this.successCount = successCount;
            this.failCount = failCount;
        }

        double getProgress() {
            return 1.0 * count / total;
        }

        boolean isDone() {
            return count >= total;
        }

    }

    @FXML
    public void onClickStartPackTheme() {
        if (configFile == null) {
            return;
        }

        Disposable disposable = Observable.create((ObservableOnSubscribe<PackProgress>) observableEmitter -> {
            final PackTaskListBean packTaskListBean = ThemePackUtils.loadTaskList(configFile);
            int successCount = 0;
            int failCount = 0;
            for (PackTaskListBean.PackTask packTask : packTaskListBean.list) {
                try {
                    ThemePackUtils.packTheme(packTask);
                    successCount++;
                } catch (Exception e) {
                    Lg.e(TAG, "pack failed", packTask.name, e);
                    failCount++;
                }
                observableEmitter.onNext(new PackProgress(successCount + failCount, packTaskListBean.list.size(), successCount, failCount));
            }
            observableEmitter.onComplete();
        }).subscribeOn(Schedulers.io())
                .observeOn(JavaFxScheduler.platform())
                .doOnNext(packProgress -> {
                    if (packProgress.isDone()) {
                        notificationPane.show(String.format("所有打包任务完成。%d成功，%d失败", packProgress.successCount, packProgress.failCount));
                    }
                })
                .doFinally(() -> statusBar.setProgress(0))
                .subscribe(progress -> statusBar.setProgress(progress.getProgress()));
    }

    @FXML
    public void onClickLoadConfig() {
        File file = ViewUtils.openFile("打开配置文件", "json文档( *.xtzjson)", " *.xtzjson");
        if (file == null) {
            return;
        }

        configFile = file;

        Disposable disposable = Observable.fromCallable(() -> ThemePackUtils.loadTaskList(file))
                .subscribeOn(Schedulers.io())
                .observeOn(JavaFxScheduler.platform())
                .subscribe(this::updatePackListView);
    }

    private void updatePackListView(@Nonnull PackTaskListBean packTaskListBean) {
        Disposable disposable = Observable.just(packTaskListBean.list)
                .map(list -> {
                    TreeItem<String> rootNode = new TreeItem<>("任务列表");
                    rootNode.setExpanded(true);
                    int checkOkCount = 0;
                    for (PackTaskListBean.PackTask task : list) {
                        TreeItem<String> taskNameNode = new TreeItem<>(task.name == null ? "未命名任务" : task.name);
                        final boolean checkOK = ThemePackUtils.checkTaskOK(task);
                        if (checkOK) {
                            checkOkCount++;
                        }
                        taskNameNode.setValue((checkOK ? "(√)" : "(×)") + taskNameNode.getValue());
                        taskNameNode.getChildren().add(new TreeItem<>("预览图文件夹：" + task.previewDirectory));
                        taskNameNode.getChildren().add(new TreeItem<>("壁纸文件：" + task.wallpaperFile));
                        taskNameNode.getChildren().add(new TreeItem<>("暗色壁纸文件：" + task.darkWallpaperFile));
                        taskNameNode.getChildren().add(new TreeItem<>("主题描述文件：" + task.descriptionFile));
                        taskNameNode.getChildren().add(new TreeItem<>("图标文件夹：" + task.iconDirectory));
                        taskNameNode.getChildren().add(new TreeItem<>("APP定制文件夹：" + task.appDirectory));
                        taskNameNode.getChildren().add(new TreeItem<>("主题输出文件：" + task.outputFile));

                        rootNode.getChildren().add(taskNameNode);
                    }

                    rootNode.setValue(rootNode.getValue() + String.format("(%d/%d)", checkOkCount, list.size()));
                    return rootNode;
                }).subscribeOn(Schedulers.io())
                .observeOn(JavaFxScheduler.platform())
                .subscribe(node -> packListView.setRoot(node));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

}
