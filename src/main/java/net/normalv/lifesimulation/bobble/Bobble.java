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
        transition = new TranslateTransition(Duration.millis(LifeSimApplication.TICK_LENGTH), circle);
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
                if(distanceTo(waterPond.getX(), waterPond.getY()) - waterPond.getWaterAmount() <= getSightDistance()) {
                    setCurrentGoal(new Goal(new Vec2d(waterPond.getX(), waterPond.getY()), 100));
                    drinkWater();
                }
            }
        }
        if(getHunger() < 80) {
            for(Apple apple : LifeSimApplication.getUpdateLoop().getApples()) {
                if(distanceTo(apple.getX(), apple.getY()) - apple.getRadius() <= getSightDistance()) {
                    setCurrentGoal(new Goal(new Vec2d(apple.getX(), apple.getY()), 100));
                    eatFood(apple);
                }
            }
        }

        if(getCurrentGoal() != null) {
            moveToNextStep();
        }
        else {
            setCurrentGoal(new Goal(new Vec2d(random.nextInt(500), random.nextInt(500)), 1));
        }
        transition.setToX(getPos().x());
        transition.setToY(getPos().y());
        transition.play();
    }

    public static Bobble makeBobbleWithRandomFeatures() {
        Random random = new Random();
        return new Bobble(
                random.nextInt(10,21),
                random.nextInt(50,101),
                new Vec2d(random.nextDouble(100,500), random.nextDouble(100,500))
        );
    }
    public static ArrayList<Bobble> makeRandomBobbles(int amount) {
        ArrayList<Bobble> randomBobbles = new ArrayList<>(amount);
        for (int i = 0; i<amount; i++) {
            mainMenuController.increaseProgress(1/amount);
            Bobble bobble = makeBobbleWithRandomFeatures();
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