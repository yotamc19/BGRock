package bgu.spl.mics.application.objects;

/**
 * Represents the robot's pose (position and orientation) in the environment.
 * Includes x, y coordinates and the yaw angle relative to a global coordinate
 * system.
 */
public class Pose {
    private float x;
    private float y;
    private float yaw;
    private int time;

    public Pose(float x, float y, float yaw, int time) {
        this.x = x;
        this.y = y;
        this.yaw = yaw;
        this.time = time;
    }

    /**
     * 
     * @return this pose x coordinate
     */
    public float getX() {
        return x;
    }

    /**
     * 
     * @return this pose y coordinate
     */
    public float getY() {
        return y;
    }

    /**
     * 
     * @return this pose yaw
     */
    public float getYaw() {
        return yaw;
    }

    /**
     * 
     * @return this pose time
     */
    public int getTime() {
        return time;
    }
}