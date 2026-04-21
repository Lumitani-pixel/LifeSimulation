package net.normalv.lifesimulation.bobble;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import net.normalv.lifesimulation.math.Vec2d;
import net.normalv.lifesimulation.world.food.FoodItem;

import java.util.ArrayList;
import java.util.Random;

import static net.normalv.lifesimulation.LifeSimApplication.mainMenuController;

public class Bobble extends Features {
    private Circle circle;
    private int hunger = 100;
    private int thirst = 100;

    public Bobble(int runSpeed, int sightDistance, Vec2d spawnPos) {
        super(runSpeed, sightDistance, spawnPos);
        this.circle = new Circle(spawnPos.x(), spawnPos.y(), 10, Color.GREEN);
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
            hunger -= (getRunSpeed() / 2);
        }
    }
    public void updateThirst() {
        thirst -= (getRunSpeed());
    }
    public void updateHealth() {
        if (thirst <= 0 || hunger <= 0) {
            damage(getSightDistance()/2);
        } else if(thirst >= 50 && hunger >= 50) {
            heal(getSightDistance()/2);
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
        moveRandom(getRunSpeed());
    }

    public void gotoWater(Vec2d position) {
        moveTo(position);
    }

    public void gotoFood(Vec2d position) {
        moveTo(position);
    }

    public static Bobble makeBobbleWithRandomFeatures() {
        Random random = new Random();
        return new Bobble(
                random.nextInt(1,11),
                random.nextInt(1,11),
                new Vec2d(random.nextDouble(1200), random.nextDouble(700))
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