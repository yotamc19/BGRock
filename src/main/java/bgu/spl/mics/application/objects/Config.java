package bgu.spl.mics.application.objects;

import java.util.List;

public class Config {
    private Cameras cameras;
    private LiDarWorkers liDarWorkers;
    private String poseJsonFilePath;
    private int tickTime;
    private int duration;

    public Cameras getCameras() {
        return cameras;
    }

    // public void setCameras(Cameras cameras) {
    //     this.cameras = cameras;
    // }

    public LiDarWorkers getLiDarWorkers() {
        return liDarWorkers;
    }

    // public void setLiDarWorkers(LiDarWorkers liDarWorkers) {
    //     this.liDarWorkers = liDarWorkers;
    // }

    public String getPoseJsonFilePath() {
        return poseJsonFilePath;
    }

    // public void setPoseJsonFile(String poseJsonFile) {
    //     this.poseJsonFile = poseJsonFile;
    // }

    public int getTickTime() {
        return tickTime;
    }

    // public void setTickTime(int tickTime) {
    //     this.tickTime = tickTime;
    // }

    public int getDuration() {
        return duration;
    }

    // public void setDuration(int duration) {
    //     this.duration = duration;
    // }
}

class Cameras {
    private List<Camera> cameras;
    private String cameraDataFilePath;

    public List<Camera> getcameras() {
        return cameras;
    }

    // public void setcameras(List<Camera> cameras) {
    //     this.cameras = cameras;
    // }

    public String getcameraDataFilePath() {
        return cameraDataFilePath;
    }

    // public void setcameraDataFilePath(String cameraDataFilePath) {
    //     this.cameraDataFilePath = cameraDataFilePath;
    // }
}

class LiDarWorkers {
    private List<LiDarWorkerTracker> lidars;
    private String lidarsDataFilePath;

    public List<LiDarWorkerTracker> getlidars() {
        return lidars;
    }

    // public void setlidars(List<LiDarWorkerTracker> lidars) {
    //     this.lidars = lidars;
    // }

    public String getlidarsDataFilePath() {
        return lidarsDataFilePath;
    }

    // public void setlidarsDataFilePath(String lidarsDataFilePath) {
    //     this.lidarsDataFilePath = lidarsDataFilePath;
    // }
}
