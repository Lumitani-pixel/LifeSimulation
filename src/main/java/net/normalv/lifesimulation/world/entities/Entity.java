package net.normalv.lifesimulation.world.entities;

import net.normalv.lifesimulation.LifeSimApplication;
import net.normalv.lifesimulation.math.Goal;
import net.normalv.lifesimulation.math.Vec2d;
import net.normalv.lifesimulation.world.food.FoodItem;
import net.normalv.lifesimulation.world.water.WaterPond;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public abstract class Entity extends Features{
    private final Random random = new Random();
    private int health;
    private boolean alive;
    private int hunger = 100;
    private int thirst = 100;

    private Vec2d pos;
    private Goal currentGoal;
    private List<Vec2d> stepsToGoal = new ArrayList<>();
    private WaterPond targetWaterPond;
    private FoodItem targetFood;

    public Entity(int runSpeed, int health, int sightDistance, Vec2d spawnPos) {
        super(runSpeed, sightDistance);
        this.health = health;
        this.alive = true;
        this.pos = spawnPos;
    }

    // Health update functions
    public void damage(int damage) {
        health-=damage;
        if(health<=0) alive = false;
    }
    public void heal(int amount) {
        health+=amount;
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
        targetWaterPond.drink();
        if(targetWaterPond.getWaterAmount() < 1) LifeSimApplication.getUpdateLoop().removeWaterPond(targetWaterPond);
        thirst+=30;
    }
    public void eatFood(FoodItem food) {
        hunger+=food.getRestoringHunger();
        LifeSimApplication.getUpdateLoop().removeFoodItem(targetFood);
    }

    public void moveToNextStep() {
        moveTo(stepsToGoal.removeFirst());
        if(stepsToGoal.isEmpty()) {
            if(thirst < 80 && targetWaterPond != null) drinkWater();
            else if(hunger < 80 && targetFood != null) eatFood(targetFood);
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

    public void setTargetFood(FoodItem food) {
        targetFood = food;
    }

    public void setTargetWaterPond(WaterPond waterPond) {
        targetWaterPond = waterPond;
    }

    public void setCurrentGoal(Goal currentGoal) {
        if(this.currentGoal != null && this.currentGoal.getPriority() > currentGoal.getPriority()) return;
        this.currentGoal = currentGoal;

        Vec2d nextStep = pos;
        stepsToGoal.clear();

        double nextX;
        double nextY;

        while(nextStep.getDifference(currentGoal.getGoalPosition()) > getRunSpeed()) {
            if(currentGoal.getGoalPosition().x() - nextStep.x() > 0) nextX = nextStep.x() + getRunSpeed();
            else nextX = nextStep.x() - getRunSpeed();

            if(currentGoal.getGoalPosition().y() - nextStep.y() > 0) nextY = nextStep.y() + getRunSpeed();
            else nextY = nextStep.y() - getRunSpeed();

            nextStep = new Vec2d(nextX, nextY);
            stepsToGoal.add(nextStep);
        }
        stepsToGoal.add(currentGoal.getGoalPosition());
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

    public Vec2d getPos() {
        return pos;
    }
}
