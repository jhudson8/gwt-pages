package com.google.gwt.gwtpages.gin.client;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.inject.client.AbstractGinModule;

public abstract class PagesModule extends AbstractGinModule {

	@Override
	protected void configure() {
		bind(HandlerManager.class).toProvider(EventBusProvider.class);
	}
}
