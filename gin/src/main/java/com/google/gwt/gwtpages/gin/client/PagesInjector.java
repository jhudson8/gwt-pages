package com.google.gwt.gwtpages.gin.client;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.gwtpages.client.Pages;
import com.google.gwt.gwtpages.client.message.Messages;
import com.google.gwt.inject.client.Ginjector;

public interface PagesInjector extends Ginjector {

	public Pages getPages();

	public Messages getMessages();

	public HandlerManager getEventBus();
}
