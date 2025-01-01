package bgu.spl.mics.application.services;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.PoseEvent;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.objects.GPSIMU;
import bgu.spl.mics.application.objects.Pose;

/**
 * PoseService is responsible for maintaining the robot's current pose (position
 * and orientation)
 * and broadcasting PoseEvents at every tick.
 */
public class PoseService extends MicroService {
    private final GPSIMU gpsimu;

    /**
     * Constructor for PoseService.
     *
     * @param gpsimu The GPSIMU object that provides the robot's pose data.
     */
    public PoseService(GPSIMU gpsimu) {
        super("PoseService");
        this.gpsimu = gpsimu;
    }

    /**
     * Initializes the PoseService.
     * Subscribes to TickBroadcast and sends PoseEvents at every tick based on the
     * current pose.
     */
    @Override
    protected void initialize() {
        subscribeBroadcast(TickBroadcast.class, tickBroadcast -> {
            /**
             * get the current pose from the json file
             * send a poseEvent to fusionSlamService- current pose, current time
             * should handle terminated?
             */

            int currentTime = tickBroadcast.getTime();
            Pose pose = gpsimu.getPoseByTimeFromDb(currentTime);
            gpsimu.updateCurrentTick(currentTime);
            gpsimu.addPose(pose);
            PoseEvent e = new PoseEvent(pose, currentTime);
            sendEvent(e);
        });
    }
}