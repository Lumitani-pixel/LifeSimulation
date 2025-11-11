package net.normalv.lifesimulation.world;

import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import net.normalv.lifesimulation.Logger;
import net.normalv.lifesimulation.bobble.Bobble;
import net.normalv.lifesimulation.controller.BobbleSimulationController;
import net.normalv.lifesimulation.controller.MainMenuController;
import net.normalv.lifesimulation.world.water.WaterPonds;

import java.util.List;

public class UpdateLoop{
    private BobbleSimulationController bobbleSimCtrl = new BobbleSimulationController();
    private MainMenuController mainController = new MainMenuController();

    private int population;
    private int waterPondAmount;
    private int foodUnits;
    private int timer;
    private int sizex = -100;
    private int sizey = 100;
    private AnchorPane simPane;

    private List<Bobble> bobbles;
    private WaterPonds.waterPond[] waterPonds;
    private List<Circle> bobbleGraphics;

    public UpdateLoop(int population, int waterPondAmount, int foodUnits) {
        // Internal Settings
        this.population = population;
        this.waterPondAmount = waterPondAmount;
        this.foodUnits = foodUnits;
        this.timer = 50;

        // Graphical settings
        simPane = bobbleSimCtrl.getSimPane();

        // World settings
        this.bobbles = Bobble.makeRandomBobbles(this.population);
        this.waterPonds = WaterPonds.createWaterPonds(sizex, sizey, this.waterPondAmount);
        //this.bobbleGraphics = Bobble.makeBobbleGraphics(this.population, mainController.getBobbleColor());
        //bobbleGraphics.forEach(circle -> {simPane.getChildren().add(circle);});

        Logger.debug("==========Starting simulation loop==========");
    }

    //Main simulation loop
    public void loop() {
        while (population>0) {
            for (Bobble bobble : bobbles) {
                if (population<=0) break;
                if (bobble.getHealth()<=0) {
                    population--;
                    Logger.info("Population: "+population);
                    continue;
                }
                bobble.updateThirst();
                bobble.updateHunger();
                bobble.updateHealth();
                bobble.wander();
            }
            try {
                Thread.sleep(timer); // time between refreshes
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    private void updateBobbles() {
    }
}
