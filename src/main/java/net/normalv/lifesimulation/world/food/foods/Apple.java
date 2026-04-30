package net.normalv.lifesimulation.world.food.foods;

import net.normalv.lifesimulation.math.Vec2d;
import net.normalv.lifesimulation.world.food.FoodItem;
import net.normalv.lifesimulation.world.food.FoodSource;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Apple extends FoodItem {
    public Apple(Vec2d pos, FoodSource source) {
        super(pos, source, 5, 20, 80, 160);
    }

    public static Apple createRandomApple(double minX, double minY, double maxX, double maxY, FoodSource source) {
        Random random = new Random();
        return new Apple(new Vec2d(random.nextDouble(minX, maxX+1), random.nextDouble(minY, maxY+1)), source);
    }

    public static Apple createRandomApple(int sizex, int sizey, FoodSource source) {
        Random random = new Random();
        return new Apple(new Vec2d(random.nextInt(sizex), random.nextInt(sizey)), source);
    }

    public static List<Apple> createRandomApples(int sizex, int sizey, int amount, FoodSource source) {
        List<Apple> apples = new ArrayList<>(amount);
        Random random = new Random();

        for(int i = 0; i<amount; i++) {
            apples.add(new Apple(new Vec2d(random.nextInt(sizex), random.nextInt(sizey)), source));
        }
        return apples;
    }
}
