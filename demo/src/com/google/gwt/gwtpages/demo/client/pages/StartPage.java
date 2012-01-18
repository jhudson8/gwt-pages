package com.google.gwt.gwtpages.demo.client.pages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.gwtpages.client.PageRequestSession;
import com.google.gwt.gwtpages.client.page.AsyncPageCallback;
import com.google.gwt.gwtpages.client.page.impl.UiBoundPage;
import com.google.gwt.gwtpages.client.page.parameters.PageParameters;
import com.google.gwt.user.client.ui.SimplePanel;

public class StartPage extends UiBoundPage<SimplePanel> {

	public void onConstruct(SimplePanel widget) {
		// we're just demonstrating the use of UiBoundWidget - all of the UiBinder boilerplate code
		// is automatically written for you using deferred binding
		StartPageWidget startPageWidget = GWT.create(StartPageWidget.class);
		widget.add(startPageWidget);
	}

	@Override
	public void onEnterPage(PageParameters parameters,
			PageRequestSession pageRequestData, AsyncPageCallback callback) {
	}
}
