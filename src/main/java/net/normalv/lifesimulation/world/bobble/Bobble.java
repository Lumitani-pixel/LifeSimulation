package net.normalv.lifesimulation.world.bobble;

import javafx.application.Platform;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;
import net.normalv.lifesimulation.LifeSimApplication;
import net.normalv.lifesimulation.math.Goal;
import net.normalv.lifesimulation.math.Vec2d;
import net.normalv.lifesimulation.world.entities.Entity;
import net.normalv.lifesimulation.world.food.FoodSource;
import net.normalv.lifesimulation.world.food.watersources.WaterPond;

import java.util.ArrayList;
import java.util.Random;

import static net.normalv.lifesimulation.LifeSimApplication.mainMenuController;

public class Bobble extends Entity {
    private int meetingCooldown = 2000;
    private Bobble bobbleToMeet;
    private Vec2d meetingPos;

    private Random random = new Random();

    public Bobble(int runSpeed, int sightDistance, Vec2d spawnPos) {
        super(runSpeed, 100, sightDistance, spawnPos, new Circle(spawnPos.x(), spawnPos.y(), 10, Color.GREEN), 10);
    }

    @Override
    public void update() {
        updateHealth();
        updateHunger();
        updateThirst();
        wander();
        updateMeet();
    }

    public void wander() {
        if(!isAlive()) return;

        if(getThirst() < 80) findWaterPond();

        if(getHunger() < 80) findFoodSource();

        if(getCurrentGoal() != null) {
            moveToNextStep();
        }
        else if(LifeSimApplication.getUpdateLoop().isMatingEnabled() && meetingCooldown<=0 && LifeSimApplication.getUpdateLoop().getPopulation() > 1) {
            findBobbleToMeet();
            meetingCooldown = random.nextInt(2500, 3500);
            return;
        }
        else {
            setCurrentGoal(new Goal(new Vec2d(random.nextInt(700), random.nextInt(700)), 1));
        }

        meetingCooldown--;
        Platform.runLater(() -> {
            ((Circle) getShape()).setCenterX(getPos().x());
            ((Circle) getShape()).setCenterY(getPos().y());
        });
    }

    // Find a water pond we can drink from
    private void findWaterPond() {
        // If we remember a water pond try to go there first
        if(getRememberedWaterPond() != null && getTargetWaterPond() != null && !getTargetWaterPond().getPos().isEqualTo(getRememberedWaterPond().getPos())) {
            setTargetWaterPond(getRememberedWaterPond());
            setCurrentGoal(new Goal(getRememberedWaterPond().getPos(), 100-getThirst()));
        }

        // If we dont remember a water pond or there is one nearer then that is our goal
        for(WaterPond waterPond : LifeSimApplication.resourceManager.getGlobalWaterPonds()) {
            if(distanceTo(waterPond.getPos()) - waterPond.getWaterAmount() <= getSightDistance()) {
                if(getRememberedWaterPond() != null && distanceTo(waterPond.getPos()) > distanceTo(getRememberedWaterPond().getPos())) continue;

                setTargetWaterPond(waterPond);
                if(setCurrentGoal(new Goal(waterPond.getPos(), 100-getThirst()))) break;
                else setTargetWaterPond(null);
            }
        }
    }

    private void findFoodSource() {
        for(FoodSource foodSource : LifeSimApplication.resourceManager.getGlobalFoodSources()) {
            if(distanceTo(foodSource.getPos()) <= getSightDistance() && foodSource.getGrownFood().size() > getMinGrownFoodForTarget()) {
                setTargetFoodSource(foodSource);
                if(setCurrentGoal(new Goal(foodSource.getPos(), 100-getHunger()))) break;
                else setTargetFoodSource(null);
            }
        }
    }

    public void updateMeet() {
        if(bobbleToMeet == null || meetingPos == null || !getPos().isEqualTo(meetingPos)) return;

        mate();
    }

    public void findBobbleToMeet() {
        if(bobbleToMeet != null || meetingPos != null) return;

        for(Entity entity : LifeSimApplication.getUpdateLoop().getEntities()) {
            if(!(entity instanceof Bobble bobble) || bobble.getHunger() < 60 || bobble.getThirst() < 60) continue;

            // Creates a meeting pos thats in the middle of both bobbles
            Vec2d meetingPos = new Vec2d((bobble.getPos().x() + getPos().x()) / 2, (bobble.getPos().y() + getPos().y()) / 2);
            if(bobble.meetBobble(meetingPos, this)) {

                setCurrentGoal(new Goal(meetingPos, 200));
                setTargetWaterPond(null);
                setTargetFoodSource(null);

                bobbleToMeet = bobble;
                this.meetingPos = meetingPos;
                break;
            }
        }
    }

    public boolean meetBobble(Vec2d meetingPos, Bobble bobble) {
        if(getHunger() < 50 || getThirst() < 50 || bobbleToMeet != null) return false;

        setTargetFoodSource(null);
        setTargetWaterPond(null);
        setCurrentGoal(new Goal(meetingPos, 200));
        bobbleToMeet = bobble;
        this.meetingPos = meetingPos;
        return true;
    }

    public void mate() {
        // Makes sure only one bobble makes a newborn
        if(this.hashCode() > bobbleToMeet.hashCode()) {
            LifeSimApplication.getUpdateLoop().addEntity(
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

    public Bobble getBobbleToMeet() {
        return bobbleToMeet;
    }

    public Vec2d getMeetingPos() {
        return meetingPos;
    }

    @Override
    public String toString() {
        return "Health: "+getHealth()+", Hunger: "+getHunger()+", Thirst: "+getThirst()+", running speed: "+getRunSpeed()+", sight distance: "+getSightDistance();
    }
}