package net.normalv.lifesimulation;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import net.normalv.lifesimulation.controller.BobbleSimulationController;
import net.normalv.lifesimulation.controller.MainMenuController;
import net.normalv.lifesimulation.util.managers.ResourceManager;
import net.normalv.lifesimulation.world.UpdateLoop;
import net.normalv.logger.Logger;

import java.io.IOException;
import java.net.URL;

public class LifeSimApplication extends Application {
    public static final int ITERATIONS_PER_FRAME = 1;

    public static URL bobbleSimURL;
    public static MainMenuController mainMenuController;
    public static BobbleSimulationController bobbleSimulationController;

    private static Stage mainStage;
    private static Scene mainMenuScene;
    private static Scene simulationScene;

    private static UpdateLoop updateLoop;

    public static ResourceManager resourceManager;

    @Override
    public void start(Stage stage) throws IOException {
        mainStage = stage;

        bobbleSimURL = getClass().getResource("bobblesimulation.fxml");
        Parent root = FXMLLoader.load(getClass().getResource("mainmenu.fxml"));
        mainMenuScene = new Scene(root);

        resourceManager = new ResourceManager();

        //Stage settings
        mainStage.setTitle("Bobble Life Sim");
        mainStage.setScene(mainMenuScene);
        mainStage.setOnCloseRequest(event -> {
            Logger.close();
            if(updateLoop != null) updateLoop.stopLoop();
        });
        mainStage.show();
    }

    public static void startSimulation(int amount, int foodAmount, int waterPuddles, boolean matingEnabled) {
        Parent simulationParent;
        try {
            simulationParent = FXMLLoader.load(bobbleSimURL);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        simulationScene = new Scene(simulationParent);

        mainStage.setScene(simulationScene);

        updateLoop = new UpdateLoop(amount, waterPuddles, foodAmount, matingEnabled);
        updateLoop.loop();
    }

    public static void stopSimulation() throws InterruptedException {
        mainStage.setScene(mainMenuScene);
        if(updateLoop != null) updateLoop.stopLoop();
    }

    public static void setMainMenuController(MainMenuController mainMenuControllerSetter) {
        mainMenuController = mainMenuControllerSetter;
    }

    public static void setBobbleSimulationController(BobbleSimulationController bobbleSimulationControllerSetter) {
        bobbleSimulationController = bobbleSimulationControllerSetter;
    }

    public static UpdateLoop getUpdateLoop() {
        return updateLoop;
    }
}