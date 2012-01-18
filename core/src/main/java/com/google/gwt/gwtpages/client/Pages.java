package com.google.gwt.gwtpages.client;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Stack;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.gwtpages.client.event.PageIllegalAccessEvent;
import com.google.gwt.gwtpages.client.event.PageNotFoundEvent;
import com.google.gwt.gwtpages.client.event.PageShownEvent;
import com.google.gwt.gwtpages.client.message.DefaultMessageHandler;
import com.google.gwt.gwtpages.client.page.ApplicationPresenter;
import com.google.gwt.gwtpages.client.page.AsyncPageCallback;
import com.google.gwt.gwtpages.client.page.DefaultPageLoadingHandler;
import com.google.gwt.gwtpages.client.page.LoadedPageContainer;
import com.google.gwt.gwtpages.client.page.Page;
import com.google.gwt.gwtpages.client.page.cache.PageCache;
import com.google.gwt.gwtpages.client.page.cache.SimplePageCache;
import com.google.gwt.gwtpages.client.page.event.PageErrorEventHandler;
import com.google.gwt.gwtpages.client.page.event.PageEventHandler;
import com.google.gwt.gwtpages.client.page.event.PageRequestEventHandler;
import com.google.gwt.gwtpages.client.page.loader.PageLoadCallback;
import com.google.gwt.gwtpages.client.page.loader.PageLoader;
import com.google.gwt.gwtpages.client.page.parameters.PageParameters;
import com.google.gwt.gwtpages.client.page.parameters.SimplePageParameters;
import com.google.gwt.gwtpages.client.page.parameters.SimpleTokenizer;
import com.google.gwt.gwtpages.client.page.parameters.Tokenizer;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.History;

/**
 * Controller class used to move from 1 page to another within a GWT Pages
 * application. When using this class, the
 * {@link GWTPagesSettings#init(PagePresenter, PageLoader, com.google.gwt.event.shared.HandlerManager)}
 * method must be called first. There are several different gotoPage methods
 * which can be used depending on the context.
 * 
 * When the application initially loads, you also usually want to call
 * {@link Pages#showStartPage(boolean)}
 * 
 * @author Joe Hudson
 */
public class Pages implements ValueChangeHandler<String> {

	private static Pages instance;

	private Settings settings = new Settings();
	protected HandlerManager eventBus;

	private boolean keepGoing = true;
	private GotoPageCommand lastCommand;
	private LoadedPageContainer currentPage;

	// local cache
	private Stack<GotoPageCommand> currentCommandStack = new Stack<GotoPageCommand>();
	private Stack<String> currentPageTokenStack = new Stack<String>();
	private static final int MAX_STACK_SIZE = 8;

	public static Pages get() {
		if (null == instance)
			instance = new Pages();
		return instance;
	}

	public static Pages init(PageLoader pageLoader,
			ApplicationPresenter applicationPresenter, HandlerManager bus,
			boolean monitorHistoryEvents) {
		Pages pages = new Pages(pageLoader, applicationPresenter, bus,
				monitorHistoryEvents);
		return pages;
	}

	public Pages(PageLoader pageLoader,
			ApplicationPresenter applicationPresenter, HandlerManager bus,
			boolean monitorHistoryEvents) {
		settings.pagePresenter = applicationPresenter;
		settings.pageLoader = pageLoader;
		eventBus = bus;
		init();
		if (monitorHistoryEvents)
			History.addValueChangeHandler(this);
	}

	protected void init() {
		if (updateStaticInstance()) {
			instance = this;
		}
		settings.pageLoader.init(this);
		settings.pagePresenter.init(this);
	}

	protected boolean updateStaticInstance () {
		return true;
	}

	public Pages() {
		if (null != instance) {
			this.settings = instance.settings;
			this.eventBus = instance.eventBus;
		}
		instance = this;
	}

	/**
	 * Return a nested pages instance that can be used within this pages instance
	 * @param presenter the nested application presenter
	 * @param bus the event bus
	 */
	public Pages nested(ApplicationPresenter presenter, HandlerManager bus) {
		return nested(settings.getPageLoader(), presenter, bus);
	}

	/**
	 * Return a nested pages instance that can be used within this pages instance
	 * @param presenter the nested application presenter
	 */
	public Pages nested(ApplicationPresenter presenter) {
		return nested(settings.getPageLoader(), presenter, new HandlerManager(null));
	}

	/**
	 * Return a nested pages instance that can be used within this pages instance
	 * @param pageLoader the page loader
	 * @param presenter the nested application presenter
	 * @param bus the event bus
	 */
	public Pages nested(PageLoader pageLoader, ApplicationPresenter presenter, HandlerManager bus) {
		Pages pages = new Pages(pageLoader, presenter, bus, false) {
			@Override
			protected boolean updateStaticInstance() {
				return false;
			}
		};
		return pages;
	}

	/**
	 * Go to the start page.
	 * 
	 * @param useHistoryToken
	 *            true if the history token should be evaluated to obtain the
	 *            page token and false to use the standard default page
	 */
	public void showStartPage(boolean useHistoryToken) {
		String token = PageLoader.PAGE_DEFAULT;
		if (useHistoryToken)
			token = History.getToken();
		if (null == token)
			token = PageLoader.PAGE_DEFAULT;
		onNewPage(token, true);
	}

	/**
	 * Return the {@link GotoPageCommand} used to navigate to the page
	 * represented by the page token
	 * 
	 * @param pageToken
	 *            the page token
	 */
	public GotoPageCommand goTo(String pageToken) {
		return new GotoPageCommand(pageToken, this);
	}

	/**
	 * Return the {@link GotoPageCommand} used to navigate to the page
	 * represented by the page token
	 * 
	 * @param pageToken
	 *            the page token
	 * @param convienance
	 *            method for using
	 *            {@link GotoPageCommand#addParameter(Serializable)}
	 */
	public GotoPageCommand goTo(String pageToken, Serializable... params) {
		GotoPageCommand cmd = new GotoPageCommand(pageToken, this);
		for (Serializable param : params)
			cmd.addParameter(param);
		return cmd;
	}

	/**
	 * Return the {@link GotoPageCommand} used to navigate to the page
	 * represented by the page token
	 * 
	 * @param pageToken
	 *            the page token
	 * @param session
	 *            a custom page request session
	 */
	public GotoPageCommand goTo(String pageToken, PageRequestSession session) {
		return new GotoPageCommand(pageToken, session, this);
	}

	/**
	 * Return the {@link GotoPageCommand} used to navigate to the page
	 * represented by the page token
	 * 
	 * @param pageToken
	 *            the page token
	 * @param session
	 *            a custom page request session
	 * @param convienance
	 *            method for using
	 *            {@link GotoPageCommand#addParameter(Serializable)}
	 */
	public GotoPageCommand goTo(String pageToken,
			PageRequestSession session, Serializable... params) {
		GotoPageCommand cmd = new GotoPageCommand(pageToken, session, this);
		for (Serializable param : params)
			cmd.addParameter(param);
		return cmd;
	}

	/**
	 * Use {@link GotoPageCommand#execute()}
	 */
	void goTo(GotoPageCommand command) {
		currentCommandStack.add(command);
		sanityCheck();
		
		keepGoing = true;
		for (PageRequestEventHandler handler : settings.pageRequestEventHandlers) {
			handler.onPageRequest(command.getPageToken(), null,
					command.getSession());
		}
		String pageToken = command.getPageToken();
		LoadedPageContainer pageData = settings.getPageCache().borrowPage(pageToken);
		String historyToken = null;
		PageParameters parameters = null;
		if (null != command.getParameterList(false)) {
			Serializable[] params = command.getParameterList(false).toArray(
					new Serializable[command.getParameterList(false).size()]);
			historyToken = createHistoryToken(pageToken, params);
			parameters = new SimplePageParameters(pageToken, historyToken,
					params, null);
		} else if (null != command.getParameterMap(false)) {
			historyToken = createHistoryToken(pageToken,
					command.getParameterMap(false));
			parameters = new SimplePageParameters(pageToken, historyToken,
					null, command.getParameterMap(false));
		} else {
			historyToken = createHistoryToken(pageToken);
			parameters = new SimplePageParameters(pageToken, historyToken,
					null, null);
		}

		if (command.shouldAddHistoryToken())
			History.newItem(historyToken, false);

		if (null != pageData) {
			goTo(pageData, parameters, command);
		} else {
			settings.getPageLoader().getPage(pageToken,
					new _PageLoadCallback(parameters, command));
		}
	}

	private void onNewPage(String historyToken, boolean addHistoryToken) {
		currentPageTokenStack.add(historyToken);
		// FIXME add sanity check here
		
		keepGoing = true;
		for (PageRequestEventHandler handler : settings.pageRequestEventHandlers) {
			handler.onPageRequest(null, historyToken, null);
		}
		if (!keepGoing())
			return;
		String pageToken = null;
		if (null == historyToken
				|| historyToken.equals(PageLoader.PAGE_DEFAULT)) {
			pageToken = PageLoader.PAGE_DEFAULT;
		} else {
			Iterator<String> possibleTokens = settings.getTokenizer()
					.getPossiblePageTokens(historyToken);
			while (possibleTokens.hasNext()) {
				String token = possibleTokens.next();
				if (settings.getPageLoader().isValidPageToken(token)) {
					pageToken = token;
					break;
				}
			}
		}
		if (null == pageToken) {
			onPageNotFound(null, historyToken);
			return;
		}

		PageParameters parameters = settings.getTokenizer().getPageParameters(
				historyToken, pageToken);
		LoadedPageContainer pageLoadResult = settings.getPageCache().borrowPage(pageToken);
		if (null != pageLoadResult) {
			// the page was cached
			goTo(pageLoadResult, parameters, new GotoPageCommand(pageToken,
					this).addHistoryToken(addHistoryToken));
		} else {
			// the page couldn't be found
			settings.getPageLoader().getPage(
					pageToken,
					new _PageLoadCallback(parameters, new GotoPageCommand(
							pageToken, this).addHistoryToken(addHistoryToken)));
		}
	}

	private void sanityCheck() {
		if (currentCommandStack.size() > MAX_STACK_SIZE || currentPageTokenStack.size() > MAX_STACK_SIZE) {
			_forwardToPage(new UnhandledStackOverflowErrorPage());
			stopRequest();
		}
	}

	/**
	 * Internal check - should we continue with the page request?
	 */
	private boolean keepGoing() {
		if (keepGoing)
			return true;
		else
			return false;
	}

	/** Async page loading callbacks **/
	private void onPageFound(LoadedPageContainer pageLoadResult,
			PageParameters pageParameters, GotoPageCommand command) {
		for (PageRequestEventHandler handler : settings.pageRequestEventHandlers) {
			handler.onPageLoaded(pageLoadResult);
		}
		if (!keepGoing())
			return;
		settings.getPageCache().registerPage(pageLoadResult.getPageToken(), pageLoadResult);
		goTo(pageLoadResult, pageParameters, command);
	}

	private void onPageNotFound(String pageToken, String historyToken) {
		ArrayList<Command> commands = new ArrayList<Command>();
		if (null == historyToken) historyToken = pageToken;
		for (PageErrorEventHandler handler : settings.pageErrorEventHandlers) {
			Command cmd = handler.onPageNotFound(historyToken);
			if (null != cmd && !commands.contains(cmd))
				commands.add(cmd);
		}
		eventBus.fireEvent(new PageNotFoundEvent(historyToken));
		if (commands.size() == 0) {
			if (settings.getPageLoader().isValidPageToken(PageLoader.PAGE_NOT_FOUND)) {
				goTo(PageLoader.PAGE_NOT_FOUND);
			}
			else {
				_forwardToPage(new UnhandledPageNotFoundPage(historyToken));
			}
		} else {
			for (Command cmd : commands)
				cmd.execute();
		}
		currentCommandStack.clear();
		currentPageTokenStack.clear();
	}

	private void onPageLoadFailure(String pageToken, String historyToken,
			Throwable cause) {
		ArrayList<Command> commands = new ArrayList<Command>();
		for (PageErrorEventHandler handler : settings.pageErrorEventHandlers) {
			Command cmd = handler.onPageLoadFailure(historyToken, cause);
			if (null != cmd && !commands.contains(cmd))
				commands.add(cmd);
		}
		if (commands.size() == 0) {
			_forwardToPage(new UnhandledPageLoadErrorPage(pageToken, cause));
		} else {
			for (Command cmd : commands)
				cmd.execute();
		}
		currentCommandStack.clear();
		currentPageTokenStack.clear();
	}

	private void goTo(LoadedPageContainer page, PageParameters parameters,
			GotoPageCommand command) {
		// do a sanity check here so we don't get caught in a loop...
		
		try {
			page.getPage().init(this);
		}
		catch (Throwable t) {
			for (PageErrorEventHandler handler : settings.pageErrorEventHandlers) {
				handler.onPageEnterFailure(page, parameters, command);
			}
			return;
		}
		command.getSession().setPageAttributes(page.getAttributes());
		page = page.copy();
		lastCommand = command;
		for (PageRequestEventHandler handler : settings.pageRequestEventHandlers) {
			handler.onBeforePageEnter(page, parameters, command);
		}
		if (!keepGoing())
			return;
		LoadedPageContainer previousPage = null;
		try {
			_AsyncPageCallback callback = new _AsyncPageCallback(page,
					previousPage, parameters, command);
			page.getPage().onEnterPage(parameters, command.getSession(),
					callback);
			for (PageRequestEventHandler handler : settings.pageRequestEventHandlers) {
				handler.onAfterPageEnter(page, parameters, command);
			}
			if (callback.waitForAsync || callback.endState) {
				if (!callback.waitForAsync) {
					try {
						settings.getPageCache().returnPage(page, false);
					}
					catch (Throwable t) {}
				}
				// don't do anything - it will happen in the callback onSuccess
				// method
				// or we are at some end state
			} else {
				// complete the process
				callback.successComplete = true;
				callback.onSuccess(true);

				currentCommandStack.clear();
				currentPageTokenStack.clear();
				try {
					settings.getPageCache().returnPage(page, false);
				}
				catch (Throwable t) {}
			}
		} catch (Throwable t) {
			try {
				settings.getPageCache().returnPage(page, true);
			}
			catch (Throwable t2) {}
			ArrayList<Command> commands = new ArrayList<Command>();
			for (PageErrorEventHandler handler : settings.pageErrorEventHandlers) {
				Command cmd = handler.onPageEnterFailure(page, parameters,
						command);
				if (null != cmd && !commands.contains(cmd))
					commands.add(cmd);
			}
			if (commands.size() == 0) {
				if (settings.getPageLoader().isValidPageToken(PageLoader.PAGE_ERROR)) {
					goTo(PageLoader.PAGE_ERROR);
				}
				else {
					_forwardToPage(new UnhandledPageEnterErrorPage(
							page.getPageToken(), t));
				}
			} else {
				for (Command cmd : commands)
					cmd.execute();
			}
			currentCommandStack.clear();
			currentPageTokenStack.clear();
		}
	}

	/**
	 * History token change listener
	 */
	public void onValueChange(ValueChangeEvent<String> event) {
		onNewPage(event.getValue(), true);
	}

	/**
	 * Create and return the history token represented by the page token and
	 * additional parameters
	 * 
	 * @param pageToken
	 *            the page token
	 * @param parameters
	 *            input parameters
	 */
	public String createHistoryToken(String pageToken,
			Serializable... parameters) {
		return settings.getTokenizer()
				.createHistoryToken(pageToken, parameters);
	}

	/**
	 * Create and return the history token represented by the page token and
	 * additional parameters
	 * 
	 * @param pageToken
	 *            the page token
	 * @param parameters
	 *            input parameters
	 */
	public String createHistoryToken(String pageToken,
			HashMap<String, Serializable> parameters) {
		return settings.getTokenizer()
				.createHistoryToken(pageToken, parameters);
	}

	/**
	 * Stop the current request processing
	 */
	public void stopRequest() {
		keepGoing = false;
		currentCommandStack.clear();
		currentPageTokenStack.clear();
	}

	public GotoPageCommand getLastCommand() {
		return lastCommand;
	}

	/**
	 * Add page lifecycle event handler
	 * 
	 * @param pageEventHandlers
	 *            the handler(s)
	 * @return the Settings
	 */
	public Pages add(PageEventHandler... pageEventHandlers) {
		for (PageEventHandler pageEventHandler : pageEventHandlers) {
			pageEventHandler.init(this);
			settings.pageEventHandlers.add(pageEventHandler);
			if (pageEventHandler instanceof PageErrorEventHandler)
				settings.pageErrorEventHandlers.add((PageErrorEventHandler) pageEventHandler);
			if (pageEventHandler instanceof PageRequestEventHandler)
				settings.pageRequestEventHandlers.add((PageRequestEventHandler) pageEventHandler);
		}
		return this;
	}

	/**
	 * Set the {@link Tokenizer}. If not set, {@link SimpleTokenizer} will be
	 * used.
	 * 
	 * @param tokenizer
	 *            the page tokenizer
	 * @return the Settings
	 */
	public Pages setPageTokenizer(Tokenizer tokenizer) {
		settings.tokenizer = tokenizer;
		tokenizer.init(this);
		return this;
	}

	/**
	 * Set the page cacheing mechanism
	 * @param pageCache the page cacheing mechanism
	 */
	public void setPageCache(PageCache pageCache) {
		settings.pageCache = pageCache;
	}

	/**
	 * Add default event handlers for handling page request messages as well as
	 * notifying the user when pages are being loaded. The handlers that are
	 * added are
	 * <ul>
	 * <ol>
	 * <li>{@link DefaultPageLoadingHandler}</li>
	 * <li>{@link DefaultMessageHandler}</li>
	 * </ul>
	 */
	public Pages addDefaultEventHandlers() {
		add(new DefaultPageLoadingHandler(), new DefaultMessageHandler());
		return this;
	}

	/**
	 * Return the settings for this pages instance
	 */
	public Settings getSettings() {
		return settings;
	}

	/**
	 * Return the common event bus
	 */
	public HandlerManager getEventBus() {
		return eventBus;
	}

	/**
	 * Return the {@link}
	 * 
	 * @return
	 */
	public LoadedPageContainer getCurrentPage() {
		return currentPage;
	}

	protected void _forwardToPage(Page page) {
		settings.getApplicationPresenter().showPage(
				new LoadedPageContainer(page), new SimplePageParameters(this),
				new PageRequestSession());
	}

	/** Async callback classes */
	private class _AsyncPageCallback implements AsyncPageCallback {
		private LoadedPageContainer previousPage;
		private LoadedPageContainer page;
		private PageParameters pageParameters;
		private GotoPageCommand command;
		private boolean successComplete = false;
		private boolean waitForAsync = false;
		private boolean endState = false;

		private _AsyncPageCallback(LoadedPageContainer page,
				LoadedPageContainer previousPage,
				PageParameters pageParameters, GotoPageCommand command) {
			this.page = page;
			this.previousPage = previousPage;
			this.pageParameters = pageParameters;
			this.command = command;
		}

		public void onSuccess() {
			onSuccess(false);
		}

		private void onSuccess(boolean force) {
			if (!force && successComplete) {
				System.err
						.println("Error with page '"
								+ page.getPageToken()
								+ "' onSuccess() was called and you must call waitForAsync() first.");
				return;
			}
			try {
				settings.getApplicationPresenter().showPage(page, pageParameters,
						command.getSession());
				currentPage = page;
				for (PageRequestEventHandler handler : settings.pageRequestEventHandlers) {
					handler.onPageEnterSuccess(page, pageParameters, command);
				}
				getEventBus().fireEvent(
						new PageShownEvent(page, pageParameters, command,
								previousPage));
				successComplete = true;
				currentCommandStack.clear();
				currentPageTokenStack.clear();
				try {
					settings.getPageCache().returnPage(page, false);
				}
				catch (Throwable t) {}
			}
			catch (Throwable t) {
				try {
					settings.getPageCache().returnPage(page, true);
				}
				catch (Throwable t2) {}
			}
		}

		public void onFailure(Throwable cause) {
			if (successComplete) {
				System.err
						.println("Error with page '"
								+ page.getPageToken()
								+ "' onFailure() was called but the request was already completed successfully.");
				return;
			}
			try {
				settings.getPageCache().returnPage(page, true);
			}
			catch (Throwable t) {}
			ArrayList<Command> commands = new ArrayList<Command>();
			for (PageErrorEventHandler handler : settings.pageErrorEventHandlers) {
				Command cmd = handler.onPageEnterFailure(page, pageParameters,
						command);
				if (null != cmd && !commands.contains(cmd))
					commands.add(cmd);
			}
			if (commands.size() == 0) {
				if (settings.getPageLoader().isValidPageToken(PageLoader.PAGE_ERROR)) {
					goTo(PageLoader.PAGE_ERROR);
				}
				else {
					_forwardToPage(new UnhandledPageEnterErrorPage(
							page.getPageToken(), cause));
				}
			} else {
				for (Command cmd : commands)
					cmd.execute();
			}
		}

		public void onIllegalAccess(Serializable... parameters) {
			try {
				// we don't define this as an error with the page - just user access issue hence the false
				settings.getPageCache().returnPage(page, false);
			}
			catch (Throwable t) {}
			if (successComplete) {
				System.err
						.println("Error with page '"
								+ page.getPageToken()
								+ "' onIllegalAccess() was called but the request was already completed successfully.");
				return;
			}
			ArrayList<Command> commands = new ArrayList<Command>();
			for (PageErrorEventHandler handler : settings.pageErrorEventHandlers) {
				Command cmd = handler.onIllegalPageAccess(page, pageParameters,
						command, parameters);
				if (null != cmd && !commands.contains(cmd))
					commands.add(cmd);
			}
			eventBus.fireEvent(new PageIllegalAccessEvent(page, pageParameters,
					command, parameters));
			if (commands.size() == 0) {
				if (settings.getPageLoader().isValidPageToken(PageLoader.PAGE_ILLEGAL_ACCESS)) {
					goTo(PageLoader.PAGE_ILLEGAL_ACCESS);
				}
				else {
					_forwardToPage(new UnhandledIllegalAccessPage(page.getPageToken()));
				}
			} else {
				for (Command cmd : commands)
					cmd.execute();
			}
			endState = true;
		}

		public void waitForAsync() {
			for (PageRequestEventHandler handler : settings.pageRequestEventHandlers) {
				handler.onPageWaitForAsync(page, pageParameters, command);
			}
			waitForAsync = true;
		}

		public void forward(GotoPageCommand command) {
			try {
				settings.getPageCache().returnPage(page, false);
			}
			catch (Throwable t) {}
			for (PageRequestEventHandler handler : settings.pageRequestEventHandlers) {
				handler.onPageForward(page, pageParameters, this.command,
						command);
			}
			endState = true;
			command.addHistoryToken(false);
			goTo(command);
		}
	}

	private class _PageLoadCallback implements PageLoadCallback {
		private PageParameters parameters;
		private GotoPageCommand command;

		public _PageLoadCallback(PageParameters parameters,
				GotoPageCommand command) {
			this.parameters = parameters;
			this.command = command;
		}

		public void onPageFound(LoadedPageContainer pageLoadResult) {
			Pages.this.onPageFound(pageLoadResult, parameters, command);
		}

		public void onPageNotFound(String pageToken) {
			Pages.this.onPageNotFound(pageToken, parameters.getHistoryToken());
		}

		public void onPageLoadFailure(String pageToken, Throwable cause) {
			Pages.this.onPageLoadFailure(pageToken,
					parameters.getHistoryToken(), cause);
		}
	}

	public class Settings {

		protected ApplicationPresenter pagePresenter;
		protected PageCache pageCache;
		protected Tokenizer tokenizer;
		protected PageLoader pageLoader;
		protected ArrayList<PageEventHandler> pageEventHandlers = new ArrayList<PageEventHandler>();
		protected ArrayList<PageErrorEventHandler> pageErrorEventHandlers = new ArrayList<PageErrorEventHandler>();
		protected ArrayList<PageRequestEventHandler> pageRequestEventHandlers = new ArrayList<PageRequestEventHandler>();

		/**
		 * Return the {@link Tokenizer}. If not set, {@link SimpleTokenizer}
		 * will be used.
		 */
		public Tokenizer getTokenizer() {
			if (null == tokenizer) {
				tokenizer = new SimpleTokenizer();
				tokenizer.init(Pages.this);
			}
			return tokenizer;
		}

		/**
		 * Return a list of all defined page lifecycle event handlers (or empty
		 * list if none defined)
		 */
		public ArrayList<PageEventHandler> getEventHandlers() {
			return pageEventHandlers;
		}

		/**
		 * Return the {@link PageLoader}.
		 */
		public PageLoader getPageLoader() {
			return pageLoader;
		}

		/**
		 * Return the {@link ApplicationPresenter}
		 */
		public ApplicationPresenter getApplicationPresenter() {
			return pagePresenter;
		}

		public PageCache getPageCache() {
			if (null == pageCache) pageCache = new SimplePageCache();
			return pageCache;
		}
	}
}