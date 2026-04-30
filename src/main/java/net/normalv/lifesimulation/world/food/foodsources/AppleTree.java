package net.normalv.lifesimulation.world.food.foodsources;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import net.normalv.lifesimulation.math.Vec2d;
import net.normalv.lifesimulation.world.food.FoodSource;
import net.normalv.lifesimulation.world.food.foods.Apple;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AppleTree extends FoodSource {
    public AppleTree(Vec2d pos, int maxFoodAmount) {
        super(pos, new Rectangle(20, 40, Color.BROWN), Apple.class, maxFoodAmount, pos, 20, 20, 10);
        ((Rectangle) getShape()).setX(pos.x());
        ((Rectangle) getShape()).setY(pos.y());
    }

    @Override
    public void updateSpawnRate(boolean raining) {
        if(getGrownFood().size() >= getMaxFoodAmount()) return;

        if(raining && --rainSpawnRate <= 0) {
            Apple apple = Apple.createRandomApple(
                    getFoodSpawnPoint().x() - getFoodSpawnRadius(),
                    getFoodSpawnPoint().y() - getFoodSpawnRadius(),
                    getFoodSpawnPoint().x() + getFoodSpawnRadius(),
                    getFoodSpawnPoint().y() + getFoodSpawnRadius(),
                    this
            );

            addFoodItem(apple);
            rainSpawnRate = 10;
        }
        else if(--spawnRate <= 0) {
            Apple apple = Apple.createRandomApple(
                    getFoodSpawnPoint().x() - getFoodSpawnRadius(),
                    getFoodSpawnPoint().y() - getFoodSpawnRadius(),
                    getFoodSpawnPoint().x() + getFoodSpawnRadius(),
                    getFoodSpawnPoint().y() + getFoodSpawnRadius(),
                    this
            );

            addFoodItem(apple);
            spawnRate = 20;
        }
    }

    public static List<AppleTree> createRandomAppleTrees(int sizex, int sizey, int amount) {
        Random random = new Random();
        List<AppleTree> appleTrees = new ArrayList<>();
        while(--amount>=0) {
            appleTrees.add(new AppleTree(new Vec2d(random.nextInt(sizex), random.nextInt(sizey)), random.nextInt(15, 25)));
        }
        return appleTrees;
    }
}
