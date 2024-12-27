package bgu.spl.mics.application.messages;

import java.util.List;

import bgu.spl.mics.Event;
import bgu.spl.mics.application.objects.StampedDetectedObjects;

public class DetectObjectsEvent implements Event<Boolean> {
	private final List<StampedDetectedObjects> stampedDetectedObjectsList;

	public DetectObjectsEvent(List<StampedDetectedObjects> stampedDetectedObjectsList) {
		this.stampedDetectedObjectsList = stampedDetectedObjectsList;
	}

	public List<StampedDetectedObjects> getStampedDetectedObjects() {
		return stampedDetectedObjectsList;
	}
}
