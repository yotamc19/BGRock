package bgu.spl.mics.application.services;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.PoseEvent;
import bgu.spl.mics.application.messages.TerminatedBroadCast;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.objects.GPSIMU;
import bgu.spl.mics.application.objects.Pose;
import bgu.spl.mics.application.objects.STATUS;

/**
 * PoseService is responsible for maintaining the robot's current pose (position
 * and orientation)
 * and broadcasting PoseEvents at every tick.
 */
public class PoseService extends MicroService {
    private final GPSIMU gpsimu;
    private int prevPoseEventTime;

    /**
     * Constructor for PoseService.
     *
     * @param gpsimu The GPSIMU object that provides the robot's pose data.
     */
    public PoseService(GPSIMU gpsimu) {
        super("PoseService");
        this.gpsimu = gpsimu;
        this.prevPoseEventTime = 0;
    }

    /**
     * Initializes the PoseService.
     * Subscribes to TickBroadcast and sends PoseEvents at every tick based on the
     * current pose.
     */
    @Override
    protected void initialize() {
        subscribeBroadcast(TickBroadcast.class, tickBroadcast -> {
            int currentTime = tickBroadcast.getTime();
            System.out.println(getName() + " " + currentTime);

            for (int i = prevPoseEventTime + 1; i <= currentTime; i++) {
                Pose pose = gpsimu.getPoseByTimeFromDb(i);
                gpsimu.updateCurrentTick(i);
                if (pose != null) {
                    gpsimu.addPose(pose);
                    PoseEvent e = new PoseEvent(pose, i);
                    sendEvent(e);
                }
            }
            prevPoseEventTime = currentTime;
        });

        subscribeBroadcast(TerminatedBroadCast.class, terminatedBroadcast -> {
            gpsimu.setStatus(STATUS.DOWN);
            terminate();
        });
    }
}