package com.google.gwt.gwtpages.client.page.loader;

import com.google.gwt.gwtpages.client.page.LoadedPageContainer;
import com.google.gwt.gwtpages.client.page.Page;

/**
 * Simple {@link PageLoader} implementation. {@link Page} instances are
 * registered for specific page tokens.
 * 
 * @author Joe Hudson
 */
public abstract class StandardPageLoader extends AbstractPageLoader {

	public void getPage(String pageToken, PageLoadCallback pageHandler) {
		if (isValidPageToken(pageToken)) {
			try {
				Page p = newInstance(pageToken);
				pageHandler.onPageFound(new LoadedPageContainer(pageToken, p,
						getPageAttributes(pageToken), this));
			} catch (Exception e) {
				pageHandler.onPageLoadFailure(pageToken, e);
			}
		} else {
			pageHandler.onPageNotFound(pageToken);
		}
	}
}