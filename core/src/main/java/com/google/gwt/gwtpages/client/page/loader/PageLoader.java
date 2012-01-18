package com.google.gwt.gwtpages.client.page.loader;

import java.util.Iterator;

import com.google.gwt.gwtpages.client.Pages;
import com.google.gwt.gwtpages.client.message.exceptions.PageNotFoundException;
import com.google.gwt.gwtpages.client.page.PageAttributes;
import com.google.gwt.gwtpages.client.page.parameters.Tokenizer;


/**
 * Interface class used to load pages based on their "token". The token
 * represents the full history token or partial history token (if accompanied by
 * additional parameters). The page token is identified by the
 * {@link Tokenizer}. The page loaded returns pages using a callback
 * mechanism so they can optionally be implemented in an async fashion.
 * 
 * Known implementations are {@link StandardPageLoader}, {@link AsyncPageLoader}, {@link CompositePageLoader}
 * 
 * @author Joe Hudson
 */
public interface PageLoader {

	public static final String PAGE_DEFAULT = "";
	public static final String PAGE_ERROR = "__error";
	public static final String PAGE_NOT_FOUND = "__notFound";
	public static final String PAGE_ILLEGAL_ACCESS = "__illegalAccess";

	/**
	 * Initialize the loader. This is where all pages would be registered.
	 */
	public void init(Pages settings);

	/**
	 * Return all known page tokens
	 */
	public Iterator<String> getValidPageTokens();

	/**
	 * Return true if the page token is mapped to a known page and false if not
	 * 
	 * @param pageToken
	 *            the page token
	 */
	public boolean isValidPageToken(String pageToken);

	/**
	 * Return the optional {@link PageAttributes} associated with this page token
	 * 
	 * @param pageToken
	 *            the page token
	 * @return the {@link PageAttributes} or null
	 * @throws PageNotFoundException
	 *             if no page was mapped to the provided page token
	 */
	public PageAttributes getPageAttributes(String pageToken)
			throws PageNotFoundException;

	/**
	 * Method to execute when requesting a page. One of the
	 * {@link PageLoadCallback} handler methods *must* be called depending on
	 * the state of the page request.
	 * 
	 * @param pageToken
	 *            the page token
	 * @param pageHandler
	 *            the {@link PageLoadCallback}
	 */
	public void getPage(String pageToken, PageLoadCallback pageHandler);
}