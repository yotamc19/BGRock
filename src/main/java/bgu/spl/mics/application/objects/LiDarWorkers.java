package bgu.spl.mics.application.objects;

import java.util.List;

public class LiDarWorkers {
	private List<LiDarWorkerTracker> LidarConfigurations;
    private String lidars_data_path;

	public LiDarWorkers() {}

    public List<LiDarWorkerTracker> getLidarsConfig() {
        return LidarConfigurations;
    }

    public String getDataFilePath() {
        return lidars_data_path;
    }

    public void setDataFilePath(String filePath) {
        lidars_data_path = filePath;
    }
}
