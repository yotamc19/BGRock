package bgu.spl.mics.application.objects;

import java.util.ArrayList;
import java.util.List;

/**
 * LiDarWorkerTracker is responsible for managing a LiDAR worker.
 * It processes DetectObjectsEvents and generates TrackedObjectsEvents by using
 * data from the LiDarDataBase.
 * Each worker tracks objects and sends observations to the FusionSlam service.
 */
public class LiDarWorkerTracker {
    private int id;
    private int frequency;
    private STATUS status;
    private List<TrackedObject> lastTrackedObjects;
    private LiDarDataBase lidarDb;
    private StatisticalFolder statisticalFolder;

    public LiDarWorkerTracker() {
        status = STATUS.UP;
        lastTrackedObjects = new ArrayList<>();
        statisticalFolder = StatisticalFolder.getInstance();
    }
    
    /**
     * 
     * @return this lidar id
     */
    public int getId() {
        return id;
    }

    /**
     * 
     * @return this lidar frequency
     */
    public int getFrequency() {
        return frequency;
    }

    /**
     * 
     * @return this lidar status
     */
    public STATUS getStatus() {
        return status;
    }

    /**
     * 
     * @param status to set this lidar status to
     */
    public void setStatus(STATUS status) {
        this.status = status;
    }

    public void updateDbFilePath(String filePath) {
        lidarDb = LiDarDataBase.getInstance(filePath);
    }

    /**
     * 
     * @return this lidar tracked objects
     */
    public List<TrackedObject> getLastTrackedObjects() {
        if (lastTrackedObjects == null) {
            lastTrackedObjects = new ArrayList<>();
        }
        return lastTrackedObjects;
    }

    /**
     * 
     * @param stampedDetectedObjects which this lidar will need to track from now on
     */
    public void trackObjects(StampedDetectedObjects stampedDetectedObjects) {
        if (status == STATUS.UP) {
            int time = stampedDetectedObjects.getTime();
            for (DetectedObject detectedObject : stampedDetectedObjects.getDetectedObjects()) {
                String currentId = detectedObject.getId();
                TrackedObject trackedObject = getTrackedObjectById(currentId);
                if (trackedObject == null) { // object is not tracked yet
                    startTrackingObject(detectedObject, time);
                } else { // object already tracked
                    trackedObject.setTime(time);
                }
            }
        }
    }

    /**
     * helper function
     * 
     * @param detectedObject of the object that this lidar needs to keep track on
     *                       from now on
     * @param time
     */
    private void startTrackingObject(DetectedObject detectedObject, int time) {
        List<CloudPoint> coordinates = lidarDb.getCoordinatesById(detectedObject.getId());
        lastTrackedObjects.add(new TrackedObject(
                detectedObject.getId(),
                time,
                detectedObject.getDescription(),
                coordinates));
        statisticalFolder.increaseNumTrackedObjects(1);
    }

    /**
     * helper function
     * 
     * @param id to find the trackedobject by
     * @return the trackedObject with the recieved id
     */
    private TrackedObject getTrackedObjectById(String id) {
        for (TrackedObject trackedObject : lastTrackedObjects) {
            if (trackedObject.getId() == id) {
                return trackedObject;
            }
        }
        return null;
    }
}