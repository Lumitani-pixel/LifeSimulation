package net.normalv.lifesimulation.world.food;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import net.normalv.lifesimulation.LifeSimApplication;
import net.normalv.lifesimulation.math.Vec2d;

public abstract class FoodItem {
    private Vec2d pos;
    private int radius;
    private int restoringHunger;
    private boolean isRotten;
    private int expiresIn;
    private int despawnIn;

    private Circle circle;

    public FoodItem(Vec2d pos, int radius, int restoringHunger, int expiresIn, int despawnIn) {
        this.pos = pos;
        this.radius = radius;
        this.restoringHunger = restoringHunger;
        this.expiresIn = expiresIn;
        this.despawnIn = despawnIn;
        this.circle = new Circle(pos.x(), pos.y(), radius, Color.RED);
    }

    public void update() {
        expiring();
        updateDespawn();
    }

    private void expiring() {
        if (expiresIn<=0) {
            isRotten=true;
            return;
        }
        expiresIn--;
    }

    private void updateDespawn() {
        if(despawnIn<=0) {
            LifeSimApplication.getUpdateLoop().removeFoodItem(this);
            return;
        }
        despawnIn--;
    }

    public boolean isRotten() {
        return isRotten;
    }

    public int getDespawnIn() {
        return despawnIn;
    }

    public int getRestoringHunger() {
        return isRotten?restoringHunger/2:restoringHunger;
    }
    public Vec2d getPos() {
        return pos;
    }
    public int getRadius() {
        return radius;
    }
    public Circle getCircle() {
        return this.circle;
    }
}
