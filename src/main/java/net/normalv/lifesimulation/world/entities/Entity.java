package net.normalv.lifesimulation.world.entities;

import javafx.scene.shape.Shape;
import net.normalv.lifesimulation.LifeSimApplication;
import net.normalv.lifesimulation.math.Goal;
import net.normalv.lifesimulation.math.Vec2d;
import net.normalv.lifesimulation.world.food.FoodItem;
import net.normalv.lifesimulation.world.food.FoodSource;
import net.normalv.lifesimulation.world.food.watersources.WaterPond;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public abstract class Entity extends Features{
    private final UUID uuid;
    private int health;
    private boolean alive;
    private int hunger = 100;
    private int thirst = 100;
    private int interactionRadius;
    private int minGrownFoodForTarget = 1; // If there is less food on a food source than this we dont set it as a target
    private int minWaterAmountForTarget = 1; // If there is less water then this we dont set the water source as a target

    private Vec2d pos;
    private Goal currentGoal;
    private List<Vec2d> stepsToGoal = new ArrayList<>();
    private WaterPond targetWaterPond;
    private FoodSource targetFoodSource;
    private Shape shape;

    private WaterPond rememberedWaterPond;
    private FoodSource rememberedFoodSource;

    public Entity(int runSpeed, int health, int sightDistance, Vec2d spawnPos, Shape shape, int interactionRadius) {
        super(runSpeed, sightDistance);
        this.health = health;
        this.alive = true;
        this.interactionRadius = interactionRadius;
        this.pos = spawnPos;
        this.uuid = UUID.randomUUID();
        this.shape = shape;
    }

    public abstract void update();

    // Health update functions
    public void damage(int damage) {
        health-=damage;
        if(health<=0) alive = false;
    }
    public void heal(int amount) {
        health+=amount;
    }

    public void updateHunger() {
        hunger -= getRunSpeed() / 15;
        if(hunger<0) hunger = 0;
    }

    public void updateThirst() {
        thirst -= getRunSpeed() / 15;
        if(thirst<0) thirst = 0;
    }

    public void updateHealth() {
        if (thirst <= 10 || hunger <= 10) {
            damage(getSightDistance()/20);
        } else if(thirst >= 60 && hunger >= 60) {
            heal(getSightDistance()/20);
        }
        //Fix to much health
        if(getHealth()>100) {
            damage(getHealth()-100);
        }
    }

    public void drinkWater() {
        targetWaterPond.drink();
        if(targetWaterPond.getWaterAmount() < 1) LifeSimApplication.getUpdateLoop().removeWaterPond(targetWaterPond);
        thirst+=30;
        targetWaterPond = null;
    }
    public void eatFood(FoodItem food) {
        hunger+=food.getRestoringHunger();
        food.getSource().removeFoodItem(food);
        setTargetFoodSource(null);
    }

    public void moveToNextStep() {
        moveTo(stepsToGoal.removeFirst());
        if(stepsToGoal.isEmpty()) {
            if(thirst < 80 && targetWaterPond != null) {
                if(targetWaterPond.getWaterAmount() > minWaterAmountForTarget) rememberedWaterPond = targetWaterPond;
                drinkWater();
            }
            else if(hunger < 80 && targetFoodSource != null) {
                if(targetFoodSource.getGrownFood().size() > minGrownFoodForTarget) rememberedFoodSource = targetFoodSource;
                targetFoodSource.getGrownFood().forEach(this::eatFood);
            }
            currentGoal = null;
        }
    }

    // Best move function ever created
    public void moveTo(Vec2d target) {
        pos = target;
    }

    public void moveTo(Entity entity) {
        moveTo(entity.pos);
    }

    /**
     * Get the hypotinuse of the triangle formed to calculate the distance to the entity
     * @param entity
     * @return
     */
    public double distanceTo(Entity entity) {
        double xDistance = this.pos.x()-entity.pos.x();
        double yDistance = this.pos.y()-entity.pos.y();

        return Math.sqrt(xDistance * xDistance + yDistance * yDistance);
    }

    /**
     * Get the distance to the cords using the pythagoras theorem
     * @param x
     * @param y
     * @return
     */
    public double distanceTo(double x, double y) {
        double xDistance = this.pos.x()-x;
        double yDistance = this.pos.y()-y;

        return Math.sqrt(xDistance * xDistance + yDistance * yDistance);
    }

    public double distanceTo(Vec2d pos) {
        return distanceTo(pos.x(), pos.y());
    }

    public void setTargetFoodSource(FoodSource foodSource) {
        targetFoodSource = foodSource;
    }

    public void setTargetWaterPond(WaterPond waterPond) {
        targetWaterPond = waterPond;
    }

    public boolean setCurrentGoal(Goal currentGoal) {
        if(this.currentGoal != null && this.currentGoal.getPriority() > currentGoal.getPriority()) return false;

        if (targetWaterPond != null) {
            this.currentGoal = new Goal(getEdgePoint(currentGoal.getGoalPosition(), targetWaterPond.getWaterAmount()), currentGoal.getPriority());
        } else if (targetFoodSource != null) {
            this.currentGoal = new Goal(getEdgePoint(currentGoal.getGoalPosition(), interactionRadius), currentGoal.getPriority());
        } else {
            this.currentGoal = currentGoal;
        }

        Vec2d nextStep = pos;
        stepsToGoal.clear();

        double nextX;
        double nextY;

        while(nextStep.getDifference(this.currentGoal.getGoalPosition()) > getRunSpeed()) {
            if(this.currentGoal.getGoalPosition().x() - nextStep.x() > 0) nextX = nextStep.x() + getRunSpeed();
            else nextX = nextStep.x() - getRunSpeed();

            if(this.currentGoal.getGoalPosition().y() - nextStep.y() > 0) nextY = nextStep.y() + getRunSpeed();
            else nextY = nextStep.y() - getRunSpeed();

            nextStep = new Vec2d(nextX, nextY);
            stepsToGoal.add(nextStep);
        }
        stepsToGoal.add(this.currentGoal.getGoalPosition());
        return true;
    }

    private Vec2d getEdgePoint(Vec2d center, double radius) {
        double dx = center.x() - pos.x();
        double dy = center.y() - pos.y();
        double length = Math.sqrt(dx * dx + dy * dy);

        if (length == 0) return center;

        double nx = dx / length;
        double ny = dy / length;
        double goalX = center.x() - nx * radius;
        double goalY = center.y() - ny * radius;

        return new Vec2d(goalX, goalY);
    }

    public void setInteractionRadius(int interactionRadius) {
        this.interactionRadius = interactionRadius;
    }

    public WaterPond getTargetWaterPond() {
        return targetWaterPond;
    }

    public FoodSource getTargetFoodSource() {
        return targetFoodSource;
    }

    public WaterPond getRememberedWaterPond() {
        return rememberedWaterPond;
    }

    public FoodSource getRememberedFoodSource() {
        return rememberedFoodSource;
    }

    public int getMinGrownFoodForTarget() {
        return minGrownFoodForTarget;
    }

    public int getMinWaterAmountForTarget() {
        return minWaterAmountForTarget;
    }

    public Goal getCurrentGoal() {
        return currentGoal;
    }

    public List<Vec2d> getStepsToGoal() {
        return stepsToGoal;
    }

    // All getters for the entity class
    public boolean isAlive() {
        return alive;
    }

    public int getHealth() {
        return health;
    }

    public int getHunger() {
        return hunger;
    }

    public int getThirst() {
        return thirst;
    }

    public int getInteractionRadius() {
        return interactionRadius;
    }

    public Vec2d getPos() {
        return pos;
    }

    public UUID getUuid() {
        return uuid;
    }

    public Shape getShape() {
        return shape;
    }
}
