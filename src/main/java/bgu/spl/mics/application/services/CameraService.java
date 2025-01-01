package bgu.spl.mics.application.services;

import bgu.spl.mics.Future;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.CrashedBroadcast;
import bgu.spl.mics.application.messages.DetectObjectsEvent;
import bgu.spl.mics.application.messages.TerminatedBroadCast;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.objects.Camera;
import bgu.spl.mics.application.objects.STATUS;
import bgu.spl.mics.application.objects.StampedDetectedObjects;
import bgu.spl.mics.application.objects.StatisticalFolder;

/**
 * CameraService is responsible for processing data from the camera and
 * sending DetectObjectsEvents to LiDAR workers.
 * 
 * This service interacts with the Camera object to detect objects and updates
 * the system's StatisticalFolder upon sending its observations.
 */
public class CameraService extends MicroService {
    private final Camera camera;
    private StatisticalFolder statisticalFolder;

    /**
     * Constructor for CameraService.
     *
     * @param camera The Camera object that this service will use to detect objects.
     */
    public CameraService(Camera camera) {
        super("Camera" + camera.getId());
        this.camera = camera;
        statisticalFolder = statisticalFolder.getInstance();////////////////////// yes?
    }

    /**
     * Initializes the CameraService.
     * Registers the service to handle TickBroadcasts and sets up callbacks for
     * sending
     * DetectObjectsEvents.
     */
    @Override
    protected void initialize() {
        subscribeBroadcast(TickBroadcast.class, tickBroadcast -> {
            int currentTime = tickBroadcast.getTime();
            StampedDetectedObjects stampedDetectedObjects = camera.detectObjects(currentTime + camera.getFrequency());
            if (stampedDetectedObjects != null) {
                DetectObjectsEvent e = new DetectObjectsEvent(stampedDetectedObjects);
                Future<Boolean> f = sendEvent(e);
                // add some statistics
                int addedDetectedObjects = stampedDetectedObjects.getDetectedObjects().size();
                statisticalFolder.increaseNumDetectedObjects(addedDetectedObjects);
            }
        });

        subscribeBroadcast(TerminatedBroadCast.class, terminatedBroadcast -> {
            camera.setStatus(STATUS.DOWN);
            terminate();
        });

        subscribeBroadcast(CrashedBroadcast.class, crashedBroadcast -> {
            camera.setStatus(STATUS.ERROR);
            terminate();
            // should also include why it crashed
        });
    }
}