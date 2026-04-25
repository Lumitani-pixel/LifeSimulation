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
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

public class UpdateLoop{
    public long tickCounter;

    private int population;
    private int waterPondAmount;
    private int foodUnits;
    private boolean matingEnabled;
    private int sizex = 700;
    private int sizey = 700;
    private AnchorPane simPane;
    private Group simGroup = new Group();

    private int maxFoodUnits = 1000;

    private int foodGrowthRate = 2; // How many ticks until another food units grows
    private int rainChance = 500; // Will change each time rain is triggered
    private int rainDuration = 10; // How many ticks the rain lasts

    private List<Bobble> bobbles;
    private List<WaterPond> waterPonds;
    private List<FoodItem> foodItems = new CopyOnWriteArrayList<>();

    private List<Bobble> bobblesToAdd = new ArrayList<>();

    public UpdateLoop(int population, int waterPondAmount, int foodUnits, boolean matingEnabled) {
        // Internal Settings
        this.population = population;
        this.waterPondAmount = waterPondAmount;
        this.foodUnits = foodUnits;
        this.matingEnabled = matingEnabled;

        // Set tick related variables
        tickCounter = 0;

        // Graphical settings
        simPane = LifeSimApplication.bobbleSimulationController.getSimPane();

        // World settings
        this.bobbles = Bobble.makeRandomBobbles(sizex, sizey, this.population);
        this.waterPonds = new CopyOnWriteArrayList<>(WaterPond.createWaterPonds(sizex, sizey, this.waterPondAmount));
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
        Random random = new Random();

        while (population > 0) {
            tickCounter++;

            Iterator<Bobble> iterator = bobbles.iterator();

            while (iterator.hasNext()) {
                Bobble bobble = iterator.next();

                if (!bobble.isAlive()) {
                    population--;

                    // Remove Bobble from internal and visual list
                    iterator.remove();
                    Platform.runLater(() -> simGroup.getChildren().remove(bobble.getCircle()));
                    Logger.info("Population: "+population);
                    Logger.info("Dead bobble: "+bobble);
                    continue;
                }

                bobble.updateAll();
            }

            bobbles.addAll(bobblesToAdd);
            bobblesToAdd.clear();

            if(foodUnits<maxFoodUnits && tickCounter%foodGrowthRate==0) addFoodItem(Apple.createRandomApple(sizex, sizey));
            if(rainDuration<=0 && tickCounter%rainChance==0) {
                rainDuration = random.nextInt(5, 11);
                rainChance = random.nextInt(300, 1000);
            } else if(rainDuration>0) {
                rainDuration--;
                if(random.nextInt(10)==5) addWaterPond(WaterPond.createWaterPond(sizex, sizey, 5));

                for(WaterPond waterPond : waterPonds) {
                    waterPond.fill();
                }
                addFoodItem(Apple.createRandomApple(sizex, sizey));
            }

            for(FoodItem foodItem : foodItems) {
                foodItem.update();
            }

            Platform.runLater(() -> {
                for(Bobble bobble : bobbles) {
                    bobble.getCircle().setCenterX(bobble.getPos().x());
                    bobble.getCircle().setCenterY(bobble.getPos().y());
                }
            });

            try {
                Thread.sleep(LifeSimApplication.TICK_LENGTH);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }

        Logger.info("Simulation duration: "+tickCounter);
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
        bobblesToAdd.add(bobble);
        addGraphicToGroup(bobble.getCircle());
        population++;
        Logger.info("Population: "+population);
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
        Platform.runLater(() -> simGroup.getChildren().add(shape));
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

    public boolean isMatingEnabled() {
        return matingEnabled;
    }

    public int getPopulation() {
        return population;
    }
}
