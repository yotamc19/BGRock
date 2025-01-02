package bgu.spl.mics.application.objects;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import com.google.gson.Gson;

public class Config {
    private Cameras Cameras;
    private LiDarWorkers LiDarWorkers;
    private String poseJsonFile;
    private int TickTime;
    private int Duration;

    public Config(String filePath) {
        Gson gson = new Gson();
        try (FileReader reader = new FileReader(filePath)) {
            Config config = gson.fromJson(reader, Config.class);
            this.Cameras = config.getCameras();
            // this.Cameras.setDataFilePath(getAbsolutePathFromRelative(Cameras.getDataFilePath()));
            this.LiDarWorkers = config.getLiDarWorkers();
            // this.LiDarWorkers.setDataFilePath(getAbsolutePathFromRelative(LiDarWorkers.getDataFilePath()));
            for (LiDarWorkerTracker lidar : LiDarWorkers.getLidarsConfig()) {
                // lidar.updateDbFilePath(LiDarWorkers.getDataFilePath());
                lidar.updateDbFilePath("example input/lidar_data.json");
            }
            this.poseJsonFile = getAbsolutePathFromRelative(config.getPoseDataFilePath());
            this.TickTime = config.getTickTime();
            this.Duration = config.getDuration();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Cameras getCameras() {
        return Cameras;
    }

    public LiDarWorkers getLiDarWorkers() {
        return LiDarWorkers;
    }

    public String getPoseDataFilePath() {
        return poseJsonFile;
    }

    public int getTickTime() {
        return TickTime;
    }

    public int getDuration() {
        return Duration;
    }

    public static String getAbsolutePathFromRelative(String relativePath) {
        Path currentFile = Paths.get("./Config.java");
        Path configDirectory = currentFile.toAbsolutePath();
        Path givenRelativePath = Paths.get(relativePath);
        Path resolvedPath = configDirectory.resolve(givenRelativePath).normalize();

        return resolvedPath.toString();
    }
}