package com.google.gwt.gwtpages.client.page;

import com.google.gwt.gwtpages.client.PageRequestSession;
import com.google.gwt.gwtpages.client.Pages;
import com.google.gwt.gwtpages.client.page.loader.PageLoader;
import com.google.gwt.gwtpages.client.page.parameters.PageParameters;
import com.google.gwt.user.client.ui.Widget;

/**
 * An application defined page. The page must be registered with a
 * {@link PageLoader} which is set as the application resource loader using
 * {@link GWTPagesSettings#init(ApplicationPresenter, PageLoader, com.google.gwt.event.shared.HandlerManager)}
 * . The page represents a single viewable page in the application. The page
 * does not necessarily need to contain the full layout structure as the
 * {@link ApplicationPresenter} can be used to control that structure.
 * 
 * @author Joe Hudson
 * 
 */
public interface Page {

	/**
	 * Initialize this page for use with GWT Pages
	 * @param pages the pages settings
	 */
	public void init(Pages pages) throws Exception;

	/**
	 * Destroy this page when it is no longer used by the application.  If you are using the GWT Pages default
	 * behavior, this method will never be called as each page is used as a singleton.
	 * @param pages the pages settings
	 */
	public void destroy(Pages pages);

	/**
	 * Lifecycle event applied when the page is set as the current page. Note:
	 * Either {@link AsyncPageCallback#onSuccess()} or
	 * {@link AsyncPageCallback#onFailure(Throwable)} *must* be called.
	 * 
	 * @param parameters
	 *            the page parameters
	 * @param pageRequestData
	 *            {@link PageRequestSession}
	 */
	public void onEnterPage(PageParameters parameters,
			PageRequestSession session, AsyncPageCallback callback) throws Exception;

	/**
	 * Lifecycle event applied when the page is removed as the current page
	 * 
	 * @return true if this page should be removed from the page stack and false
	 *         otherwise
	 */
	public void onExitPage();

	/**
	 * Return the page component as a widget
	 */
	public Widget asWidget();
}
