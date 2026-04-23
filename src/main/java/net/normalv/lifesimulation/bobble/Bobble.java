package net.normalv.lifesimulation.bobble;

import javafx.animation.TranslateTransition;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import net.normalv.lifesimulation.LifeSimApplication;
import net.normalv.lifesimulation.math.Goal;
import net.normalv.lifesimulation.math.Vec2d;
import net.normalv.lifesimulation.world.food.Apple;
import net.normalv.lifesimulation.world.food.FoodItem;
import net.normalv.lifesimulation.world.water.WaterPond;

import java.util.ArrayList;
import java.util.Random;

import static net.normalv.lifesimulation.LifeSimApplication.mainMenuController;

public class Bobble extends Features {
    private Circle circle;
    private int hunger = 100;
    private int thirst = 100;

    private Random random = new Random();

    private TranslateTransition transition;

    public Bobble(int runSpeed, int sightDistance, Vec2d spawnPos) {
        super(runSpeed, sightDistance, spawnPos);
        this.circle = new Circle(spawnPos.x(), spawnPos.y(), 10, Color.GREEN);
        transition = new TranslateTransition(Duration.millis(40), circle);
    }

    public void updateAll() {
        updateHealth();
        updateHunger();
        updateThirst();
        wander();
    }

    public void updateHunger() {
        if (getRunSpeed()/2 <= 0) {
            hunger -= 2;
        } else {
            hunger -= (getRunSpeed() / 10);
        }
        if(hunger<0) hunger = 0;
    }
    public void updateThirst() {
        thirst -= getRunSpeed() / 10;
        if(thirst<0) thirst = 0;
    }
    public void updateHealth() {
        if (thirst <= 0 || hunger <= 0) {
            damage(getSightDistance()/10);
        } else if(thirst >= 50 && hunger >= 50) {
            heal(getSightDistance()/5);
        }
        //Fix to much health
        if(getHealth()>100) {
            damage(getHealth()-100);
        }
    }

    public void drinkWater() {
        thirst+=50;
    }
    public void eatFood(FoodItem food) {
        hunger+=food.getRestoringHunger();
    }

    public void wander() {
        if(!isAlive()) return;

        if(thirst < 80) {
            for(WaterPond waterPond : LifeSimApplication.getUpdateLoop().getWaterPonds()) {
                if(distanceTo(waterPond.getX(), waterPond.getY()) - waterPond.getWaterAmount() <= getSightDistance()) {
                    setCurrentGoal(new Goal(new Vec2d(waterPond.getX(), waterPond.getY()), 100));
                    drinkWater();
                }
            }
        }
        if(hunger < 80) {
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
    public int getHunger() {
        return hunger;
    }
    public int getThirst() {
        return thirst;
    }
}