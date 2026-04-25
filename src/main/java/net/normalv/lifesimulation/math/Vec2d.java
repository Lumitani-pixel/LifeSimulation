package net.normalv.lifesimulation.math;

public record Vec2d (double x, double y) {
    public double getDifference(Vec2d otherPos) {
        return Math.abs(this.x - otherPos.x) + Math.abs(this.y - otherPos.y);
    }

    public boolean isEqualTo(Vec2d vec2d) {
        return x == vec2d.x() && y == vec2d.y();
    }
}
