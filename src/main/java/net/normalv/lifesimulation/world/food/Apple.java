package net.normalv.lifesimulation.world.food;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Apple extends FoodItem{
    public Apple(int x, int y) {
        super(x, y, 5, 20, 40);
    }

    public static Apple createRandomApple(int sizex, int sizey) {
        Random random = new Random();
        return new Apple(random.nextInt(sizex), random.nextInt(sizey));
    }

    public static List<Apple> createRandomApples(int sizex, int sizey, int amount) {
        List<Apple> apples = new ArrayList<>(amount);
        Random random = new Random();

        for(int i = 0; i<amount; i++) {
            apples.add(new Apple(random.nextInt(sizex), random.nextInt(sizey)));
        }
        return apples;
    }
}
