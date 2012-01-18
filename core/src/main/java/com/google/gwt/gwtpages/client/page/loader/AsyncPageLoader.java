package com.google.gwt.gwtpages.client.page.loader;

import java.util.HashMap;
import java.util.Iterator;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.gwtpages.client.message.exceptions.PageNotFoundException;
import com.google.gwt.gwtpages.client.page.LoadedPageContainer;
import com.google.gwt.gwtpages.client.page.Page;
import com.google.gwt.gwtpages.client.page.PageAttributes;

/**
 * Page loaded that uses a separate async request to lazy-load each page when
 * requested. You *must* instantiate this class using {@link GWT#create(Class)}.
 * 
 * @author Joe Hudson
 * 
 */
public abstract class AsyncPageLoader extends AbstractPageLoader {

	public static enum LoadType {
		INDIVIDUAL, COMPLETE;
	}

	private HashMap<String, Boolean> loadedPageFlag = new HashMap<String, Boolean>();
	private HashMap<String, Page> newPages = new HashMap<String, Page>();
	private LoadType loadType = LoadType.INDIVIDUAL;

	/**
	 * All pages registered with this loader will, by default, use a separate
	 * async call to load each individual page
	 */
	public AsyncPageLoader() {
	}

	/**
	 * Set the load type:
	 * 
	 * @param loadType
	 *            : LoadType.INDIVIDUAL will load each page with a separate
	 *            async call; LoadType.COMPLETE will use a single async call for
	 *            all pages registered with this loader
	 */
	public AsyncPageLoader setLoadType(LoadType loadType) {
		this.loadType = loadType;
		return this;
	}

	public void getPage(final String pageToken,
			final PageLoadCallback pageLoadCallback) {
		// has the page been loaded?
		if (null != loadedPageFlag.get(pageToken)) {
			// don't do an async load
			if (null != newPages.get(pageToken)) {
				// have we created the page during a async load for a differnet
				// page
				try {
					pageLoadCallback
							.onPageFound(new LoadedPageContainer(pageToken,
									newPages.get(pageToken),
									getPageAttributes(pageToken),
									AsyncPageLoader.this));
				} catch (PageNotFoundException e) {
					// we know the page is here
					pageLoadCallback.onPageFound(new LoadedPageContainer(
							pageToken, newPages.get(pageToken), null,
							AsyncPageLoader.this));
				}
				newPages.remove(pageToken);
			} else {
				// create a new page
				Page page = null;
				PageAttributes attributes = null;
				try {
					page = newInstance(pageToken);
					attributes = getPageAttributes(pageToken);
				} catch (PageNotFoundException e) {
					// we know the page exists because it was loaded before
				}
				pageLoadCallback.onPageFound(new LoadedPageContainer(pageToken,
						page, attributes, AsyncPageLoader.this));
			}
		}

		// do an async load
		if (isValidPageToken(pageToken)) {
			if (null == loadType || loadType == LoadType.INDIVIDUAL) {
				// only load a single page
				GWT.runAsync(new RunAsyncCallback() {
					public void onSuccess() {
						try {
							Page page = newInstance(pageToken);
							pageLoadCallback
									.onPageFound(new LoadedPageContainer(
											pageToken, page,
											getPageAttributes(pageToken),
											AsyncPageLoader.this));
							loadedPageFlag.put(pageToken, Boolean.TRUE);
						} catch (Throwable e) {
							pageLoadCallback.onPageLoadFailure(pageToken, e);
						}
					}

					public void onFailure(Throwable reason) {
						pageLoadCallback.onPageLoadFailure(pageToken, reason);
					}
				});
			} else {
				// load all pages at once
				GWT.runAsync(new RunAsyncCallback() {
					public void onSuccess() {
						Throwable pageException = null;
						LoadedPageContainer rtn = null;
						Iterator<String> pageTokens = getValidPageTokens();
						while (pageTokens.hasNext()) {
							String key = pageTokens.next();
							try {
								Page page = newInstance(key);
								loadedPageFlag.put(pageToken, Boolean.TRUE);
								if (key.equals(pageToken)) {
									// this is the page we are looking for
									rtn = new LoadedPageContainer(pageToken,
											page, getPageAttributes(pageToken),
											AsyncPageLoader.this);
								} else {
									newPages.put(pageToken, page);
								}
							} catch (Throwable e) {
								if (key.equals(pageToken)) {
									pageException = e;
								}
							}
						}
						if (null != rtn) {
							pageLoadCallback.onPageFound(rtn);
						} else {
							pageLoadCallback.onPageLoadFailure(pageToken,
									pageException);
						}
					}

					public void onFailure(Throwable reason) {
						pageLoadCallback.onPageLoadFailure(pageToken, reason);
					}
				});
			}
		} else {
			// the page wasn't found
			pageLoadCallback.onPageNotFound(pageToken);
		}
	}
}