package com.google.gwt.gwtpages.client.page.cache;

import java.util.HashMap;

import com.google.gwt.gwtpages.client.page.LoadedPageContainer;

/**
 * Simple implementation of the {@link PageCache}.  Using this, a single instance of all
 * pages will be cached.
 * 
 * @author Joe Hudson
 */
public class SimplePageCache implements PageCache {

	private HashMap<String, LoadedPageContainer> pages = new HashMap<String, LoadedPageContainer>();

	public LoadedPageContainer borrowPage(String pageToken) {
		return pages.get(pageToken);
	}

	public void returnPage(LoadedPageContainer pageContainer, boolean isInError) {
	}

	public void registerPage(String pageToken, LoadedPageContainer pageContainer) {
		pages.put(pageToken, pageContainer);
	}

}
