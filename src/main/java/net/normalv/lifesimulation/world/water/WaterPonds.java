package net.normalv.lifesimulation.world.water;

import java.util.Random;

public class WaterPonds {
    private int x;
    private int y;
    private int waterAmount;

    public static waterPond[] createWaterPonds(int sizex, int sizey, int amount) {
       Random random = new Random();
      waterPond[] waterPond = new waterPond[amount];
      for (int i = 0; i < amount; i++) {
         waterPond[i] = new waterPond(random.nextInt(sizex, sizey), random.nextInt(sizex, sizey), random.nextInt(1, 100));
      }
       return waterPond;
    }

    public record waterPond(int x, int y, int waterAmount) {
      public void updateSize() {

      }
    }
}
