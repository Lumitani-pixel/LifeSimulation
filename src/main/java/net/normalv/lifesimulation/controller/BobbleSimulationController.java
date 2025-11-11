package net.normalv.lifesimulation.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import net.normalv.lifesimulation.Logger;

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

    private Stage stage;

    public void stop(ActionEvent e) {
        stage = (Stage) mainPane.getScene().getWindow();
        Logger.close();
        stage.close();
    }

    public AnchorPane getSimPane() {
        return simPane;
    }
}
