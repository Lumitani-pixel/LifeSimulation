package net.normalv.lifesimulation.math;

public class Goal {
    private Vec2d goalPosition;
    private int priority;

    public Goal(Vec2d goalPosition, int priority) {
        this.goalPosition = goalPosition;
        this.priority = priority;
    }

    public Vec2d getGoalPosition() {
        return goalPosition;
    }

    public int getPriority() {
        return priority;
    }
}
