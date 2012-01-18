package com.google.gwt.gwtpages.client.page.event;

import com.google.gwt.gwtpages.client.GotoPageCommand;
import com.google.gwt.gwtpages.client.PageRequestSession;
import com.google.gwt.gwtpages.client.page.ApplicationPresenter;
import com.google.gwt.gwtpages.client.page.AsyncPageCallback;
import com.google.gwt.gwtpages.client.page.LoadedPageContainer;
import com.google.gwt.gwtpages.client.page.Page;
import com.google.gwt.gwtpages.client.page.loader.PageLoader;
import com.google.gwt.gwtpages.client.page.parameters.PageParameters;

/**
 * {@link Page} lifecycle event handler interface. Order of events are:
 * <ol>
 * <li>
 * {@link PageEventHandler#onPageRequest(String, String, GWTPageAttributes, PageRequestSession)}
 * </li>
 * <li> {@link PageEventHandler#onPageLoaded(LoadedPageContainer)} (usually only
 * once when before the page is cached)</li>
 * <li>
 * {@link PageEventHandler#onBeforePageEnter(LoadedPageContainer, PageParameters, PageRequestSession)}
 * </li>
 * <li>
 * {@link PageEventHandler#onAfterPageEnter(LoadedPageContainer, PageParameters, PageRequestSession)}
 * </li>
 * <li>
 * {@link PageEventHandler#onPageEnterSuccess(LoadedPageContainer, PageParameters, PageRequestSession)}
 * </li>
 * 
 * @author Joe Hudson
 */
public interface PageRequestEventHandler extends PageEventHandler {

	/**
	 * Event fired after the page
	 * {@link SyncPage#onShowPage(PageParameters, PageRequestSession)} or
	 * {@link AsyncPage#onEnterPage(PageParameters, PageRequestSession, AsyncPageCallback)}
	 * methods are called.
	 * 
	 * @param pageLoadResult
	 *            the page and associated data
	 * @param parameters
	 *            the page parameters
	 * @param command
	 *            {@link GotoPageCommand}
	 */
	public void onAfterPageEnter(LoadedPageContainer pageLoadResult,
			PageParameters parameters, GotoPageCommand command);

	/**
	 * Event fired after the page has been sucessfully rendered including all
	 * sync or async data requests that are required to show the page. With an
	 * {@link AsyncPage} ths is called when the page calls the
	 * {@link AsyncPageCallback#onSuccess()} method is called.
	 * 
	 * @param pageLoadResult
	 *            the page and associated data
	 * @param parameters
	 *            the page parameters
	 * @param command
	 *            {@link GotoPageCommand}
	 */
	public void onPageEnterSuccess(LoadedPageContainer pageLoadResult,
			PageParameters parameters, GotoPageCommand command);

	/**
	 * Event fired when the page has been requested - before the page dealt with
	 * in any way
	 * 
	 * @param pageToken
	 *            the page token (part of the history token without additional
	 *            parameter related information) (will be null if no executed
	 *            through a {@link GotoPageCommand})
	 * @param historyToken
	 *            the full history token (will be null if executed through a
	 *            {@link GotoPageCommand})
	 * @param session
	 *            {@link PageRequestSession} (will be null if no executed
	 *            through a {@link GotoPageCommand})
	 */
	public void onPageRequest(String pageToken, String historyToken,
			PageRequestSession session);

	/**
	 * Event fired after the {@link Page} is loaded but before the page is given
	 * to the {@link ApplicationPresenter}
	 * 
	 * @param pageLoadResult
	 *            the page and associated data
	 * @param parameters
	 *            the page parameters
	 * @param command
	 *            {@link GotoPageCommand}
	 */
	public void onBeforePageEnter(LoadedPageContainer pageLoadResult,
			PageParameters parameters, GotoPageCommand command);

	/**
	 * Event fired after when the {@link PageLoader} initially loads the
	 * {@link Page}
	 * 
	 * @param pageLoadResult
	 *            the page and associated data
	 */
	public void onPageLoaded(LoadedPageContainer result);

	/**
	 * Initiated by the page using {@link AsyncPageCallback#wait()} to notify
	 * event handlers that some async process (data loading for example) is
	 * happening
	 * 
	 * @param loadedPageContainer
	 *            {@link LoadedPageContainer}
	 * @param parameters
	 *            {@link PageParameters}
	 * @param command
	 *            {@link GotoPageCommand}
	 */
	public void onPageWaitForAsync(LoadedPageContainer loadedPageContainer,
			PageParameters parameters, GotoPageCommand command);

	/**
	 * Initiated by the page using {@link AsyncPageCallback#waitForAsync()} to notify
	 * event handlers that a forward request has been initiated
	 * 
	 * @param currentLoadedPageContainer
	 *            the current loaded page container
	 * @param parameters
	 *            currentParameters the current parameters
	 * @param currentCommand
	 *            the command representing the page that initiated the forward
	 * @param forwardCommand
	 *            the command that will be forwarded to
	 */
	public void onPageForward(LoadedPageContainer currentLoadedPageContainer,
			PageParameters currentParameters, GotoPageCommand currentCommand,
			GotoPageCommand forwardCommand);
}