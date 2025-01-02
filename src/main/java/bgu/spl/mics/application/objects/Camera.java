package bgu.spl.mics.application.objects;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a camera sensor on the robot.
 * Responsible for detecting objects in the environment.
 */
public class Camera {
    private final int id;
    private final int frequency;
    private STATUS status;
    private final List<StampedDetectedObjects> detectedObjs;
    private List<StampedDetectedObjects> allDetectedObjs;

    public Camera(int id, int frequency) {
        this.id = id;
        this.frequency = frequency;
        status = STATUS.UP;
        detectedObjs = new ArrayList<>();
    }

    /**
     * 
     * @return this camera id
     */
    public int getId() {
        return id;
    }

    /**
     * 
     * @return this camera frequency
     */
    public int getFrequency() {
        return frequency;
    }

    /**
     * 
     * @return this camera status
     */
    public STATUS getStatus() {
        return status;
    }

    /**
     * 
     * @param status to set the current camera status to
     */
    public void setStatus(STATUS status) {
        this.status = status;
    }

    public void setAllDetectedObjects(List<StampedDetectedObjects> allDetectedObjects) {
        this.allDetectedObjs = allDetectedObjects;
    }

    /**
     * 
     * @param currentTime to look for objects in the database
     */
    public StampedDetectedObjects detectObjects(int time) {
        if (status == STATUS.UP) {
            for (int i = 0; i < allDetectedObjs.size(); i++) {
                if (time == allDetectedObjs.get(i).getTime()) {
                    detectedObjs.add(allDetectedObjs.get(i));
                    return allDetectedObjs.get(i);
                }
            }
        }
        return null;
    }

    /**
     * 
     * @param time of the objects we want to get
     * @return the objects which were detected at time {@param time}
     */
    public StampedDetectedObjects getItemAtTime(int time) {
        for (StampedDetectedObjects objs : detectedObjs) {
            if (objs.getTime() == time) {
                return objs;
            }
        }
        return null;
    }
}