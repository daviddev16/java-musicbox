package org.musicbox.core.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class Listeners {

	private static Listeners globalListeners;

	private List<Listener> listeners;

	public Listeners() {
		this.listeners = new ArrayList<>();
		globalListeners = this;
	}

	public static void register(Listener... listeners) {
		Arrays.stream(listeners).filter(listener -> listener != null)
		.forEach(listener -> getListenersList().add(listener));
	}

	private static List<Listener> getListenersList() {
		return globalListeners.listeners;
	}

	public static Listener[] getAllListeners() {
		return globalListeners.listeners.toArray(new Listener[globalListeners.listeners.size()]);
	}

	public static void setup() {
		new Listeners();
	}

}
