package bgu.spl.mics.application.objects;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Output {
	private int systemRuntime;
	private int numDetectedObjects;
	private int numTrackedObjects;
	private int numLandmarks;
	private Map<String, LandMark> landMarks;

	public Output(int systemRuntime, int numDetectedObjects, int numTrackedObjects, int numLandmarks,
			List<LandMark> landMarks) {
		this.systemRuntime = systemRuntime;
		this.numDetectedObjects = numDetectedObjects;
		this.numTrackedObjects = numTrackedObjects;
		this.numLandmarks = numLandmarks;
		this.landMarks = new HashMap<String, LandMark>();

		for (LandMark landmark : landMarks) {
			this.landMarks.put(landmark.getId(), landmark);
		}
	}
}
