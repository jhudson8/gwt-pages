package com.google.gwt.gwtpages.client.page.event;

import java.io.Serializable;

import com.google.gwt.gwtpages.client.GotoPageCommand;
import com.google.gwt.gwtpages.client.page.AsyncPageCallback;
import com.google.gwt.gwtpages.client.page.LoadedPageContainer;
import com.google.gwt.gwtpages.client.page.Page;
import com.google.gwt.gwtpages.client.page.loader.PageLoader;
import com.google.gwt.gwtpages.client.page.parameters.PageParameters;
import com.google.gwt.user.client.Command;

/**
 * Event handler allowing access to all GWT Pages handle-able error states
 *  
 * @author Joe Hudson
 */
public interface PageErrorEventHandler extends PageEventHandler {

	/**
	 * Initiated by the page using {@link AsyncPageCallback#wait()} to notify
	 * event handlers that some the page has violated an authentication /
	 * authorization check
	 * 
	 * @param loadedPageContainer
	 *            {@link LoadedPageContainer}
	 * @param pageParameters
	 *            {@link PageParameters}
	 * @param command
	 *            {@link GotoPageCommand}
	 * @param previousHandlerCommandResult
	 *            the return Command of the previous event handler or null
	 * @param parameters
	 *            any meaningful parameters which demonstrate the illegal access
	 * @return a command containing instructions for how to handle this
	 *         exception or return null if this handler does not handle this issue
	 */
	public Command onIllegalPageAccess(LoadedPageContainer loadedPageContainer,
			PageParameters pageParameters, GotoPageCommand command, Serializable... parameters);

	/**
	 * Event fired if an error occurs while showing the requested page (but
	 * after the page was loaded correctly from the {@link PageLoader}. With an
	 * {@link AsyncPage} this is called when the page calls the
	 * {@link AsyncPageCallback#onFailure(Throwable)} method is called.
	 * 
	 * @param pageLoadResult
	 *            the page and associated data
	 * @param parameters
	 *            the page parameters
	 * @param command
	 *            {@link GotoPageCommand}
	 * @return a command containing instructions for how to handle this
	 *         exception or return null if this handler does not handle this issue
	 */
	public Command onPageEnterFailure(LoadedPageContainer pageLoadResult,
			PageParameters parameters, GotoPageCommand command);

	/**
	 * Event fired if a {@link Page} is requested but not found
	 * 
	 * @param historyToken
	 *            the history token
	 * @return a command containing instructions for how to handle this
	 *         exception or return null if this handler does not handle this issue
	 */
	public Command onPageNotFound(String historyToken);

	/**
	 * Event fired if an exception is thrown when loading a {@link Page}
	 * 
	 * @param historyToken
	 *            the history token
	 * @param cause
	 *            the error that was thrown
	 * @param previousHandlerCommandResult
	 *            the return Command of the previous event handler or null
	 * @return a command containing instructions for how to handle this
	 *         exception or return null if this handler does not handle this issue
	 */
	public Command onPageLoadFailure(String historyToken, Throwable cause);
}
