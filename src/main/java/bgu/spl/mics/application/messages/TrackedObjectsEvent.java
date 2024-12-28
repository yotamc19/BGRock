package bgu.spl.mics.application.messages;

import java.util.List;
import bgu.spl.mics.Event;
import bgu.spl.mics.application.objects.TrackedObject;

public class TrackedObjectsEvent implements Event<Boolean> {
	private final List<TrackedObject> trackedObjectsList;

	public TrackedObjectsEvent(List<TrackedObject> trackedObjectsList) {
		this.trackedObjectsList = trackedObjectsList;
	}

	/**
	 * 
	 * @return the list of objects to keep track of
	 */
	public List<TrackedObject> getTrackedObjects() {
		return trackedObjectsList;
	}
}
