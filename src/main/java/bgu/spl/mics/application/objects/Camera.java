package bgu.spl.mics.application.objects;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a camera sensor on the robot.
 * Responsible for detecting objects in the environment.
 */
public class Camera {
    private int id;
    private int frequency;
    private STATUS status;
    private List<StampedDetectedObjects> detectedObjects;
    private List<StampedDetectedObjects> allDetectedObjects;

    public Camera() {
        status = STATUS.UP;
        detectedObjects = new ArrayList<>();
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
        this.allDetectedObjects = allDetectedObjects;
    }

    /**
     * 
     * @param currentTime to look for objects in the database
     */
    public StampedDetectedObjects detectObjects(int time) {
        if (status == STATUS.UP) {
            for (int i = 0; i < allDetectedObjects.size(); i++) {
                if (time == allDetectedObjects.get(i).getTime()) {
                    // if (i == allDetectedObjects.size() - 1) {
                    //     status = STATUS.DOWN;
                    // }
                    detectedObjects.add(allDetectedObjects.get(i));
                    return allDetectedObjects.get(i);
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
        for (StampedDetectedObjects objs : detectedObjects) {
            if (objs.getTime() == time) {
                return objs;
            }
        }
        return null;
    }
}
