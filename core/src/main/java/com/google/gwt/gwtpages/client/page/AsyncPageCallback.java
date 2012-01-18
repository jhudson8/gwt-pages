package com.google.gwt.gwtpages.client.page;

import java.io.Serializable;

import com.google.gwt.gwtpages.client.GotoPageCommand;
import com.google.gwt.gwtpages.client.page.event.PageEventHandler;

public interface AsyncPageCallback {

	/**
	 * Event called when the page load is complete and successful
	 */
	void onSuccess();

	/**
	 * Event called if an error occurs while loading the page
	 * 
	 * @param cause
	 *            the thrown exception
	 */
	void onFailure(Throwable cause);

	/**
	 * This is used to communicate to event handlers that the requested page can not be visited
	 * @param exception optional exception
	 * @param message optional exception message
	 */
	void onIllegalAccess(Serializable... parameters);

	/**
	 * This is a way to send a command to all
	 * {@link PageEventHandler#onPageWaitForAsync(LoadedPageContainer, com.google.gwt.gwtpages.client.page.parameters.PageParameters, com.google.gwt.gwtpages.client.GotoPageCommand)}
	 */
	void waitForAsync();

	/**
	 * Forward the current request to another page command.  This will complete the current page lifecycle and force the history token to not be modified
	 * on the forward regardless of what the {@link GotoPageCommand} describes.
	 * 
	 * @param command the gotopage command
	 */
	void forward(GotoPageCommand command);
}