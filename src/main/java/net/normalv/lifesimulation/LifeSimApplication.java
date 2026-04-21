package net.normalv.lifesimulation;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import net.normalv.lifesimulation.controller.BobbleSimulationController;
import net.normalv.lifesimulation.controller.MainMenuController;
import net.normalv.lifesimulation.world.UpdateLoop;
import net.normalv.logger.Logger;

import java.io.IOException;
import java.net.URL;

public class LifeSimApplication extends Application {
    public static URL bobbleSimURL;
    public static MainMenuController mainMenuController;
    public static BobbleSimulationController bobbleSimulationController;

    private static Thread simLoopThread;
    private static Stage mainStage;
    private static Scene mainMenuScene;
    private static Scene simulationScene;

    @Override
    public void start(Stage stage) throws IOException {
        mainStage = stage;

        bobbleSimURL = getClass().getResource("bobblesimulation.fxml");
        Parent root = FXMLLoader.load(getClass().getResource("mainmenu.fxml"));
        mainMenuScene = new Scene(root);

        //Stage settings
        mainStage.setTitle("Bobble Life Sim");
        mainStage.setScene(mainMenuScene);
        mainStage.setOnCloseRequest(event -> Logger.close());
        mainStage.show();
    }

    public static void startSimulation(int amount, int foodAmount, int waterPuddles, boolean mating) {
        Parent simulationParent;
        try {
            simulationParent = FXMLLoader.load(bobbleSimURL);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        simulationScene = new Scene(simulationParent);

        mainStage.setScene(simulationScene);

        UpdateLoop updateLoop = new UpdateLoop(amount, waterPuddles, foodAmount);
        simLoopThread = new Thread(updateLoop::loop, "UpdateLoop");
        simLoopThread.setDaemon(true);
        simLoopThread.start();
    }

    public static void stopSimulation() throws InterruptedException {
        mainStage.setScene(mainMenuScene);
        simLoopThread.join();
    }

    public static void setMainMenuController(MainMenuController mainMenuControllerSetter) {
        mainMenuController = mainMenuControllerSetter;
    }

    public static void setBobbleSimulationController(BobbleSimulationController bobbleSimulationControllerSetter) {
        bobbleSimulationController = bobbleSimulationControllerSetter;
    }
}