package bgu.spl.mics.application;

import bgu.spl.mics.application.objects.Camera;
import bgu.spl.mics.application.objects.CameraData;
import bgu.spl.mics.application.objects.Cameras;
import bgu.spl.mics.application.objects.Config;
import bgu.spl.mics.application.objects.FusionSlam;
import bgu.spl.mics.application.objects.GPSIMU;
import bgu.spl.mics.application.objects.LiDarWorkerTracker;
import bgu.spl.mics.application.objects.LiDarWorkers;
import bgu.spl.mics.application.services.CameraService;
import bgu.spl.mics.application.services.FusionSlamService;
import bgu.spl.mics.application.services.LiDarService;
import bgu.spl.mics.application.services.PoseService;
import bgu.spl.mics.application.services.TimeService;

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

        String CONFIG_PATH = "example input/configuration_file.json";
        Config config = new Config(CONFIG_PATH);

        // cameras setup
        Cameras cameras = config.getCameras();
        CameraData cameraData = new CameraData(cameras.getDataFilePath());
        cameras.updateCamerasAllDetectedObjects(cameraData);
        createAllCameraServices(cameras);

        // lidars setup
        LiDarWorkers lidars = config.getLiDarWorkers();
        createAllLidarServices(lidars);

        // fusion slam setup
        FusionSlam fusionSlam = FusionSlam.getInstance();
        createFusionSlamService(fusionSlam);

        // pose setup
        GPSIMU gpsimu = new GPSIMU(config.getPoseDataFilePath());
        createPoseService(gpsimu);

        // time setup
        createTimeService(config.getTickTime(), config.getDuration());
    }

    private static void createAllCameraServices(Cameras cameras) {
        for (Camera camera : cameras.getCamerasConfig()) {
            CameraService cameraService = new CameraService(camera);
            Thread thread = new Thread(cameraService, cameraService.getName());
            thread.start();
        }
    }

    private static void createAllLidarServices(LiDarWorkers lidars) {
        for (LiDarWorkerTracker lidar : lidars.getLidarsConfig()) {
            LiDarService lidarService = new LiDarService(lidar);
            Thread thread = new Thread(lidarService, lidarService.getName());
            thread.start();
        }
    }

    private static void createFusionSlamService(FusionSlam fusionSlam) {
        FusionSlamService fusionSlamService = new FusionSlamService(fusionSlam);
        Thread thread = new Thread(fusionSlamService, "FusionSlamService");
        thread.start();
    }

    private static void createPoseService(GPSIMU gpsimu) {
        PoseService poseService = new PoseService(gpsimu);
        Thread thread = new Thread(poseService, "PoseService");
        thread.start();
    }

    private static void createTimeService(int tickTime, int duration) {
        TimeService timeService = new TimeService(tickTime, duration);
        Thread thread = new Thread(timeService, "TimeService");
        thread.start();
    }
}