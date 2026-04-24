package net.normalv.lifesimulation.bobble;

import javafx.animation.TranslateTransition;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import net.normalv.lifesimulation.LifeSimApplication;
import net.normalv.lifesimulation.math.Goal;
import net.normalv.lifesimulation.math.Vec2d;
import net.normalv.lifesimulation.world.entities.Entity;
import net.normalv.lifesimulation.world.entities.Features;
import net.normalv.lifesimulation.world.food.Apple;
import net.normalv.lifesimulation.world.food.FoodItem;
import net.normalv.lifesimulation.world.water.WaterPond;

import java.util.ArrayList;
import java.util.Random;

import static net.normalv.lifesimulation.LifeSimApplication.mainMenuController;

public class Bobble extends Entity {
    private Circle circle;

    private Random random = new Random();

    private TranslateTransition transition;

    public Bobble(int runSpeed, int sightDistance, Vec2d spawnPos) {
        super(runSpeed, 100, sightDistance, spawnPos);
        this.circle = new Circle(spawnPos.x(), spawnPos.y(), 10, Color.GREEN);
    }

    public void updateAll() {
        updateHealth();
        updateHunger();
        updateThirst();
        wander();
    }

    public void wander() {
        if(!isAlive()) return;

        if(getThirst() < 80) {
            for(WaterPond waterPond : LifeSimApplication.getUpdateLoop().getWaterPonds()) {
                if(distanceTo(waterPond.getPos()) - waterPond.getWaterAmount() <= getSightDistance()) {
                    setTargetWaterPond(waterPond);
                    setCurrentGoal(new Goal(waterPond.getPos(), 100));
                }
            }
        }
        if(getHunger() < 80) {
            for(FoodItem foodItem : LifeSimApplication.getUpdateLoop().getFoodItems()) {
                if(distanceTo(foodItem.getPos()) - foodItem.getRadius() <= getSightDistance()) {
                    setTargetFood(foodItem);
                    setCurrentGoal(new Goal(foodItem.getPos(), 100));
                }
            }
        }

        if(getCurrentGoal() != null) {
            moveToNextStep();
        }
        else {
            setCurrentGoal(new Goal(new Vec2d(random.nextInt(500), random.nextInt(500)), 1));
        }
        circle.setCenterX(getPos().x());
        circle.setCenterY(getPos().y());
    }

    public static Bobble makeBobbleWithRandomFeatures(int spawnRadiusX, int spawnRadiusY) {
        Random random = new Random();
        return new Bobble(
                random.nextInt(10,21),
                random.nextInt(50,101),
                new Vec2d(random.nextInt(spawnRadiusX), random.nextInt(spawnRadiusY))
        );
    }
    public static ArrayList<Bobble> makeRandomBobbles(int spawnRadiusX, int spawnRadiusY, int amount) {
        ArrayList<Bobble> randomBobbles = new ArrayList<>(amount);
        for (int i = 0; i<amount; i++) {
            mainMenuController.increaseProgress(1/amount);
            Bobble bobble = makeBobbleWithRandomFeatures(spawnRadiusX, spawnRadiusY);
            randomBobbles.add(bobble);
        }
        return randomBobbles;
    }

    public Circle getCircle() {
        return this.circle;
    }

    @Override
    public String toString() {
        return "Health: "+getHealth()+", Hunger: "+getHunger()+", Thirst: "+getThirst();
    }
}