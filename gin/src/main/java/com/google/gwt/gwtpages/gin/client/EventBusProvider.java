package com.google.gwt.gwtpages.gin.client;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.gwtpages.client.Pages;
import com.google.inject.Provider;

public class EventBusProvider implements Provider<HandlerManager> {

	public HandlerManager get() {
		return Pages.get().getEventBus();
	}
}
