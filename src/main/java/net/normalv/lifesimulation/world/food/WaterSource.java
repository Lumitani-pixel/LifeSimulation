package net.normalv.lifesimulation.world.food;

import javafx.scene.shape.Shape;
import net.normalv.lifesimulation.math.Vec2d;

public abstract class WaterSource {
    protected Vec2d pos;
    protected int waterAmount;
    protected Shape shape;

    public WaterSource(Vec2d pos, Shape shape, int waterAmount) {
        this.pos = pos;
        this.waterAmount = waterAmount;
        this.shape = shape;
    }

    public abstract void fill(int waterAmount);

    public abstract void fill();

    public abstract void drink();

    public Vec2d getPos() {
        return pos;
    }

    public int getWaterAmount() {
        return waterAmount;
    }

    public Shape getShape() {
        return this.shape;
    }
}
