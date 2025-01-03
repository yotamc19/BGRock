package bgu.spl.mics.application.objects;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class CameraData {
	private Map<String, List<StampedDetectedObjects>> cameras;

	public CameraData(String filePath) {
        Gson gson = new Gson();
        // try (FileReader reader = new FileReader(filePath)) {
        try (FileReader reader = new FileReader("example input/camera_data.json")) {
            Type cameraMapType = new TypeToken<Map<String, List<StampedDetectedObjects>>>() {
            }.getType();
            Map<String, List<StampedDetectedObjects>> map = gson.fromJson(reader, cameraMapType);
            this.cameras = map;
        } catch (IOException e) {
            e.printStackTrace();
        }
	}

	public Map<String, List<StampedDetectedObjects>> getCameras() {
		return cameras;
	}
}
