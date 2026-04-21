package net.normalv.lifesimulation.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import net.normalv.lifesimulation.LifeSimApplication;
import net.normalv.logger.Logger;

public class BobbleSimulationController {
    @FXML
    private SplitPane mainPane;
    @FXML
    private AnchorPane simPane;
    @FXML
    private AnchorPane logPane;
    @FXML
    private TextFlow logs;
    @FXML
    private Button stopButton;

    public BobbleSimulationController() {
        LifeSimApplication.setBobbleSimulationController(this);
    }

    public void stop(ActionEvent e) {
        try {
            LifeSimApplication.stopSimulation();
        } catch (InterruptedException ex) {
            throw new RuntimeException(ex);
        }
    }

    public AnchorPane getSimPane() {
        return simPane;
    }
}
