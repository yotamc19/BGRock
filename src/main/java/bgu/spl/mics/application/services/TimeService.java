package bgu.spl.mics.application.services;

import java.util.concurrent.TimeUnit;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.TerminatedBroadCast;
import bgu.spl.mics.application.messages.TickBroadcast;

/**
 * TimeService acts as the global timer for the system, broadcasting TickBroadcast messages
 * at regular intervals and controlling the simulation's duration.
 */
public class TimeService extends MicroService {
    private final int tickTime;
    private final int duration;
    private int currentTick;

    /**
     * Constructor for TimeService.
     *
     * @param TickTime  The duration of each tick in milliseconds.
     * @param Duration  The total number of ticks before the service terminates.
     */
    public TimeService(int tickTime, int duration) {
        super("TimeService");
        this.tickTime = tickTime;
        this.duration = duration;
        this.currentTick = 0;
    }

    /**
     * Initializes the TimeService.
     * Starts broadcasting TickBroadcast messages and terminates after the specified duration.
     */
    @Override
    protected void initialize() {
        Thread t = new Thread(() -> {
            while (currentTick < duration) {
                try {
                    TimeUnit.MILLISECONDS.sleep(tickTime);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }

                currentTick++;
                TickBroadcast tickBroadcast = new TickBroadcast(currentTick);
                sendBroadcast(tickBroadcast);
            }

            sendBroadcast(new TerminatedBroadCast()); // finished running the program
            terminate();
        });

        t.start();
    }
}