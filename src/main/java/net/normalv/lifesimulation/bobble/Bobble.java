package net.normalv.lifesimulation.bobble;

import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import net.normalv.lifesimulation.controller.BobbleSimulationController;
import net.normalv.lifesimulation.controller.MainMenuController;
import net.normalv.lifesimulation.math.Vec2d;
import net.normalv.lifesimulation.world.food.FoodItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Bobble extends Features {
    private static MainMenuController mainMenuController = new MainMenuController();
    private int hunger = 100;
    private int thirst = 100;

    public Bobble(int runSpeed, int sightDistance, Vec2d spawnPos) {
        super(runSpeed, sightDistance, spawnPos);
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
            randomBobbles.add(makeBobbleWithRandomFeatures());
        }
        return randomBobbles;
    }

    public static List<Circle> makeBobbleGraphics(int amount, Color color) {
        BobbleSimulationController bobbleSimCtrl = new BobbleSimulationController();
        AnchorPane pane = bobbleSimCtrl.getSimPane();
        Random random = new Random();
        ArrayList<Circle> circles = new ArrayList<>();

        for(int i = 0; i<amount; i++) {
            circles.add(new Circle(random.nextDouble(pane.getMinWidth(), pane.getMaxWidth()), random.nextDouble(pane.getMinHeight(), pane.getMaxHeight()), 40, color));
        }
        return circles;
    }

    public int getHunger() {
        return hunger;
    }
    public int getThirst() {
        return thirst;
    }
}