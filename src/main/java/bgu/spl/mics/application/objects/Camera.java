package bgu.spl.mics.application.objects;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * Represents a camera sensor on the robot.
 * Responsible for detecting objects in the environment.
 */
public class Camera {
    private final int id;
    private final int frequency;
    private STATUS status;
    private final List<StampedDetectedObjects> detectedObjs;
    private final List<StampedDetectedObjects> allDetectedObjs;

    public Camera(int id, int frequency) {
        this.id = id;
        this.frequency = frequency;
        status = STATUS.UP;
        detectedObjs = new ArrayList<>();
        allDetectedObjs = loadDetectedObjectsFromFile();
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

    public void detectObjects(int currentTime) {
        if (status == STATUS.UP) {
            for (int i = 0; i < allDetectedObjs.size(); i++) {
                if (currentTime == allDetectedObjs.get(i).getTime()) {
                    detectedObjs.add(allDetectedObjs.get(i));
                    break;
                }
            }
        }
    }

    public StampedDetectedObjects getItemAtTime(int time) {
        for (StampedDetectedObjects objs : detectedObjs) {
            if (objs.getTime() == time) {
                return objs;
            }
        }
        return null;
    }

    private List<StampedDetectedObjects> loadDetectedObjectsFromFile() {
        Gson gson = new Gson();
        try {
            FileReader reader = new FileReader("camera_data.json");
            Type stampDetectedObjectsType = new TypeToken<List<StampedDetectedObjects>>() {
            }.getType();
            List<StampedDetectedObjects> stampDetectedObjectsList = gson.fromJson(reader, stampDetectedObjectsType);
            return stampDetectedObjectsList;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
