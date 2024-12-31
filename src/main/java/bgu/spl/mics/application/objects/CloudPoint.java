package bgu.spl.mics.application.objects;

/**
 * CloudPoint represents a specific point in a 3D space as detected by the LiDAR.
 * These points are used to generate a point cloud representing objects in the environment.
 */
public class CloudPoint {
    private final double x;
    private final double y;
    private final double z;

    public CloudPoint(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * 
     * @return x coordinate
     */
    public double getX() {
        return x;
    }

    /**
     * 
     * @return y coordinate
     */
    public double getY() {
        return y;
    }

    /**
     * 
     * @return z coordinate
     */
    public double getZ() {
        return z;
    }
}