package com.google.gwt.gwtpages.client.page.event;

import com.google.gwt.gwtpages.client.Pages;

/**
 * Superclass for event handler interfaces.  All implementations must be one of:
 * <ul>
 * 	<li>{@link PageRequestEventHandler}</li>
 * 	<li>{@link PageErrorEventHandler}</li>
 * </ul>
 * @author Joe
 *
 */
public interface PageEventHandler {

	/**
	 * Intitialize this event handler to be used with the provide {@link Pages}
	 * instance
	 * 
	 * @param pages
	 *            the pages instance
	 */
	public void init(Pages pages);
}
