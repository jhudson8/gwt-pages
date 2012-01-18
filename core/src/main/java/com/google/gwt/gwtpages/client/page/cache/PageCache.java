package com.google.gwt.gwtpages.client.page.cache;

import com.google.gwt.gwtpages.client.page.LoadedPageContainer;

/**
 * Cacheing mechanism for GWT Pages.
 *
 * @author Joe Hudson
 */
public interface PageCache {

	public LoadedPageContainer borrowPage(String pageToken);

	public void returnPage(LoadedPageContainer pageContainer, boolean isInError);

	public void registerPage(String pageToken, LoadedPageContainer pageContainer);
}
