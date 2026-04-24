package net.normalv.lifesimulation.world;

import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Shape;
import net.normalv.lifesimulation.LifeSimApplication;
import net.normalv.lifesimulation.bobble.Bobble;
import net.normalv.lifesimulation.world.food.Apple;
import net.normalv.lifesimulation.world.food.FoodItem;
import net.normalv.lifesimulation.world.water.WaterPond;
import net.normalv.logger.Logger;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class UpdateLoop{
    public long tickCounter;

    private int population;
    private int waterPondAmount;
    private int foodUnits;
    private int sizex = 700;
    private int sizey = 700;
    private AnchorPane simPane;
    private Group simGroup = new Group();

    private int foodGrowthRate = 1;

    private List<Bobble> bobbles;
    private List<WaterPond> waterPonds;
    private List<FoodItem> foodItems = new ArrayList<>();

    public UpdateLoop(int population, int waterPondAmount, int foodUnits) {
        // Internal Settings
        this.population = population;
        this.waterPondAmount = waterPondAmount;
        this.foodUnits = foodUnits;

        // Set tick related variables
        tickCounter = 0;

        // Graphical settings
        simPane = LifeSimApplication.bobbleSimulationController.getSimPane();

        // World settings
        this.bobbles = Bobble.makeRandomBobbles(sizex, sizey, this.population);
        this.waterPonds = WaterPond.createWaterPonds(sizex, sizey, this.waterPondAmount);
        this.foodItems.addAll(Apple.createRandomApples(sizex, sizey, foodUnits));

        // Add graphics
        addFoodItemsToRender();
        addWaterPondsToRender();
        addBobblesToRender();
        simPane.getChildren().add(simGroup);

        Logger.debug("==========Starting simulation loop==========");
    }

    //Main simulation loop
    public void loop() {
        while (population > 0) {
            tickCounter++;

            Iterator<Bobble> iterator = bobbles.iterator();

            while (iterator.hasNext()) {
                Bobble bobble = iterator.next();

                if (!bobble.isAlive()) {
                    population--;
                    iterator.remove();
                    Logger.info("Population: "+population);
                    for(Bobble bobble1 : bobbles) {
                        System.out.println(bobble1);
                    }
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

    public void removeFoodItem(FoodItem foodItem) {
        foodItems.remove(foodItem);
        foodUnits--;
        Platform.runLater(() -> simGroup.getChildren().remove(foodItem.getCircle()));
    }

    public void removeWaterPond(WaterPond waterPond) {
        waterPonds.remove(waterPond);
        waterPondAmount--;
        Platform.runLater(() -> simGroup.getChildren().remove(waterPond.getCircle()));
    }

    public void addFoodItem(FoodItem foodItem) {
        foodItems.add(foodItem);
        addGraphicToGroup(foodItem.getCircle());
        foodUnits++;
    }

    public void addWaterPond(WaterPond waterPond) {
        waterPonds.add(waterPond);
        waterPondAmount++;
    }

    public void addBobble(Bobble bobble) {
        bobbles.add(bobble);
        addGraphicToGroup(bobble.getCircle());
        population++;
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

    private void addFoodItemsToRender() {
        for(FoodItem foodItem : foodItems) {
            addGraphicToGroup(foodItem.getCircle());
        }
    }

    public void addGraphicToGroup(Shape shape) {
        simGroup.getChildren().add(shape);
    }

    public List<Bobble> getBobbles() {
        return bobbles;
    }

    public List<WaterPond> getWaterPonds() {
        return waterPonds;
    }

    public List<FoodItem> getFoodItems() {
        return foodItems;
    }
}
