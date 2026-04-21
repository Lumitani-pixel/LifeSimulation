package net.normalv.lifesimulation.world.food;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Apple extends FoodItem{
    private Circle circle;

    public Apple(int x, int y) {
        super(x, y, 20, 40);
        this.circle = new Circle(x, y, 5, Color.RED);
    }

    public Circle getCircle() {
        return this.circle;
    }
}
