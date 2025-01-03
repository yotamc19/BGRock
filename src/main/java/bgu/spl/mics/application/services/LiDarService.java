package bgu.spl.mics.application.services;

import java.util.ArrayList;
import java.util.List;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.CrashedBroadcast;
import bgu.spl.mics.application.messages.DetectObjectsEvent;
import bgu.spl.mics.application.messages.TerminatedBroadCast;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.messages.TrackedObjectsEvent;
import bgu.spl.mics.application.objects.LiDarWorkerTracker;
import bgu.spl.mics.application.objects.STATUS;
import bgu.spl.mics.application.objects.StatisticalFolder;
import bgu.spl.mics.application.objects.TrackedObject;

/**
 * LiDarService is responsible for processing data from the LiDAR sensor and
 * sending TrackedObjectsEvents to the FusionSLAM service.
 * 
 * This service interacts with the LiDarWorkerTracker object to retrieve and
 * process
 * cloud point data and updates the system's StatisticalFolder upon sending its
 * observations.
 */
public class LiDarService extends MicroService {
    private final LiDarWorkerTracker liDarWorkerTracker;
    private StatisticalFolder statisticalFolder;

    /**
     * Constructor for LiDarService.
     *
     * @param LiDarWorkerTracker A LiDAR Tracker worker object that this service
     *                           will use to process data.
     */
    public LiDarService(LiDarWorkerTracker liDarWorkerTracker) {
        super("LiDarWorkerTracker" + liDarWorkerTracker.getId());
        this.liDarWorkerTracker = liDarWorkerTracker;
        statisticalFolder = StatisticalFolder.getInstance();
    }

    /**
     * Initializes the LiDarService.
     * Registers the service to handle DetectObjectsEvents and TickBroadcasts,
     * and sets up the necessary callbacks for processing data.
     */
    @Override
    protected void initialize() {
        subscribeBroadcast(TickBroadcast.class, tickBroadcast -> {
            int currentTime = tickBroadcast.getTime();
            System.out.println(getName() + " " + currentTime);
            List<TrackedObject> trackedObjectsFound = new ArrayList<>();
            List<TrackedObject> lastTrackedObjects = liDarWorkerTracker.getLastTrackedObjects();
            for (TrackedObject trackedObject : lastTrackedObjects) {
                if (trackedObject.getTime() <= currentTime + liDarWorkerTracker.getFrequency()
                        && !trackedObject.getIsSentToFusion()) {
                    trackedObject.setIsSentToFusion(true);
                    trackedObjectsFound.add(trackedObject);
                }
            }
            if (trackedObjectsFound.size() > 0) {
                TrackedObjectsEvent e = new TrackedObjectsEvent(trackedObjectsFound);
                sendEvent(e);

                int lastSystemRuntime = statisticalFolder.getSystemRuntime();
                statisticalFolder.increaseSystemRuntimeByNumOfTicks(currentTime - lastSystemRuntime);
            }
        });

        subscribeEvent(DetectObjectsEvent.class, detectObjectsEvent -> {
            liDarWorkerTracker.trackObjects(detectObjectsEvent.getStampedDetectedObjects());
        });

        subscribeBroadcast(TerminatedBroadCast.class, terminatedBroadcast -> {
            liDarWorkerTracker.setStatus(STATUS.DOWN);
            terminate();
        });

        subscribeBroadcast(CrashedBroadcast.class, crashedBroadcast -> {
            liDarWorkerTracker.setStatus(STATUS.ERROR);
            terminate();
            // should also include why it crashed
        });
    }
}

/*
 * found object through camera with stampedcloudpoints time if 4
 * (should assume with time of 4 also in lidardb)
 * my camera has a frequency of 2
 * my camera will send detectedobject event in time 6
 * my lidar will accept the detectedobject event from my camera
 * Divide:
 * if my lidar has a frequency of 2 or smaller ->
 * it will instantly send trackedobject event to fusionslam
 * else ->
 * lets assume freq = 4,
 * then it will send trackedobject to fusion slam only on time 4 + 4 = 6
 */