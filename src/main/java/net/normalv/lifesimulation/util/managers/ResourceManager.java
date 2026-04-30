package net.normalv.lifesimulation.util.managers;

import net.normalv.lifesimulation.world.food.FoodItem;
import net.normalv.lifesimulation.world.food.FoodSource;
import net.normalv.lifesimulation.world.water.WaterPond;

import java.util.*;

public class ResourceManager {
    private List<FoodItem> globalFoodItems = new ArrayList<>();
    private List<FoodSource> globalFoodSources = new ArrayList<>();
    private List<WaterPond> globalWaterPonds = new ArrayList<>();

    private List<FoodItem> addFoodItems = new ArrayList<>();
    private List<FoodSource> addFoodSources = new ArrayList<>();
    private List<WaterPond> addWaterPonds = new ArrayList<>();
    private List<FoodItem> removeFoodItems = new ArrayList<>();
    private List<FoodSource> removeFoodSources = new ArrayList<>();
    private List<WaterPond> removeWaterPonds = new ArrayList<>();

    public void update() {
        globalFoodSources.removeAll(removeFoodSources);
        globalFoodItems.removeAll(removeFoodItems);
        globalWaterPonds.removeAll(removeWaterPonds);
        removeFoodSources.clear();
        removeFoodItems.clear();
        removeWaterPonds.clear();

        globalFoodSources.addAll(addFoodSources);
        globalFoodItems.addAll(addFoodItems);
        globalWaterPonds.addAll(addWaterPonds);
        addFoodSources.clear();
        addFoodItems.clear();
        addWaterPonds.clear();
    }

    public void addFoodSource(FoodSource foodSource) {
        addFoodSources.add(foodSource);
    }

    public void addFoodItem(FoodItem foodItem) {
        addFoodItems.add(foodItem);
    }

    public void addWaterPond(WaterPond waterPond) {
        addWaterPonds.add(waterPond);
    }

    public void removeFoodSource(FoodSource foodSource) {
        removeFoodSources.add(foodSource);
    }

    public void removeFoodItem(FoodItem foodItem) {
        removeFoodItems.add(foodItem);
    }

    public void removeWaterPond(WaterPond waterPond) {
        removeWaterPonds.add(waterPond);
    }

    public void assignGlobalFoodItems(List<? extends FoodItem> foodItems) {
        globalFoodItems.addAll(foodItems);
    }

    public void assignGlobalFoodSources(List<? extends FoodSource> foodSources) {
        globalFoodSources.addAll(foodSources);
    }

    public void assignGlobalWaterPonds(List<WaterPond> waterPonds) {
        globalWaterPonds.addAll(waterPonds);
    }

    public List<FoodItem> getGlobalFoodItems() {
        return globalFoodItems;
    }

    public List<FoodSource> getGlobalFoodSources() {
        return globalFoodSources;
    }

    public List<WaterPond> getGlobalWaterPonds() {
        return globalWaterPonds;
    }
}