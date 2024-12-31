package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;
import bgu.spl.mics.application.objects.Pose;

public class PoseEvent implements Event<Void> {
	private final Pose pose;
	private final int time;

	public PoseEvent(Pose pose, int time) {
		this.pose = pose;
		this.time = time;
	}

	/**
	 * 
	 * @return this event pose
	 */
	public Pose getPose() {
		return pose;
	}

	/**
	 * 
	 * @return this event time
	 */
	public int getTime() {
		return time;
	}
}