package net.normalv.lifesimulation.world.food;

public abstract class FoodItem {
    private int x;
    private int y;
    private int radius;
    private int restoringHunger;
    private boolean isRotten;
    private int expiresIn;

    public FoodItem(int x, int y, int radius, int restoringHunger, int expiresIn) {
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.restoringHunger = restoringHunger;
        this.expiresIn = expiresIn;
    }

    private void expiring() {
        if (expiresIn<=0) {
            isRotten=true;
            return;
        }
        expiresIn--;
    }

    public boolean isRotten() {
        return isRotten;
    }

    public int getRestoringHunger() {
        return isRotten?restoringHunger/2:restoringHunger;
    }
    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public int getRadius() {
        return radius;
    }
}
