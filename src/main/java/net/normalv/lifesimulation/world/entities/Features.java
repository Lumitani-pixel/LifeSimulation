package net.normalv.lifesimulation.world.entities;

import java.util.Random;

public abstract class Features {
    private int sightDistance; // How far away they can detect objects
    private int runSpeed; // How far they can run each tick

    public Features(int runSpeed, int sightDistance) {
        this.sightDistance = sightDistance;
        this.runSpeed = runSpeed;
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

    public int getSightDistance() {
        return sightDistance;
    }
    public int getRunSpeed() {
        return runSpeed;
    }
}
