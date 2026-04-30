package net.normalv.lifesimulation.world.food;

import javafx.scene.shape.Shape;
import net.normalv.lifesimulation.LifeSimApplication;
import net.normalv.lifesimulation.math.Vec2d;

import java.util.ArrayList;
import java.util.List;

public abstract class FoodSource {
    private Vec2d pos;
    private Shape shape;
    private Class<? extends FoodItem> growingFoodItem;
    private List<FoodItem> grownFood = new ArrayList<>();
    private List<FoodItem> addGrownFood = new ArrayList<>();
    private List<FoodItem> removeGrownFood = new ArrayList<>();
    private int maxFoodAmount;
    private Vec2d foodSpawnPoint;
    private int foodSpawnRadius;
    protected int spawnRate; // In ticks so 20 means every 20 ticks one spawn
    protected int rainSpawnRate;

    public FoodSource(Vec2d pos, Shape shape, Class<? extends FoodItem> growingFoodItem, int maxFoodAmount, Vec2d foodSpawnPoint, int foodSpawnRadius, int spawnRate, int rainSpawnRate) {
        this.pos = pos;
        this.shape = shape;
        this.growingFoodItem = growingFoodItem;
        this.maxFoodAmount = maxFoodAmount;
        this.foodSpawnPoint = foodSpawnPoint;
        this.foodSpawnRadius = foodSpawnRadius;
        this.spawnRate = spawnRate;
        this.rainSpawnRate = rainSpawnRate;
    }

    public void update() {
        updateSpawnRate(false);
        for(FoodItem foodItem : getGrownFood()) {
            foodItem.update();
        }

        grownFood.removeAll(removeGrownFood);
        removeGrownFood.clear();
        grownFood.addAll(addGrownFood);
        addGrownFood.clear();
    }

    public abstract void updateSpawnRate(boolean raining);

    public void addFoodItem(FoodItem foodItem) {
        addGrownFood.add(foodItem);
        LifeSimApplication.getUpdateLoop().addFoodItem(foodItem);
    }

    public void removeFoodItem(FoodItem foodItem) {
        removeGrownFood.add(foodItem);
        LifeSimApplication.getUpdateLoop().removeFoodItem(foodItem);
    }

    public int getMaxFoodAmount() {
        return maxFoodAmount;
    }

    public List<FoodItem> getGrownFood() {
        return grownFood;
    }

    public Class<? extends FoodItem> getGrowingFoodItem() {
        return growingFoodItem;
    }

    public Vec2d getFoodSpawnPoint() {
        return foodSpawnPoint;
    }

    public int getFoodSpawnRadius() {
        return foodSpawnRadius;
    }

    public int getSpawnRate() {
        return spawnRate;
    }

    public int getRainSpawnRate() {
        return rainSpawnRate;
    }

    public Vec2d getPos() {
        return pos;
    }

    public Shape getShape() {
        return shape;
    }
}