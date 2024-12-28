package bgu.spl.mics.application.objects;

import java.util.ArrayList;
import java.util.List;

/**
 * LiDarWorkerTracker is responsible for managing a LiDAR worker.
 * It processes DetectObjectsEvents and generates TrackedObjectsEvents by using data from the LiDarDataBase.
 * Each worker tracks objects and sends observations to the FusionSlam service.
 */
public class LiDarWorkerTracker {
    private final int id;
    private final int frequency;
    private STATUS status;
    private final List<TrackedObject> lastTrackedObjects;
    private final LiDarDataBase lidarDb;
    
    public LiDarWorkerTracker(int id, int frequency) {
        this.id = id;
        this.frequency = frequency;
        status = STATUS.UP;
        lastTrackedObjects = new ArrayList<>();

        String PATH_TO_LIDAR_DATA_FILE = "example input/lidar_data.json";
        lidarDb = LiDarDataBase.getInstance(PATH_TO_LIDAR_DATA_FILE);
    }

    public int getId() {
        return id;
    }

    public int getFrequency() {
        return frequency;
    }

    public STATUS getStatus() {
        return status;
    }

    public void setStatus(STATUS status) {
        this.status = status;
    }

    public List<TrackedObject> getLastTrackedObjects() {
        return lastTrackedObjects;
    }

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

    private void startTrackingObject(DetectedObject detectedObject, int time) {
        List<CloudPoint> coordinates = lidarDb.getCoordinatesById(detectedObject.getId());
        lastTrackedObjects.add(new TrackedObject(
            detectedObject.getId(),
            time,
            detectedObject.getDescription(),
            coordinates
        ));
    }

    private TrackedObject getTrackedObjectById(String id) {
        for (TrackedObject trackedObject : lastTrackedObjects) {
            if (trackedObject.getId() == id) {
                return trackedObject;
            }
        }
        return null;
    }
}
