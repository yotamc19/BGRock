package bgu.spl.mics.application.objects;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Holds statistical information about the system's operation.
 * This class aggregates metrics such as the runtime of the system,
 * the number of objects detected and tracked, and the number of landmarks
 * identified.
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

    public StatisticalFolder getInstance() {
        if (instance == null) {
            instance = new StatisticalFolder();
        }
        return instance;
    }

    public int getSystemRuntime() {
        return systemRuntime.get();
    }

    public void increaseSystemRuntimeByNumOfTicks(int numOfTicks) {
        int oldVal;
        do {
            oldVal = getSystemRuntime();
        } while (!systemRuntime.compareAndSet(oldVal, oldVal + numOfTicks));
    }

    public int getNumDetectedObjects() {
        return numDetectedObjects.get();
    }

    public void increaseNumDetectedObjects(int addMe) {
        int oldVal;
        do {
            oldVal = getNumDetectedObjects();
        } while (!numDetectedObjects.compareAndSet(oldVal, oldVal + addMe));
    }

    public int getNumTrackedObjects() {
        return numTrackedObjects.get();
    }

    public void increaseNumTrackedObjects(int addMe) {
        int oldVal;
        do {
            oldVal = getNumTrackedObjects();
        } while (!numTrackedObjects.compareAndSet(oldVal, oldVal + addMe));
    }

    public int getNumLandmarks() {
        return numLandmarks.get();
    }

    public void increaseNumLandmarks(int addMe) {
        int oldVal;
        do {
            oldVal = getNumLandmarks();
        } while (!numLandmarks.compareAndSet(oldVal, oldVal + addMe));
    }
}