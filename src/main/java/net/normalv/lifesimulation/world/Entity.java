package net.normalv.lifesimulation.world;

import net.normalv.lifesimulation.math.Vec2d;

import java.util.Random;

public abstract class Entity {
    private final Random random = new Random();
    private int runSpeed;
    private int health;
    private boolean alive;
    private Vec2d pos;
    private Vec2d currentGoal;

    public Entity(int runSpeed, int health, Vec2d spawnPos) {
        this.runSpeed = runSpeed;
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

    // Generic move function
    public void moveTo(Vec2d target) {
        double dx = target.x() - pos.x();
        double dy = target.y() - pos.y();

        double distance = Math.sqrt(dx * dx + dy * dy);

        // If already very close, snap to target
        if (distance <= runSpeed || distance == 0) {
            pos = target;
            return;
        }

        // Normalize direction
        double nx = dx / distance;
        double ny = dy / distance;

        // Move by runSpeed in that direction
        pos = new Vec2d(
                pos.x() + nx * runSpeed,
                pos.y() + ny * runSpeed
        );
    }

    public void moveTo(Entity entity) {
        moveTo(entity.pos);
    }

    public void moveRandom(int runSpeed) {
        double dx = random.nextDouble(-runSpeed, runSpeed);
        double dy = random.nextDouble(-runSpeed, runSpeed);

        pos = new Vec2d(
                pos.x() + dx,
                pos.y() + dy
        );
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

    public void setCurrentGoal(Vec2d currentGoal) {
        this.currentGoal = currentGoal;
    }

    public Vec2d getCurrentGoal() {
        return currentGoal;
    }

    // All getters for the entity class
    public boolean isAlive() {
        return alive;
    }
    public int getHealth() {
        return health;
    }
    public int getRunSpeed() {
        return runSpeed;
    }
    public Vec2d getPos() {
        return pos;
    }
}
