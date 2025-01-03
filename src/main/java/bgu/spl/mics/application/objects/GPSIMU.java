package bgu.spl.mics.application.objects;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * Represents the robot's GPS and IMU system.
 * Provides information about the robot's position and movement.
 */
public class GPSIMU {
    private int currentTick;
    private STATUS status;
    private List<Pose> poses;
    private List<Pose> allPoses;

    public GPSIMU(String filePath) {
        currentTick = 0;
        status = STATUS.UP;
        poses = new ArrayList<>();
        allPoses = loadPosesFromFile(filePath);
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

    public Pose getPoseByTimeFromDb(int time) {
        for (Pose pose : allPoses) {
            if (time == pose.getTime()) {
                return pose;
            }
        }
        return null;
    }

    private List<Pose> loadPosesFromFile(String filePath) {
        Gson gson = new Gson();
        try {
            // FileReader reader = new FileReader(filePath);
            FileReader reader = new FileReader("example input/pose_data.json");
            Type posesType = new TypeToken<List<Pose>>() {
            }.getType();
            List<Pose> posesList = gson.fromJson(reader, posesType);
            return posesList;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}