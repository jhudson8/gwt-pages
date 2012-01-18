package com.google.gwt.gwtpages.client.message.exceptions;

import com.google.gwt.gwtpages.client.page.loader.PageLoader;

/**
 * Exception thrown when a page was not located by the {@link PageLoader}
 * 
 * @author Joe Hudson
 */
public class PageNotFoundException extends Exception {
	private static final long serialVersionUID = 1L;

	private String pageToken;

	public PageNotFoundException(String pageToken) {
		super("Unknown page '" + pageToken + "'");
		this.pageToken = pageToken;
	}

	public String getPageToken() {
		return pageToken;
	}
}