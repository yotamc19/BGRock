package bgu.spl.mics.application.objects;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages the fusion of sensor data for simultaneous localization and mapping
 * (SLAM).
 * Combines data from multiple sensors (e.g., LiDAR, camera) to build and update
 * a global map.
 * Implements the Singleton pattern to ensure a single instance of FusionSlam
 * exists.
 */
public class FusionSlam {
    private final List<LandMark> landmarks;
    private final List<Pose> poses;

    private static class FusionSlamSingletonHolder{
        private static FusionSlam instance = new FusionSlam();
    }

    private FusionSlam() {
        landmarks = new ArrayList<>();
        poses = new ArrayList<>();
    }

    /**
     * 
     * @return the singleton FusionSlam object
     */
    public FusionSlam getInstance() {
        return FusionSlamSingletonHolder.instance;
    }

    /**
     * 
     * @return all the current landmarks
     */
    public List<LandMark> getLandmarks() {
        return landmarks;
    }

    /**
     * 
     * @return all the past poses of the robot
     */
    public List<Pose> getPoses() {
        return poses;
    }

    /**
     * 
     * @param trackedObject represents the object which needs to get his position
     *                      updated
     */
    public void updatePosition(TrackedObject trackedObject) {
        LandMark trackedLandmark = getLandmarkById(trackedObject.getId());
        if (trackedLandmark == null) { // the landmark has been recognized before //? has not maybe
            trackedLandmark = new LandMark(
                    trackedObject.getId(),
                    trackedObject.getDescription(),
                    getGlobalCoordinates(trackedObject)
            // trackedObject.getCoordinates()////////////////// ? not the correct
            // coordinates?
            );
            landmarks.add(trackedLandmark);
        } else { // first time seeing this landmark // not first time maybe?
            // trackedLandmark.updateCoordinates(convertToGlobalCoordinates(trackedObject));
            trackedLandmark.updateCoordinates(getGlobalCoordinates(trackedObject));
        }
    }

    /**
     * 
     * @param pose which needs to be added to the current poses
     */
    public void updatePose(Pose pose) {
        poses.add(pose);
    }

    /**
     * helper function
     * 
     * @param id of the landmark we want to get
     * @return the landmark for the corresponding id if exists, null otherwise
     */
    private LandMark getLandmarkById(String id) {
        for (LandMark landmark : landmarks) {
            if (landmark.getId() == id) {
                return landmark;
            }
        }
        return null;
    }

    /**
     * helper function
     * 
     * @param trackedObject the object of which we want to convert the coordinates
     * @return the global coordinates resulting in calculations using the
     *         coresponding pose and the coordinates
     */
    private List<CloudPoint> getGlobalCoordinates(TrackedObject trackedObject) {
        int time = trackedObject.getTime();
        List<CloudPoint> prevCoordinatres = trackedObject.getCoordinates();
        Pose currentPose = null;
        for (Pose pose : poses) {
            if (pose.getTime() == time) {
                currentPose = pose;
                break;
            }
        }

        List<CloudPoint> globalCoordinates = new ArrayList<>();
        for (CloudPoint point : prevCoordinatres) {
            globalCoordinates.add(convertLocalPointToGlobalPoint(point, currentPose));
        }
        return globalCoordinates;
    }

    private CloudPoint convertLocalPointToGlobalPoint(CloudPoint point, Pose pose) {
        double xLocal = point.getX();
        double yLocal = point.getY();
        double xRobot = pose.getX();
        double yRobot = pose.getY();

        double yawInRadian = Math.toRadians(pose.getYaw());
        double cosinYaw = Math.cos(yawInRadian);
        double sinYaw = Math.sin(yawInRadian);
        double xGlobal = (cosinYaw * xLocal) - (sinYaw * yLocal) + xRobot;
        double yGlobal = (sinYaw * xLocal) + (cosinYaw * yLocal) + yRobot;

        return new CloudPoint(xGlobal, yGlobal, point.getZ());
    }
}