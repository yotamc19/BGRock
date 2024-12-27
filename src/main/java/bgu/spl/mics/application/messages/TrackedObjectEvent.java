package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;
import bgu.spl.mics.application.objects.TrackedObject;

public class TrackedObjectEvent implements Event<Boolean> {
	private final TrackedObject trackedObject;

	public TrackedObjectEvent(TrackedObject trackedObject) {
		this.trackedObject = trackedObject;
	}

	public TrackedObject geTrackedObject() {
		return trackedObject;
	}
}
