package net.normalv.lifesimulation.world.water;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import net.normalv.lifesimulation.math.Vec2d;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WaterPond {
    private Vec2d pos;
    private int waterAmount;
    private Circle circle;

    public WaterPond(Vec2d pos, int waterAmount) {
        this.pos = pos;
        this.waterAmount = waterAmount;
        this.circle = new Circle(pos.x(), pos.y(), waterAmount, Color.BLUE);
    }

    public void fill(int waterAmount) {
        this.waterAmount+=waterAmount;
        circle.setRadius(this.waterAmount);
    }

    public void fill() {
        waterAmount++;
        circle.setRadius(waterAmount);
    }

    public void drink() {
        waterAmount--;
        circle.setRadius(waterAmount);
    }

    public static WaterPond createWaterPond(int sizex, int sizey) {
        Random random = new Random();
        return createWaterPond(sizex, sizey, random.nextInt(1, 100));
    }

    public static WaterPond createWaterPond(int sizex, int sizey, int waterAmount) {
        Random random = new Random();
        return new WaterPond(new Vec2d(random.nextInt(sizex), random.nextInt(sizey)), waterAmount);
    }

    public static List<WaterPond> createWaterPonds(int sizex, int sizey, int amount) {
        Random random = new Random();
        List<WaterPond> waterPonds = new ArrayList<>(amount);

        for (int i = 0; i < amount; i++) {
            waterPonds.add(createWaterPond(sizex, sizey, random.nextInt(1, 100)));
        }
        return waterPonds;
    }

    public Vec2d getPos() {
        return pos;
    }

    public int getWaterAmount() {
        return waterAmount;
    }

    public Circle getCircle() {
        return this.circle;
    }
}
