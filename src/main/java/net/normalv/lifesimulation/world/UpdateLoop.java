package net.normalv.lifesimulation.world;

import javafx.animation.AnimationTimer;
import javafx.scene.Group;
import javafx.scene.layout.AnchorPane;
import net.normalv.lifesimulation.LifeSimApplication;
import net.normalv.lifesimulation.bobble.Bobble;
import net.normalv.lifesimulation.world.food.FoodSource;
import net.normalv.lifesimulation.world.food.FoodItem;
import net.normalv.lifesimulation.world.food.foodsources.AppleTree;
import net.normalv.lifesimulation.world.food.watersources.WaterPond;
import net.normalv.logger.Logger;

import java.util.*;

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

    private int rainChance = 500; // Will change each time rain is triggered
    private int rainDuration = 10; // How many ticks the rain lasts

    private List<Bobble> bobbles;
    private List<Bobble> bobblesToAdd = new ArrayList<>();

    private AnimationTimer animationTimer;

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
        LifeSimApplication.resourceManager.assignGlobalFoodSources(AppleTree.createRandomAppleTrees(sizex, sizey, this.foodUnits));
        LifeSimApplication.resourceManager.assignGlobalWaterPonds(WaterPond.createWaterPonds(sizex, sizey, this.waterPondAmount));

        // Add graphics
        addFoodSourcesToRender();
        addFoodItemsToRender();
        addWaterPondsToRender();
        addBobblesToRender();
        simPane.getChildren().add(simGroup);

        Logger.debug("==========Starting simulation loop==========");
    }

    //Main simulation loop
    public void loop() {
        Random random = new Random();

        animationTimer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                for(int i = 0; i<LifeSimApplication.iterations_per_frame; i++) {
                    if(population <= 0) return;

                    tickCounter++;
                    LifeSimApplication.resourceManager.update();

                    Iterator<Bobble> iterator = bobbles.iterator();

                    while (iterator.hasNext()) {
                        Bobble bobble = iterator.next();

                        if (!bobble.isAlive()) {
                            population--;

                            // Remove Bobble from internal and visual list
                            iterator.remove();
                            simGroup.getChildren().remove(bobble.getCircle());
                            Logger.info("Population: "+population);
                            Logger.info("Dead bobble: "+bobble);
                            continue;
                        }

                        bobble.updateAll();
                    }

                    bobbles.addAll(bobblesToAdd);
                    bobblesToAdd.clear();

                    if(rainDuration<=0 && tickCounter%rainChance==0) {
                        rainDuration = random.nextInt(5, 11);
                        rainChance = random.nextInt(100, 1000);
                    } else if(rainDuration>0) {
                        rainDuration--;
                        if(random.nextInt(21)==5) addWaterPond(WaterPond.createWaterPond(sizex, sizey, 10));

                        for(WaterPond waterPond : LifeSimApplication.resourceManager.getGlobalWaterPonds()) {
                            waterPond.fill();
                        }
                    }

                    for(FoodSource foodSource : LifeSimApplication.resourceManager.getGlobalFoodSources()) {
                        foodSource.update();
                    }
                }
            }
        };
        animationTimer.start();
    }

    public void stopLoop() {
        animationTimer.stop();
    }

    public void removeFoodItem(FoodItem foodItem) {
        LifeSimApplication.resourceManager.removeFoodItem(foodItem);
        foodUnits--;
        simGroup.getChildren().remove(foodItem.getCircle());
    }

    public void removeWaterPond(WaterPond waterPond) {
        LifeSimApplication.resourceManager.removeWaterPond(waterPond);
        waterPondAmount--;
        simGroup.getChildren().remove(waterPond.getShape());
    }

    public void addFoodSource(FoodSource foodSource) {
        LifeSimApplication.resourceManager.addFoodSource(foodSource);
        simGroup.getChildren().add(foodSource.getShape());
        foodUnits++;
    }

    public void addFoodItem(FoodItem foodItem) {
        LifeSimApplication.resourceManager.addFoodItem(foodItem);
        simGroup.getChildren().add(foodItem.getCircle());
        foodUnits++;
    }

    public void addWaterPond(WaterPond waterPond) {
        LifeSimApplication.resourceManager.addWaterPond(waterPond);
        simGroup.getChildren().add(waterPond.getShape());
        waterPondAmount++;
    }

    public void addBobble(Bobble bobble) {
        bobblesToAdd.add(bobble);
        simGroup.getChildren().add(bobble.getCircle());
        population++;
        Logger.info("Population: "+population);
    }

    private void addBobblesToRender() {
        for(Bobble bobble : bobbles) {
            simGroup.getChildren().add(bobble.getCircle());
        }
    }

    private void addWaterPondsToRender() {
        for(WaterPond waterPond : LifeSimApplication.resourceManager.getGlobalWaterPonds()) {
            simGroup.getChildren().add(waterPond.getShape());
        }
    }

    private void addFoodItemsToRender() {
        for(FoodItem foodItem : LifeSimApplication.resourceManager.getGlobalFoodItems()) {
            simGroup.getChildren().add(foodItem.getCircle());
        }
    }

    private void addFoodSourcesToRender() {
        for(FoodSource foodSource : LifeSimApplication.resourceManager.getGlobalFoodSources()) {
            simGroup.getChildren().add(foodSource.getShape());
        }
    }

    public void setRainDuration(int duration) {
        rainDuration = duration;
    }

    public List<Bobble> getBobbles() {
        return bobbles;
    }

    public Group getSimGroup() {
        return simGroup;
    }

    public boolean isMatingEnabled() {
        return matingEnabled;
    }

    public int getPopulation() {
        return population;
    }
}
