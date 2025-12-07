package net.normalv.lifesimulation.world;

import net.normalv.lifesimulation.math.Vec2d;

import java.util.Random;

public abstract class Entity {
    private final Random random = new Random();
    private int runSpeed;
    private int health;
    private Vec2d pos;

    public Entity(int runSpeed, int health, Vec2d spawnPos) {
        this.runSpeed = runSpeed;
        this.health = health;
        this.pos = spawnPos;
    }

    // Health update functions
    public void damage(int damage) {health-=damage;}
    public void heal(int amount) {health+=amount;}

    // Generic move function
    public void moveTo(Vec2d pos) {
        this.pos = pos;
    }

    public void moveTo(Entity entity) {
        pos=entity.pos;
    }

    public void moveRandom(int runSpeed) {
        double lowerBound = runSpeed/2-runSpeed;
        double upperBound = runSpeed/2;
        pos = new Vec2d(random.nextDouble(lowerBound, upperBound), random.nextDouble(lowerBound, upperBound));
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

    // All getters for the entity class
    public int getHealth() {return health;}
    public int getRunSpeed() {return runSpeed;}
}
