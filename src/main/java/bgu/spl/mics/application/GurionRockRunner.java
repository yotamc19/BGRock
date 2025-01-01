package bgu.spl.mics.application;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import bgu.spl.mics.application.objects.Camera;
import bgu.spl.mics.application.objects.Config;
import bgu.spl.mics.application.objects.DetectedObject;
import bgu.spl.mics.application.objects.LiDarDataBase;
import bgu.spl.mics.application.objects.StampedCloudPoints;
import bgu.spl.mics.application.objects.StampedDetectedObjects;
import bgu.spl.mics.application.services.CameraService;

/**
 * The main entry point for the GurionRock Pro Max Ultra Over 9000 simulation.
 * <p>
 * This class initializes the system and starts the simulation by setting up
 * services, objects, and configurations.
 * </p>
 */
public class GurionRockRunner {

    /**
     * The main method of the simulation.
     * This method sets up the necessary components, parses configuration files,
     * initializes services, and starts the simulation.
     *
     * @param args Command-line arguments. The first argument is expected to be the
     *             path to the configuration file.
     */
    public static void main(String[] args) {
        System.out.println("Hello World!");

        // TODO: Parse configuration file.
        // TODO: Initialize system components and services.
        // TODO: Start the simulation.

        List<StampedDetectedObjects> stampDetectedObjectsList = new ArrayList<>();
        Gson gson = new Gson();
        try {
            FileReader reader = new FileReader("example input/camera_data.json");
            Type stampDetectedObjectsType = new TypeToken<List<StampedDetectedObjects>>() {
            }.getType();
            stampDetectedObjectsList = gson.fromJson(reader, stampDetectedObjectsType);
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (StampedDetectedObjects obj : stampDetectedObjectsList) {
            for (DetectedObject o : obj.getDetectedObjects()) {
                System.out.println(o.toString());
            }
        }



        // String CONFIG_PATH = "example input/configuration_file.json";
        // Config config = getConfig(CONFIG_PATH);


        // create all camera services
        // List<Camera> cameras = config.getCameras().getcameras();
        // String filePath = config.getCameras().getcameraDataFilePath();
        // createCameraServices(cameras);
        
        
        // create all lidar services
        // create fusion slam service
        // create pose service
        // creation of time service and start
        
    }

    private static void createCameraServices(List<Camera> cameras) {
        for (Camera camera : cameras) {
            CameraService cameraService = new CameraService(camera);
            Thread thread = new Thread(cameraService);
            thread.start();
        }
    }

    private static Config getConfig(String filePath) {
        Gson gson = new Gson();
        try (FileReader reader = new FileReader(filePath)) {
            Config config = gson.fromJson(reader, Config.class);
            return config;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}