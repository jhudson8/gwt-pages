package com.google.gwt.gwtpages.demo.client;

import java.io.Serializable;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.gwtpages.client.GotoPageCommand;
import com.google.gwt.gwtpages.client.Pages;
import com.google.gwt.gwtpages.client.applicationpresenter.CompositeLayoutApplicationPresenter;
import com.google.gwt.gwtpages.client.page.LoadedPageContainer;
import com.google.gwt.gwtpages.client.page.event.PageErrorEventHandler;
import com.google.gwt.gwtpages.client.page.parameters.PageParameters;
import com.google.gwt.gwtpages.demo.client.loaders.DemoPageLoader;
import com.google.gwt.gwtpages.demo.client.pages.PageConstants;
import com.google.gwt.gwtpages.demo.client.pages.layout.DefaultLayoutPage;
import com.google.gwt.gwtpages.demo.client.pages.layout.NoLayoutPage;
import com.google.gwt.gwtpages.demo.client.pages.layout.RightNavLayoutPage;
import com.google.gwt.user.client.Command;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class GWT_Pages_Demo implements EntryPoint, PageErrorEventHandler {

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		// composite application presenter for use with the layouts demo page
		CompositeLayoutApplicationPresenter presenter = new CompositeLayoutApplicationPresenter(PageConstants.LAYOUT_DEFAULT)
			.register(PageConstants.LAYOUT_DEFAULT, new DefaultLayoutPage())
			.register(PageConstants.LAYOUT_NONE, new NoLayoutPage())
			.register(PageConstants.LAYOUT_NAV_RIGHT, new RightNavLayoutPage());
		
		// initialize the pages settings
		Pages.init(
				(DemoPageLoader) GWT.create(DemoPageLoader.class), // the class responsible for mapping page tokens to pages
				presenter, // the component that will actually render the pages
				new HandlerManager(null), true) // the event bus
			.addDefaultEventHandlers().add(this); // add default behavior if desired

		// make the initial page show up (and check the history token to go directly to a requested page and bypass the start
		// page if necessary
		Pages.get().showStartPage(true);
	}

	@Override
	public void init(Pages pages) {}
	
	@Override
	public Command onPageEnterFailure(LoadedPageContainer pageLoadResult,
			PageParameters parameters, GotoPageCommand command) {
		return new Command() {
			@Override
			public void execute() {
				Pages.get().gotoPage(PageConstants.PAGE_500).addHistoryToken(false).execute();
			}
		};
		
	}

	@Override
	public Command onPageLoadFailure(String historyToken, Throwable cause) {
		return new Command() {
			@Override
			public void execute() {
				Pages.get().gotoPage(PageConstants.PAGE_500).addHistoryToken(false).execute();
			}
		};
	}

	@Override
	public Command onIllegalPageAccess(LoadedPageContainer loadedPageContainer,
			PageParameters pageParameters, GotoPageCommand command,
			Serializable... parameters) {
		// we won't hit this method because we aren't calling illegal access
		return null;
	}

	@Override
	public Command onPageNotFound(String historyToken) {
		return new Command() {
			@Override
			public void execute() {
				Pages.get().gotoPage(PageConstants.PAGE_404).addHistoryToken(false).execute();
			}
		};
	}
}