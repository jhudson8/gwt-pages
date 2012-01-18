package com.google.gwt.gwtpages.client.page;

import java.io.Serializable;

import com.google.gwt.gwtpages.client.GotoPageCommand;
import com.google.gwt.gwtpages.client.PageRequestSession;
import com.google.gwt.gwtpages.client.Pages;
import com.google.gwt.gwtpages.client.page.event.PageEventHandler;
import com.google.gwt.gwtpages.client.page.parameters.PageParameters;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.RootPanel;

public class DefaultPageLoadingHandler implements PageEventHandler {

	public static final String LOADING_CLASS_NAME = "ajax-loading";

	public void init(Pages pages) {
	}

	public void onAfterPageEnter(LoadedPageContainer pageLoadResult,
			PageParameters parameters, GotoPageCommand command) {
	}

	public void onPageEnterSuccess(LoadedPageContainer pageLoadResult,
			PageParameters parameters, GotoPageCommand command) {
		RootPanel.getBodyElement().removeClassName(LOADING_CLASS_NAME);
	}

	public Command onPageEnterFailure(LoadedPageContainer pageLoadResult,
			PageParameters parameters, GotoPageCommand command) {
		RootPanel.getBodyElement().removeClassName(LOADING_CLASS_NAME);
		return null;
	}

	public void onPageRequest(String pageToken, String historyToken,
			PageRequestSession session) {
		RootPanel.getBodyElement().addClassName(LOADING_CLASS_NAME);
	}

	public void onBeforePageEnter(LoadedPageContainer pageLoadResult,
			PageParameters parameters, GotoPageCommand command) {
	}

	public void onPageLoaded(LoadedPageContainer result) {
	}

	public Command onPageNotFound(String historyToken) {
		RootPanel.getBodyElement().removeClassName(LOADING_CLASS_NAME);
		return null;
	}

	public Command onPageLoadFailure(String historyToken, Throwable cause) {
		RootPanel.getBodyElement().removeClassName(LOADING_CLASS_NAME);
		return null;
	}

	public void onPageWaitForAsync(LoadedPageContainer loadedPageContainer,
			PageParameters parameters, GotoPageCommand command) {
	}

	public Command onIllegalPageAccess(LoadedPageContainer loadedPageContainer,
			PageParameters pageParameters, GotoPageCommand command,
			Serializable... parameters) {
		RootPanel.getBodyElement().removeClassName(LOADING_CLASS_NAME);
		return null;
	}

	public void onPageRedirect(LoadedPageContainer currentLoadedPageContainer,
			PageParameters currentParameters, GotoPageCommand currentCommand,
			GotoPageCommand forwardCommand) {
	}
}