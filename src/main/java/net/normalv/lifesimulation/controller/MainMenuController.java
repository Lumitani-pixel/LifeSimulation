package net.normalv.lifesimulation.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import net.normalv.lifesimulation.LifeSimApplication;
import net.normalv.lifesimulation.world.UpdateLoop;
import net.normalv.logger.Logger;

import java.io.IOException;

public class MainMenuController {
    @FXML
    private CheckBox food;
    @FXML
    private TextField startFoodAmount;
    @FXML
    private CheckBox water;
    @FXML
    private TextField startWaterPuddles;
    @FXML
    private CheckBox mating;
    @FXML
    private Button start;
    @FXML
    private TextField amount;
    @FXML
    private ProgressBar progressBar;
    @FXML
    private Label errorMessage;
    @FXML
    private ColorPicker bobbleColor;
    @FXML
    private ColorPicker foodColor;
    @FXML
    private ColorPicker waterColor;

    private Parent root;
    private Stage stage;
    private Scene scene;
    private double progress = 0;

    public void start(ActionEvent e) throws IOException {
        errorMessage.setText("");

        // Assign the field value to a string for ease of use in other functions
        String amountField = this.amount.getText();
        String foodField = this.startFoodAmount.getText();
        String waterField = this.startWaterPuddles.getText();

        // Get the value of the fields
        int amount = Integer.parseInt(amountField.isBlank() ? "1" : amountField);
        int foodAmount = food.isSelected() ? Integer.parseInt(foodField.isBlank() ? "0" : foodField) : 0;
        int waterPuddles = water.isSelected() ? Integer.parseInt(waterField.isBlank() ? "0" : waterField) : 0;

        // Check if all values are valid
        if(amount<=0) {
            errorMessage.setText("The \"amount\" of bobbles must be over 0");
            return;
        } else if (foodAmount<0 || waterPuddles<0) {
            errorMessage.setText("The food and water amount must be over or equal to 0");
            return;
        }

        // Load the new scene "simulation scene"
        root = FXMLLoader.load(LifeSimApplication.bobbleSimURL);
        stage = (Stage) ((Node)e.getSource()).getScene().getWindow();
        scene = new Scene(root);

        String matingEnabled = mating.isSelected()?"enabled":"disabled";
        Logger.debug("Starting new sim with. Population: "+amount+", WaterPonds: "+waterPuddles+", Starting FoodUnits: "+foodAmount+" mating is: "+matingEnabled);

        // Start the simulation thread
        Thread simLoopThread;
        UpdateLoop updateLoop = new UpdateLoop(amount, waterPuddles, foodAmount);
        simLoopThread = new Thread(updateLoop::loop, "UpdateLoop");
        simLoopThread.setDaemon(true);
        simLoopThread.start();

        // Finally show the stage and assign the scene
        stage.setScene(scene);
        stage.show();
    }

    // So you can see how far it is at building the scene
    public void increaseProgress(double value) {
        //progress+=value;
        //this.progressBar.setProgress(progress);
    }

    public Color getFoodColor() {
        if(foodColor == null) return Color.RED;
        return foodColor.getValue();
    }
    public Color getWaterColor() {
        if(waterColor == null) return Color.BLUE;
        return waterColor.getValue();
    }
    public Color getBobbleColor() {
        if(bobbleColor == null) return Color.GREEN;
        return bobbleColor.getValue();
    }
}