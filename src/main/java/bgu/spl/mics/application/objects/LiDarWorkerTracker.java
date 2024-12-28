package bgu.spl.mics.application.objects;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

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
    private final List<TrackedObject> allTrackedObjects;

    public LiDarWorkerTracker(int id, int frequency) {
        this.id = id;
        this.frequency = frequency;
        status = STATUS.UP;
        lastTrackedObjects = new ArrayList<>();
        allTrackedObjects = loadTrackedObjectsFromFile();
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

    public void trackObjects(int currentTime) {
        if (status == STATUS.UP) {
            for (int i = 0; i < allTrackedObjects.size(); i++) {
                if (currentTime == allTrackedObjects.get(i).getTime()) {
                    lastTrackedObjects.add(allTrackedObjects.get(i));
                }
            }
        }
    }

    public List<TrackedObject> getAllItemsAtTime(int time) {
        List<TrackedObject> list = new ArrayList<>();
        for (TrackedObject obj : allTrackedObjects) {
            if (obj.getTime() == time) {
                list.add(obj);
            }
        }
        return list;
    }

    private List<TrackedObject> loadTrackedObjectsFromFile() {
        Gson gson = new Gson();
        try (FileReader reader = new FileReader("lidar_data.json")) {
            Type trackedObjectType = new TypeToken<List<TrackedObject>>(){}.getType();
            List<TrackedObject> trackedObjectsList = gson.fromJson(reader, trackedObjectType);
            return trackedObjectsList;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
