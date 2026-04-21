package net.normalv.lifesimulation.world;

import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import net.normalv.lifesimulation.LifeSimApplication;
import net.normalv.lifesimulation.bobble.Bobble;
import net.normalv.lifesimulation.world.water.WaterPonds;
import net.normalv.logger.Logger;

import java.util.List;

public class UpdateLoop{
    private int population;
    private int waterPondAmount;
    private int foodUnits;
    private int timer;
    private int sizex = -100;
    private int sizey = 100;
    private AnchorPane simPane;

    private List<Bobble> bobbles;
    private WaterPonds[] waterPonds;

    public UpdateLoop(int population, int waterPondAmount, int foodUnits) {
        // Internal Settings
        this.population = population;
        this.waterPondAmount = waterPondAmount;
        this.foodUnits = foodUnits;
        this.timer = 50;

        // Graphical settings
        simPane = LifeSimApplication.bobbleSimulationController.getSimPane();

        // World settings
        this.bobbles = Bobble.makeRandomBobbles(this.population);
        this.waterPonds = WaterPonds.createWaterPonds(sizex, sizey, this.waterPondAmount);

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
                bobble.updateAll();
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
