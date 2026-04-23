package net.normalv.lifesimulation.world;

import javafx.scene.Group;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Shape;
import net.normalv.lifesimulation.LifeSimApplication;
import net.normalv.lifesimulation.bobble.Bobble;
import net.normalv.lifesimulation.world.food.Apple;
import net.normalv.lifesimulation.world.water.WaterPond;
import net.normalv.logger.Logger;

import java.util.Iterator;
import java.util.List;

public class UpdateLoop{
    private int population;
    private int waterPondAmount;
    private int foodUnits;
    private int sizex = 700;
    private int sizey = 700;
    private AnchorPane simPane;
    private Group simGroup = new Group();

    private List<Bobble> bobbles;
    private WaterPond[] waterPonds;
    private List<Apple> apples;

    public UpdateLoop(int population, int waterPondAmount, int foodUnits) {
        // Internal Settings
        this.population = population;
        this.waterPondAmount = waterPondAmount;
        this.foodUnits = foodUnits;

        // Graphical settings
        simPane = LifeSimApplication.bobbleSimulationController.getSimPane();

        // World settings
        this.bobbles = Bobble.makeRandomBobbles(this.population);
        this.waterPonds = WaterPond.createWaterPonds(sizex, sizey, this.waterPondAmount);
        this.apples = Apple.createRandomApples(sizex, sizey, foodUnits);

        // Add graphics
        addApplesToRender();
        addWaterPondsToRender();
        addBobblesToRender();
        simPane.getChildren().add(simGroup);

        Logger.debug("==========Starting simulation loop==========");
    }

    //Main simulation loop
    public void loop() {
        while (population > 0) {

            Iterator<Bobble> iterator = bobbles.iterator();

            while (iterator.hasNext()) {
                Bobble bobble = iterator.next();

                if (!bobble.isAlive()) {
                    population--;
                    iterator.remove();
                    Logger.info("Population: "+population);
                    continue;
                }

                bobble.updateAll();
            }

            try {
                Thread.sleep(LifeSimApplication.TICK_LENGTH);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    private void addBobblesToRender() {
        for(Bobble bobble : bobbles) {
            addGraphicToGroup(bobble.getCircle());
        }
    }

    private void addWaterPondsToRender() {
        for(WaterPond waterPond : waterPonds) {
            addGraphicToGroup(waterPond.getCircle());
        }
    }

    private void addApplesToRender() {
        for(Apple apple : apples) {
            addGraphicToGroup(apple.getCircle());
        }
    }

    public void addGraphicToGroup(Shape shape) {
        simGroup.getChildren().add(shape);
    }

    public List<Bobble> getBobbles() {
        return bobbles;
    }

    public WaterPond[] getWaterPonds() {
        return waterPonds;
    }

    public List<Apple> getApples() {
        return apples;
    }
}
