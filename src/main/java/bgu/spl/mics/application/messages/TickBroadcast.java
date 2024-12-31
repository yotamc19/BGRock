package bgu.spl.mics.application.messages;

import bgu.spl.mics.Broadcast;

public class TickBroadcast implements Broadcast {
	private final int currentTime;

	public TickBroadcast(int currentTime) {
		this.currentTime = currentTime;
	}

	/**
	 * @return the Tick broadcast current time
	 */
	public int getTime() {
		return currentTime;
	}
}