package bgu.spl.mics.application;

import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputFilter.Config;

import com.google.gson.Gson;

import bgu.spl.mics.application.objects.LiDarDataBase;
import bgu.spl.mics.application.objects.StampedCloudPoints;

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

        String CONFIG_PATH = "example input/configuration_file.json";
        LiDarDataBase liDarDataBase = LiDarDataBase.getInstance(path);

        // Print all stamped cloud points
        for (StampedCloudPoints point : liDarDataBase.getCloudPoints()) {
            System.out.println(point);
        }
    }

    private Object getConfig(String filePath) {
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