package net.normalv.lifesimulation.world;

import java.util.Random;

public abstract class Entity {
    private final Random random = new Random();
    private int runSpeed;
    private int health;
    private double x;
    private double y;

    public Entity(int runSpeed, int health, double spawnX, double spawnY) {
        this.runSpeed = runSpeed;
        this.health = health;
        this.x = spawnX;
        this.y = spawnY;
    }

    // Health update functions
    public void damage(int damage) {health-=damage;}
    public void heal(int amount) {health+=amount;}

    // Generic move function
    public void moveTo(double x, double y) {
        this.x=x;
        this.y=y;
    }

    public void moveTo(Entity entity) {
        this.x=entity.getX();
        this.y=entity.getY();
    }

    public void moveRandom(int runSpeed) {
        double lowerBound = runSpeed/2-runSpeed;
        double upperBound = runSpeed/2;
        x = random.nextDouble(lowerBound, upperBound);
        y = random.nextDouble(lowerBound, upperBound);
    }

    /**
     * Get the hypotinuse of the triangle formed to calculate the distance to the entity
     * @param entity
     * @return
     */
    public double distanceTo(Entity entity) {
        double xDistance = this.getX()-entity.getX();
        double yDistance = this.getY()-entity.getY();

        return Math.sqrt(xDistance * xDistance + yDistance * yDistance);
    }

    /**
     * Get the distance to the cords using the pythagoras theorem
     * @param x
     * @param y
     * @return
     */
    public double distanceTo(double x, double y) {
        double xDistance = this.getX()-x;
        double yDistance = this.getY()-y;

        return Math.sqrt(xDistance * xDistance + yDistance * yDistance);
    }

    // All getters for the entity class
    public double getX() {return x;}
    public double getY() {return y;}
    public int getHealth() {return health;}
    public int getRunSpeed() {return runSpeed;}
}
