package bgu.spl.mics.application.services;

import java.util.List;
import bgu.spl.mics.Future;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.CrashedBroadcast;
import bgu.spl.mics.application.messages.DetectObjectsEvent;
import bgu.spl.mics.application.messages.TerminatedBroadCast;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.messages.TrackedObjectEvent;
import bgu.spl.mics.application.objects.LiDarWorkerTracker;
import bgu.spl.mics.application.objects.TrackedObject;
import bgu.spl.mics.application.statuses.LiDarStatus;

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

    /**
     * Constructor for LiDarService.
     *
     * @param LiDarWorkerTracker A LiDAR Tracker worker object that this service will use to process data.
     */
    public LiDarService(LiDarWorkerTracker liDarWorkerTracker) {
        super("LiDarWorkerTracker" + liDarWorkerTracker.getId());
        this.liDarWorkerTracker = liDarWorkerTracker;
        // add statistics folder
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
            liDarWorkerTracker.trackObjects(currentTime);

            int timeToSend = currentTime - liDarWorkerTracker.getFrequency();
            if (timeToSend > 0) {
                List<TrackedObject> trackedObjectsList = liDarWorkerTracker.getAllItemsAtTime(timeToSend);
                for (TrackedObject obj : trackedObjectsList) {
                    TrackedObjectEvent e = new TrackedObjectEvent(obj);
                    Future<Boolean> f = sendEvent(e);
                    // add some statistics about f
                }
            }
        });
        subscribeEvent(DetectObjectsEvent.class, detectObjectsEvent -> {
            // rethink here
            // liDarWorkerTracker.trackObjects(detectObjectsEvent.getStampedDetectedObjects());
        });
        subscribeBroadcast(TerminatedBroadCast.class, terminatedBroadcast -> {
            liDarWorkerTracker.setStatus(LiDarStatus.Down);
            terminate();
        });
        subscribeBroadcast(CrashedBroadcast.class, crashedBroadcast -> {
            liDarWorkerTracker.setStatus(LiDarStatus.Error);
            terminate();
             // should also include why it crashed
        });
    }
}
