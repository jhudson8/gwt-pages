package com.google.gwt.gwtpages.client;

import com.google.gwt.gwtpages.client.page.AsyncPageCallback;
import com.google.gwt.gwtpages.client.page.Page;
import com.google.gwt.gwtpages.client.page.TransientPage;
import com.google.gwt.gwtpages.client.page.parameters.PageParameters;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;

class UnhandledPageLoadErrorPage extends HTML implements Page, TransientPage {

	UnhandledPageLoadErrorPage(String pageToken, Throwable cause) {
		super(
				"<p>The requested page for '<b>"
						+ pageToken
						+ "</b>' threw an exception while being loaded by the page loader.</p>"
						+ "<p><span style=\"color: #FF0000\">"
						+ cause.getMessage()
						+ "</span></p>"
						+ "<p>You are looking the default GWT Pages fallback page for this issue.  You should deal with "
						+ "this case by registering an event handler {see Pages.add(PageEventHandler}).  This specific "
						+ "method that needs to be dealt with here is the onPageLoadFailure method.  You would usually "
						+ "call Pages.goTo(...).execute() to direct to an appropriate page.</p>");
	}

	public void init(Pages pages) throws Exception {
	}

	public void destroy(Pages pages) {
	}

	public void onEnterPage(PageParameters parameters,
			PageRequestSession session, AsyncPageCallback callback) {
	}

	public void onExitPage() {
	}

	public Widget asWidget() {
		return this;
	}
}