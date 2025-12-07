package net.normalv.lifesimulation.bobble;

import net.normalv.lifesimulation.math.Vec2d;
import net.normalv.lifesimulation.world.Entity;

import java.util.Random;

public abstract class Features extends Entity {
    private int sightDistance;
    private int atractiveness;

    public Features(int runSpeed, int sightDistance, Vec2d spawnPos) {
        super(runSpeed, 100, spawnPos);
        this.sightDistance = sightDistance;
        this.atractiveness = calculateAtractiveness(runSpeed, sightDistance);
    }

    public int getSightDistance() {
        return sightDistance;
    }
    public int getAtractiveness() {
        return atractiveness;
    }

    private int calculateAtractiveness(int runSpeed, int sightDistance) {
        return (runSpeed+sightDistance)/2;
    }

    /*
    * If there is a newborn we need to make new attributes for it
    * We also look at simple mutation which is chosen at random
     */
    public int getNewBornAttribute(int dadsValue, int momsValue) {
        Random random = new Random();
        if(random.nextBoolean()) {  //if true get mutated speed
            return (dadsValue+momsValue)/2+random.nextInt(-2, 3);
        } else { //else get the average from mom and dad
            return (dadsValue+momsValue)/2;
        }
    }
}
