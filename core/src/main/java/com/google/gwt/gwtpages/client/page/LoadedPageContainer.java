package com.google.gwt.gwtpages.client.page;

import com.google.gwt.gwtpages.client.page.loader.PageLoader;

/**
 * Entity class used to contain a {@link Page} and associated data.
 * 
 * @author Joe Hudson
 */
public class LoadedPageContainer {

	private String pageToken;
	private Page page;
	private PageAttributes attributes;
	private PageLoader pageLoader;

	/**
	 * Default constructor
	 */
	public LoadedPageContainer() {
	}

	/**
	 * Default constructor
	 */
	public LoadedPageContainer(Page page) {
		this.page = page;
	}

	/**
	 * @param pageToken
	 *            the specific segment of the history token which identifies the
	 *            page
	 * @param page
	 *            the loaded {@link Page}
	 * @param attributes
	 *            the {@link PageAttributes} provided by the {@link PageLoader}
	 * @param pageLoader
	 *            the {@link PageLoader} used to actually load the page (useful
	 *            with nested page loaders)
	 */
	public LoadedPageContainer(String pageToken, Page presenter, PageAttributes attributes,
			PageLoader pageLoader) {
		this.pageToken = pageToken;
		this.page = presenter;
		this.attributes = attributes;
		this.pageLoader = pageLoader;
	}

	/**
	 * Return the the specific segment of the history token which identifies the
	 * page
	 */
	public String getPageToken() {
		return pageToken;
	}

	/**
	 * Set the specific segment of the history token which identifies the page
	 * 
	 * @param pageToken
	 *            the page token
	 */
	public void setPageToken(String pageToken) {
		this.pageToken = pageToken;
	}

	/**
	 * Return the {@link Page}
	 */
	public Page getPage() {
		return page;
	}

	/**
	 * Set the {@link Page}
	 */
	public void setPage(Page page) {
		this.page = page;
	}

	/**
	 * Return the {@link PageAttributes}
	 */
	public PageAttributes getAttributes() {
		return attributes;
	}

	/**
	 * Set the {@link PageAttributes}
	 */
	public void setAttributes(PageAttributes attributes) {
		this.attributes = attributes;
	}

	/**
	 * Return the {@link PageLoader}
	 */
	public PageLoader getPageLoader() {
		return pageLoader;
	}

	/**
	 * Set the {@link PageLoader}
	 */
	public void setPageLoader(PageLoader pageLoader) {
		this.pageLoader = pageLoader;
	}

	/**
	 * Return a duplicate copy of this page load result
	 */
	public LoadedPageContainer copy() {
		return new LoadedPageContainer(pageToken, page, (null != attributes)?attributes.copy():null, pageLoader);
	}
}