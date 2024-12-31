package bgu.spl.mics.application.objects;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Holds statistical information about the system's operation.
 * This class aggregates metrics such as the runtime of the system,
 * the number of objects detected and tracked, and the number of landmarks identified.
 */
public class StatisticalFolder {
    private static StatisticalFolder instance = null;
    private AtomicInteger systemRuntime;
    private AtomicInteger numDetectedObjects;
    private AtomicInteger numTrackedObjects;
    private AtomicInteger numLandmarks;

    private StatisticalFolder() {
        systemRuntime = new AtomicInteger(0);
        numDetectedObjects = new AtomicInteger(0);
        numTrackedObjects = new AtomicInteger(0);
        numLandmarks = new AtomicInteger(0);
    }

    public StatisticalFolder getInstance() { ////////////////////////////////
        if (instance == null) {
            instance = new StatisticalFolder();
        }
        return instance;
    }

    public int getSystemRuntime() {
        return systemRuntime;
    }

    public void increaseSystemRuntimeByNumOfTicks(int numOfTicks) {
        do {
            int oldVal = getSystemRuntime();
        } while (!systemRuntime.compareAndSet(oldVal, oldVal+numOfTicks));//////////////////////////////////// Is this correct?
    }

    public int getNumDetectedObjects() {
        return numDetectedObjects;
    }

    public void increaseNumDetectedObjects(int addMe) {
        do{
            int oldVal = getNumDetectedObjects();
        } while (!numDetectedObjects.compareAndSet(oldVal, oldVal + addMe));
    }

    public int getNumTrackedObjects() {
        return numTrackedObjects;
    }

    public void increaseNumTrackedObjects(int addMe) {
        do{
            int oldVal = getNumTrackedObjects();
        } while (!numTrackedObjects.compareAndSet(oldVal, oldVal + addMe));
    }

    public int getNumLandmarks() {
        return numLandmarks;
    }

    public void increaseNumLandmarks(int addMe) {
        do{
            int oldVal = getNumLandmarks();
        } while (!numLandmarks.compareAndSet(oldVal, oldVal + addMe));
    }
}