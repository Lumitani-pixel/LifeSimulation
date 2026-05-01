package net.normalv.lifesimulation.world.food.watersources;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import net.normalv.lifesimulation.math.Vec2d;
import net.normalv.lifesimulation.world.food.WaterSource;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WaterPond extends WaterSource {
    public WaterPond(Vec2d pos, int waterAmount) {
        super(pos, new Circle(pos.x(), pos.y(), waterAmount, Color.BLUE), waterAmount);
    }

    @Override
    public void fill(int waterAmount) {
        this.waterAmount+=waterAmount;
        ((Circle)shape).setRadius(this.waterAmount);
    }

    @Override
    public void fill() {
        waterAmount++;
        ((Circle)shape).setRadius(waterAmount);
    }

    @Override
    public void drink() {
        waterAmount--;
        ((Circle)shape).setRadius(waterAmount);
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
}
