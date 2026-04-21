package net.normalv.lifesimulation.world.water;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.Random;

public class WaterPonds {
    private int x;
    private int y;
    private int waterAmount;
    private Circle circle;

    public WaterPonds(int x, int y, int waterAmount) {
        this.x = x;
        this.y = y;
        this.waterAmount = waterAmount;
        this.circle = new Circle(x, y, waterAmount, Color.BLUE);
    }

    public static WaterPonds[] createWaterPonds(int sizex, int sizey, int amount) {
        Random random = new Random();
        WaterPonds[] waterPond = new WaterPonds[amount];

        for (int i = 0; i < amount; i++) {
            waterPond[i] = new WaterPonds(random.nextInt(sizex, sizey), random.nextInt(sizex, sizey), random.nextInt(1, 100));
        }
        return waterPond;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWaterAmount() {
        return waterAmount;
    }

    public Circle getCircle() {
        return this.circle;
    }
}
