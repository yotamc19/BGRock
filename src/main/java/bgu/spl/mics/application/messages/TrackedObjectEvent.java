package bgu.spl.mics.application.messages;

import java.util.List;
import bgu.spl.mics.Event;
import bgu.spl.mics.application.objects.TrackedObject;

public class TrackedObjectEvent implements Event<Boolean> {
	private final List<TrackedObject> trackedObjectsList;

	public TrackedObjectEvent(List<TrackedObject> trackedObjectsList) {
		this.trackedObjectsList = trackedObjectsList;
	}

	public List<TrackedObject> geTrackedObject() {
		return trackedObjectsList;
	}
}
