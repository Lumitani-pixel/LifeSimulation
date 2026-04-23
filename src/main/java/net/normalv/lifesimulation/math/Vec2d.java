package net.normalv.lifesimulation.math;

public record Vec2d (double x, double y) {
    public double getDifference(Vec2d otherPos) {
        return Math.abs(this.x - otherPos.x) + Math.abs(this.y - otherPos.y);
    }
}
