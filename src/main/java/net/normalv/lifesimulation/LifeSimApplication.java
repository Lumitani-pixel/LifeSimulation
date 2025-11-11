package net.normalv.lifesimulation;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class LifeSimApplication extends Application {
    public static URL bobbleSimURL;

    @Override
    public void start(Stage stage) throws IOException {
        bobbleSimURL = getClass().getResource("bobblesimulation.fxml");
        Parent root = FXMLLoader.load(getClass().getResource("mainmenu.fxml"));
        Scene mainMenuScene = new Scene(root);

        //Stage settings
        stage.setTitle("Bobble Life Sim");
        stage.setScene(mainMenuScene);
        stage.setOnCloseRequest(event -> {
            Logger.close();
        });
        stage.show();
    }
}