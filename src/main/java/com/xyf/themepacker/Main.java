package com.xyf.themepacker;

import com.xyf.common.util.R;
import com.xyf.common.util.ViewUtils;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        ViewUtils.init(primaryStage);
        Parent root = FXMLLoader.load(R.getLayout("main.fxml"));
        primaryStage.setTitle("主题打包");
        primaryStage.getIcons().add(R.getImage("app_icon.png"));
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
