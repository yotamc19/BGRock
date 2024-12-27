package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;
import bgu.spl.mics.application.objects.StampedDetectedObjects;

public class DetectObjectsEvent implements Event<Boolean> {
	private final StampedDetectedObjects stampedDetectedObjs;

	public DetectObjectsEvent(StampedDetectedObjects stampedDetectedObjs) {
		this.stampedDetectedObjs = stampedDetectedObjs;
	}

	public StampedDetectedObjects getStampedDetectedObjects() {
		return stampedDetectedObjs;
	}
}
