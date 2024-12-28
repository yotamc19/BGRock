package bgu.spl.mics.application.objects;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages the fusion of sensor data for simultaneous localization and mapping (SLAM).
 * Combines data from multiple sensors (e.g., LiDAR, camera) to build and update a global map.
 * Implements the Singleton pattern to ensure a single instance of FusionSlam exists.
 */
public class FusionSlam {
    private static FusionSlam instance = null;
    private final List<LandMark> landmarks;
    private final List<Pose> poses; 

    private FusionSlam() {
        landmarks = new ArrayList<>();
        poses = new ArrayList<>();
    }

    /**
     * 
     * @return the singleton FusionSlam object
     */
    public FusionSlam getInstance() {
        if (instance == null) {
            instance = new FusionSlam();
        }
        return instance;
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
     * @param trackedObject represents the object which needs to get his position updated
     */
    public void updatePosition(TrackedObject trackedObject) {
        LandMark trackedLandmark = getLandmarkById(trackedObject.getId());
        if (trackedLandmark == null) { // the landmark has been recognized before
            trackedLandmark = new LandMark(
                trackedObject.getId(),
                trackedObject.getDescription(),
                trackedObject.getCoordinates()
            );
            landmarks.add(trackedLandmark);
        } else { // first time seeing this landmark
            trackedLandmark.updateCoordinates(trackedObject.getCoordinates());
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
}
