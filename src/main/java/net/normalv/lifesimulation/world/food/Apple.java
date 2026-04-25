package net.normalv.lifesimulation.world.food;

import net.normalv.lifesimulation.math.Vec2d;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Apple extends FoodItem{
    public Apple(Vec2d pos) {
        super(pos, 5, 20, 40, 60);
    }

    public static Apple createRandomApple(int sizex, int sizey) {
        Random random = new Random();
        return new Apple(new Vec2d(random.nextInt(sizex), random.nextInt(sizey)));
    }

    public static List<Apple> createRandomApples(int sizex, int sizey, int amount) {
        List<Apple> apples = new ArrayList<>(amount);
        Random random = new Random();

        for(int i = 0; i<amount; i++) {
            apples.add(new Apple(new Vec2d(random.nextInt(sizex), random.nextInt(sizey))));
        }
        return apples;
    }
}
