package com.google.gwt.gwtpages.client.event.breadcrumb;

import java.io.Serializable;
import java.util.Stack;

import com.google.gwt.gwtpages.client.GotoPageCommand;
import com.google.gwt.gwtpages.client.PageRequestSession;
import com.google.gwt.gwtpages.client.Pages;
import com.google.gwt.gwtpages.client.page.LoadedPageContainer;
import com.google.gwt.gwtpages.client.page.PageTitleProvider;
import com.google.gwt.gwtpages.client.page.TransientPage;
import com.google.gwt.gwtpages.client.page.event.PageEventHandler;
import com.google.gwt.gwtpages.client.page.loader.PageLoader;
import com.google.gwt.gwtpages.client.page.parameters.PageParameters;
import com.google.gwt.user.client.Command;

/**
 * Page event handler used to keep breadcrumbs. All pages by default are added
 * to the breadcrumb stack. The stack can be cleared using
 * {@link BreadcrumbEventHandler#clear()} or by adding a page or session
 * attribute using the key {@link BreadcrumbEventHandler#TOKEN_CLEAR_STACK} and
 * the value of {@link Boolean#TRUE}. If you do not want a page to be added to
 * the breadcrumbs, that page should implement {@link TransientPage}
 * 
 * @author Joe Hudson
 */
public class BreadcrumbEventHandler implements PageEventHandler {

	public static String TOKEN_CLEAR_STACK = "BreadcrumbEventHandler:clearStack";
	private static BreadcrumbEventHandler instance = null;

	/**
	 * Return the last handler that was created or null of none were created
	 */
	public static BreadcrumbEventHandler get() {
		return instance;
	}

	private Stack<BreadcrumbItem> breadcrumbs = new Stack<BreadcrumbItem>();

	public BreadcrumbEventHandler() {
		instance = this;
	}

	public void init(Pages pages) {
	}

	public void clear() {
		breadcrumbs.clear();
	}

	public Stack<BreadcrumbItem> getBreadcrumbStack() {
		return breadcrumbs;
	}

	public void onAfterPageEnter(LoadedPageContainer pageLoadResult,
			PageParameters parameters, GotoPageCommand command) {
		if (pageLoadResult.getPage() instanceof TransientPage)
			return;
		String pageLabel = null;
		if (pageLoadResult.getPage() instanceof PageTitleProvider) {
			pageLabel = ((PageTitleProvider) pageLoadResult.getPage())
					.getPageTitle();
		} else if (pageLoadResult instanceof PageTitleProvider) {
			pageLabel = ((PageTitleProvider) pageLoadResult).getPageTitle();
		}
		breadcrumbs.push(new BreadcrumbItem(command, pageLoadResult,
				parameters, pageLabel));
	}

	public void onPageEnterSuccess(LoadedPageContainer pageLoadResult,
			PageParameters parameters, GotoPageCommand command) {
	}

	public Command onPageEnterFailure(LoadedPageContainer pageLoadResult,
			PageParameters parameters, GotoPageCommand command) {
		return null;
	}

	public void onPageRequest(String pageToken, String historyToken,
			PageRequestSession session) {
	}

	public void onBeforePageEnter(LoadedPageContainer pageLoadResult,
			PageParameters parameters, GotoPageCommand command) {
		Boolean val = (Boolean) command.getSession().get(TOKEN_CLEAR_STACK,
				true);
		if (null != val && val)
			clear();
	}

	public void onPageLoaded(LoadedPageContainer result) {
	}

	public Command onPageNotFound(String historyToken) {
		return null;
	}

	public Command onPageLoadFailure(String historyToken, Throwable cause) {
		return null;
	}

	public void onPageWaitForAsync(LoadedPageContainer loadedPageContainer,
			PageParameters parameters, GotoPageCommand command) {
	}

	public Command onIllegalPageAccess(LoadedPageContainer loadedPageContainer,
			PageParameters pageParameters, GotoPageCommand command,
			Serializable... parameters) {
		return null;
	}

	public void onPageRedirect(LoadedPageContainer currentLoadedPageContainer,
			PageParameters currentParameters, GotoPageCommand currentCommand,
			GotoPageCommand forwardCommand) {
	}

	/**
	 * Container class for page-related information related to a single page
	 * request that is persisted in the page stack. The
	 * {@link PageLoader#keepInStack(LoadedPageContainer, PageParameters)} is
	 * used to determine if the page should be kept in the page stack even after
	 * another page is requested.
	 * 
	 * @author Joe Hudson
	 */
	public class BreadcrumbItem {

		private String label;
		private LoadedPageContainer loadedPageContainer;
		private PageParameters parameters;
		private GotoPageCommand command;

		public BreadcrumbItem(GotoPageCommand command,
				LoadedPageContainer loadedPageContainer,
				PageParameters parameters, String label) {
			this.label = label;
			this.command = command;
			this.loadedPageContainer = loadedPageContainer;
			this.parameters = parameters;
		}

		/**
		 * Return the {@link LoadedPageContainer} containing all of the
		 * associated page data
		 */
		public LoadedPageContainer getLoadedPageContainer() {
			return loadedPageContainer;
		}

		/**
		 * Return the {@link PageParameters} for the specific page request
		 */
		public PageParameters getParameters() {
			return parameters;
		}

		/**
		 * Return a command that will navigate to this page
		 */
		public GotoPageCommand getCommand() {
			return command;
		}

		/**
		 * Return the label associated with this breadcrumb item
		 * 
		 * @return
		 */
		public String getLabel() {
			return label;
		}
	}
}