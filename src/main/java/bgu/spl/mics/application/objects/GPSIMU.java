package bgu.spl.mics.application.objects;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the robot's GPS and IMU system.
 * Provides information about the robot's position and movement.
 */
public class GPSIMU {
    private int currentTick;
    private STATUS status;
    private List<Pose> poses;

    public GPSIMU() {
        currentTick = 0;
        status = STATUS.UP;
        poses = new ArrayList<>();
    }

    public int getCurrentTick() {
        return currentTick;
    }

    public void updateCurrentTick(int time) {
        currentTick = time;
    }

    public STATUS getStatus() {
        return status;
    }

    public void setStatus(STATUS status) {
        this.status = status;
    }

    public List<Pose> getPoses() {
        return poses;
    }

    public void addPose(Pose pose) {
        poses.add(pose);
    }
}
