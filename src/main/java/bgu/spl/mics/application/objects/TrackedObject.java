package bgu.spl.mics.application.objects;

import java.util.List;

/**
 * Represents an object tracked by the LiDAR.
 * This object includes information about the tracked object's ID, description,
 * time of tracking, and coordinates in the environment.
 */
public class TrackedObject {
    private final String id;
    private int time;
    private final String description;
    private final List<CloudPoint> coordinates;
    private boolean isSentToFusion;

    public TrackedObject(String id, int time, String description, List<CloudPoint> coordinates) {
        this.id = id;
        this.time = time;
        this.description = description;
        this.coordinates = coordinates;
        this.isSentToFusion = false;
    }

    public String getId() {
        return id;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public String getDescription() {
        return description;
    }

    public List<CloudPoint> getCoordinates() {
        return coordinates;
    }

    public boolean getIsSentToFusion() {
        return isSentToFusion;
    }

    public void setIsSentToFusion(boolean isSentToFusion) {
        this.isSentToFusion = isSentToFusion;
    }
}