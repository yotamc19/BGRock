package bgu.spl.mics.application.objects;

/**
 * Holds statistical information about the system's operation.
 * This class aggregates metrics such as the runtime of the system,
 * the number of objects detected and tracked, and the number of landmarks identified.
 */
public class StatisticalFolder {
    private static StatisticalFolder instance = null;
    private int systemRuntime;
    private int numDetectedObjects;
    private int numTrackedObjects;
    private int numLandmarks;

    private StatisticalFolder() {
        systemRuntime = 0;
        numDetectedObjects = 0;
        numTrackedObjects = 0;
        numLandmarks = 0;
    }

    public StatisticalFolder getInstance() {
        if (instance == null) {
            instance = new StatisticalFolder();
        }
        return instance;
    }

    public int getSystemRuntime() {
        return systemRuntime;
    }

    public void updateSystemRuntimeByNumOfTicks(int numOfTicks) {
        systemRuntime += numOfTicks;
    }

    public int getNumDetectedObjects() {
        return numDetectedObjects;
    }

    public void incrementNumDetectedObjects() {
        numDetectedObjects++;
    }

    public int getNumTrackedObjects() {
        return numTrackedObjects;
    }

    public void incrementNumTrackedObjects() {
        numTrackedObjects++;
    }

    public int getNumLandmarks() {
        return numLandmarks;
    }

    public void incrementNumLandmarks() {
        numLandmarks++;
    }
}
