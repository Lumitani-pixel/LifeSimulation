package net.normalv.lifesimulation.bobble;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import net.normalv.lifesimulation.LifeSimApplication;
import net.normalv.lifesimulation.math.Goal;
import net.normalv.lifesimulation.math.Vec2d;
import net.normalv.lifesimulation.world.entities.Entity;
import net.normalv.lifesimulation.world.food.FoodItem;
import net.normalv.lifesimulation.world.water.WaterPond;

import java.util.ArrayList;
import java.util.Random;

import static net.normalv.lifesimulation.LifeSimApplication.mainMenuController;

public class Bobble extends Entity {
    private Circle circle;

    private int meetingCooldown = 2000;
    private Bobble bobbleToMeet;
    private Vec2d meetingPos;

    private Random random = new Random();

    public Bobble(int runSpeed, int sightDistance, Vec2d spawnPos) {
        super(runSpeed, 100, sightDistance, spawnPos);
        this.circle = new Circle(spawnPos.x(), spawnPos.y(), 10, Color.GREEN);
    }

    public void updateAll() {
        updateHealth();
        updateHunger();
        updateThirst();
        wander();
        updateMeet();
    }

    public void wander() {
        if(!isAlive()) return;

        if(getThirst() < 80 && getTargetWaterPond() == null) {
            for(WaterPond waterPond : LifeSimApplication.getUpdateLoop().getWaterPonds()) {
                if(distanceTo(waterPond.getPos()) - waterPond.getWaterAmount() <= getSightDistance()) {

                    setTargetWaterPond(waterPond);
                    if(setCurrentGoal(new Goal(waterPond.getPos(), 100-getThirst()))) break;
                    else setTargetWaterPond(null);
                }
            }
        }
        if(getHunger() < 80 && getTargetFood() == null) {
            for(FoodItem foodItem : LifeSimApplication.getUpdateLoop().getFoodItems()) {
                if(foodItem.getDespawnIn() < 10) continue;
                if(distanceTo(foodItem.getPos()) - foodItem.getRadius() <= getSightDistance()) {

                    setTargetFood(foodItem);
                    if(setCurrentGoal(new Goal(foodItem.getPos(), 100-getHunger()))) break;
                    else setTargetFood(null);
                }
            }
        }

        if(getCurrentGoal() != null) {
            moveToNextStep();
        }
        else if(LifeSimApplication.getUpdateLoop().isMatingEnabled() && meetingCooldown<=0 && LifeSimApplication.getUpdateLoop().getPopulation() > 1) {
            findBobbleToMeet();
            meetingCooldown = 3000;
            return;
        }
        else {
            setCurrentGoal(new Goal(new Vec2d(random.nextInt(700), random.nextInt(700)), 1));
        }

        meetingCooldown--;
        //circle.setCenterX(getPos().x());
        //circle.setCenterY(getPos().y());
    }

    public void updateMeet() {
        if(bobbleToMeet == null || meetingPos == null || !getPos().isEqualTo(meetingPos)) return;

        mate();
    }

    public void findBobbleToMeet() {
        if(bobbleToMeet != null || meetingPos != null) return;

        for(Bobble bobble : LifeSimApplication.getUpdateLoop().getBobbles()) {
            if(bobble.getHunger() < 60 || bobble.getThirst() < 60) continue;

            // Creates a meeting pos thats in the middle of both bobbles
            Vec2d meetingPos = new Vec2d((bobble.getPos().x() + getPos().x()) / 2, (bobble.getPos().y() + getPos().y()) / 2);
            if(bobble.meetBobble(meetingPos, this)) {

                setCurrentGoal(new Goal(meetingPos, 200));
                setTargetWaterPond(null);
                setTargetFood(null);

                bobbleToMeet = bobble;
                this.meetingPos = meetingPos;
                break;
            }
        }
    }

    public boolean meetBobble(Vec2d meetingPos, Bobble bobble) {
        if(getHunger() < 50 || getThirst() < 50 || bobbleToMeet != null) return false;

        setTargetFood(null);
        setTargetWaterPond(null);
        setCurrentGoal(new Goal(meetingPos, 200));
        bobbleToMeet = bobble;
        this.meetingPos = meetingPos;
        return true;
    }

    public void mate() {
        // Makes sure only one bobble makes a newborn
        if(this.hashCode() > bobbleToMeet.hashCode()) {
            LifeSimApplication.getUpdateLoop().addBobble(
                    new Bobble(getNewBornAttribute(bobbleToMeet.getRunSpeed(), getRunSpeed()),
                            getNewBornAttribute(bobbleToMeet.getSightDistance(), getSightDistance()),
                            getPos()
                    )
            );
        }

        // Makes sure we reset so we can find new goals
        bobbleToMeet = null;
        meetingPos = null;
    }

    public static Bobble makeBobbleWithRandomFeatures(int spawnRadiusX, int spawnRadiusY) {
        Random random = new Random();
        return new Bobble(
                random.nextInt(15,26),
                random.nextInt(80,121),
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
        return "Health: "+getHealth()+", Hunger: "+getHunger()+", Thirst: "+getThirst()+", running speed: "+getRunSpeed()+", sight distance: "+getSightDistance();
    }
}