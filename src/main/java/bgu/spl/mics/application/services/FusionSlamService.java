package bgu.spl.mics.application.services;

import java.io.FileWriter;
import java.io.IOException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.PoseEvent;
import bgu.spl.mics.application.messages.TerminatedBroadCast;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.messages.TrackedObjectsEvent;
import bgu.spl.mics.application.objects.FusionSlam;
import bgu.spl.mics.application.objects.Output;
import bgu.spl.mics.application.objects.StatisticalFolder;
import bgu.spl.mics.application.objects.TrackedObject;

/**
 * FusionSlamService integrates data from multiple sensors to build and update
 * the robot's global map.
 * 
 * This service receives TrackedObjectsEvents from LiDAR workers and PoseEvents
 * from the PoseService,
 * transforming and updating the map with new landmarks.
 */
public class FusionSlamService extends MicroService {
    private final FusionSlam fusionSlam;
    private StatisticalFolder statisticalFolder;

    /**
     * Constructor for FusionSlamService.
     *
     * @param fusionSlam The FusionSLAM object responsible for managing the global
     *                   map.
     */
    public FusionSlamService(FusionSlam fusionSlam) {
        super("FusionSlamService");
        this.fusionSlam = fusionSlam;
        this.statisticalFolder = StatisticalFolder.getInstance();
    }

    /**
     * Initializes the FusionSlamService.
     * Registers the service to handle TrackedObjectsEvents, PoseEvents, and
     * TickBroadcasts,
     * and sets up callbacks for updating the global map.
     */
    @Override
    protected void initialize() {
        subscribeEvent(TrackedObjectsEvent.class, trackObjectsEvent -> {
            for (TrackedObject trackedObject : trackObjectsEvent.getTrackedObjects()) {
                fusionSlam.updatePosition(trackedObject);
            }
        });

        subscribeEvent(PoseEvent.class, poseEvent -> {
            fusionSlam.updatePose(poseEvent.getPose());
        });

        subscribeBroadcast(TickBroadcast.class, tickBroadcast -> {
            statisticalFolder.increaseSystemRuntimeByNumOfTicks(1);
        });

        subscribeBroadcast(TerminatedBroadCast.class, terminatedBroadcast -> {
            terminate();

            Output output = new Output(
                    statisticalFolder.getSystemRuntime(),
                    statisticalFolder.getNumDetectedObjects(),
                    statisticalFolder.getNumTrackedObjects(),
                    statisticalFolder.getNumLandmarks(),
                    fusionSlam.getLandmarks());
                    
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            try (FileWriter writer = new FileWriter("output_file.json")) {
                gson.toJson(output, writer);
                System.out.println("Output have been written to output_file.json");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}