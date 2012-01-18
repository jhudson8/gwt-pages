package com.google.gwt.gwtpages.client.page.loader;

import com.google.gwt.gwtpages.client.page.LoadedPageContainer;

/**
 * Callback class used by the {@link PageLoader}. All page load requests are
 * callback-oriented so they can optionally provide async implementations.
 * 
 * @author Joe Hudson
 */
public interface PageLoadCallback {

	/**
	 * Method called when the page is located
	 * 
	 * @param pageLoadResult
	 *            the {@link LoadedPageContainer}
	 */
	void onPageFound(LoadedPageContainer page);

	/**
	 * Method called if the page could not be located
	 * 
	 * @param String
	 *            pageToken the page token
	 */
	void onPageNotFound(String pageToken);

	/**
	 * Method called if an error occured when loading the page
	 * 
	 * @param String
	 *            pageToken the page token
	 * @param cause
	 *            the thrown exception
	 */
	void onPageLoadFailure(String pageToken, Throwable cause);
}
