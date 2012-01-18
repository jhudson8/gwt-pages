package com.google.gwt.gwtpages.gin.client.page.loader;

import com.google.gwt.gwtpages.client.page.LoadedPageContainer;
import com.google.gwt.gwtpages.client.page.Page;
import com.google.gwt.gwtpages.client.page.loader.AbstractPageLoader;
import com.google.gwt.gwtpages.client.page.loader.PageLoadCallback;

public abstract class StandardGinPageLoader extends AbstractPageLoader implements GinPageLoader {

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