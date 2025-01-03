package bgu.spl.mics.application.objects;

import java.util.List;
import java.util.Map;

public class Cameras {
	private List<Camera> CamerasConfigurations;
    private String camera_datas_path;

	public Cameras() {}

    public List<Camera> getCamerasConfig() {
        return CamerasConfigurations;
    }

    public String getDataFilePath() {
        return camera_datas_path;
    }

    public void setDataFilePath(String filePath) {
        camera_datas_path = filePath;
    }

    public void updateCamerasAllDetectedObjects(CameraData cameraData) {
        Map<String, List<StampedDetectedObjects>> map = cameraData.getCameras();
        for (Camera camera : CamerasConfigurations) {
            List<StampedDetectedObjects> allDetectedObjects = map.get("camera" + camera.getId());
            camera.setAllDetectedObjects(allDetectedObjects);
        }
    }
}
