package com.google.gwt.gwtpages.client;

import com.google.gwt.gwtpages.client.page.AsyncPageCallback;
import com.google.gwt.gwtpages.client.page.Page;
import com.google.gwt.gwtpages.client.page.TransientPage;
import com.google.gwt.gwtpages.client.page.parameters.PageParameters;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;

class UnhandledStackOverflowErrorPage extends HTML implements
		Page, TransientPage {

	UnhandledStackOverflowErrorPage() {
		super(
				"<p>You are encountering a situation where you will potentially receive a StackOverflowException " +
			"so this process was halted.  This usually occurs when an error display page causes an error when " +
			"it is rendering.  It can also happen when chaining forward commands that eventually go back around " +
			"to start again.  Please check your code and try again.</p>");
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