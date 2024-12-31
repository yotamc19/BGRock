package bgu.spl.mics.application.services;

import java.util.ArrayList;
import java.util.List;
import bgu.spl.mics.Future;
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
 * This service interacts with the LiDarWorkerTracker object to retrieve and process
 * cloud point data and updates the system's StatisticalFolder upon sending its
 * observations.
 */
public class LiDarService extends MicroService {
    private final LiDarWorkerTracker liDarWorkerTracker;
    private StatisticalFolder statisticalFolder;

    /**
     * Constructor for LiDarService.
     *
     * @param LiDarWorkerTracker A LiDAR Tracker worker object that this service will use to process data.
     */
    public LiDarService(LiDarWorkerTracker liDarWorkerTracker) {
        super("LiDarWorkerTracker" + liDarWorkerTracker.getId());
        this.liDarWorkerTracker = liDarWorkerTracker;
        // add statistics folder
        statisticalFolder = statisticalFolder.getInstance();
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
            List<TrackedObject> trackedObjectsFound = new ArrayList<>();
            List<TrackedObject> lastTrackedObjects = liDarWorkerTracker.getLastTrackedObjects();
            for (TrackedObject trackedObject : lastTrackedObjects) {
                if (trackedObject.getTime() <= currentTime - liDarWorkerTracker.getFrequency()) { /////////////////// - <
                    trackedObjectsFound.add(trackedObject);
                }
            }
            if (trackedObjectsFound.size() > 0) {
                TrackedObjectsEvent e = new TrackedObjectsEvent(trackedObjectsFound);
                Future<Boolean> f = sendEvent(e);
                // add some statistics
                int addedTrackedObjects = trackedObjectsFound.size();
                statisticalFolder.increaseNumTrackedObjects(addedTrackedObjects);
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