package net.normalv.lifesimulation;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import net.normalv.lifesimulation.controller.BobbleSimulationController;
import net.normalv.lifesimulation.controller.MainMenuController;
import net.normalv.logger.Logger;

import java.io.IOException;
import java.net.URL;

public class LifeSimApplication extends Application {
    public static URL bobbleSimURL;
    public static MainMenuController mainMenuController;
    public static BobbleSimulationController bobbleSimulationController;

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

    public static void setMainMenuController(MainMenuController mainMenuControllerSetter) {
        mainMenuController = mainMenuControllerSetter;
    }

    public static void setBobbleSimulationController(BobbleSimulationController bobbleSimulationControllerSetter) {
        bobbleSimulationController = bobbleSimulationControllerSetter;
    }
}