package com.google.gwt.gwtpages.demo.client.pages;

import java.util.Date;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.gwtpages.client.PageRequestSession;
import com.google.gwt.gwtpages.client.Pages;
import com.google.gwt.gwtpages.client.message.PageRequestSessionWithMessage;
import com.google.gwt.gwtpages.client.page.AsyncPageCallback;
import com.google.gwt.gwtpages.client.page.impl.UiBoundPage;
import com.google.gwt.gwtpages.client.page.parameters.PageParameters;
import com.google.gwt.gwtpages.demo.client.components.SourceLinkPanel;
import com.google.gwt.gwtpages.demo.client.components.SourceReferencePanel;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.inject.Inject;

public class NavigationPage extends UiBoundPage<FlowPanel> implements ClickHandler {

	@Inject
	Pages pages;

	@UiField
	Anchor dynamicNavigation1;
	@UiField
	Anchor dynamicNavigation2;
	@UiField
	Anchor dynamicNavigation3;
	@UiField
	Anchor dynamicNavigation4;
	@UiField
	Anchor dynamicNavigation5;
	@UiField
	Anchor dynamicNavigation6;
	@UiField
	SimplePanel sourceRef;

	public void onConstruct(FlowPanel widget) {
		dynamicNavigation1.addClickHandler(this);
		dynamicNavigation2.addClickHandler(this);
		dynamicNavigation3.addClickHandler(this);
		dynamicNavigation4.addClickHandler(this);
		dynamicNavigation5.addClickHandler(this);
		dynamicNavigation6.addClickHandler(this);
		sourceRef.add(new SourceReferencePanel(new SourceLinkPanel(
				NavigationPage.class)));
	}

	@Override
	public void onEnterPage(PageParameters parameters,
			PageRequestSession pageRequestData, final AsyncPageCallback callback) {
		// this is executed when this page is requested - all of the parameters
		// are provided to provide us with information we need.
		// PageParameters: wrapper object to make it easier to deal with the
		// history token string
		// PageRequestSession: transient session that can be used to pass
		// information to the
		// responding page that won't show up in the history token
		// AsyncPageCallback: page flow callback class that we *must* call a
		// method on. This is
		// valuable when retrieving data with async requests

		// callback.waitForAsync();
		// GWT.runAsync(new RunAsyncCallback() {
		//
		// @Override
		// public void onSuccess() {
		// Window.alert("You shouldn't see the page yet!");
		// callback.onSuccess();
		// }
		//
		// @Override
		// public void onFailure(Throwable reason) {
		// callback.onFailure(reason);
		// }
		// });
	}

	@Override
	public void onExitPage() {
		// this is executed when we navigate from this page to another page
	}

	/** HANDLER METHODS **/
	private void onSimplePageNavigation() {
		// or Pages.get() if not using dependency injection
		pages.gotoPage(PageConstants.PAGE_REQUEST_INFO).execute();
	}

	private void onOrdinalParamsNavigation() {
		pages.gotoPage(PageConstants.PAGE_REQUEST_INFO).addParameter("param1")
				.addParameter("param2").execute();
	}

	private void onMappedParamsNavigation() {
		pages.gotoPage(PageConstants.PAGE_REQUEST_INFO)
				.addParameter("param1", "parameters don't have to be strings")
				.addParameter("aDate", new Date())
				.addParameter("aBoolean", true).execute();
	}

	private void onTransientDataNavigation() {
		PageRequestSession session = new PageRequestSession();
		session.put("foo", "this will not appear in the history token");
		pages.goTo(PageConstants.PAGE_REQUEST_INFO, session)
				.addParameter("param1", "you can still add history parameters")
				.execute();
	}

	private void onCommandPassing() {
		PageRequestSession session = new PageRequestSession();
		session.put(PageConstants.PARAM_BACK_TITLE, "Navigation").put(
				PageConstants.PARAM_BACK_COMMAND, pages.getLastCommand());
		pages.goTo(PageConstants.PAGE_REQUEST_INFO, session)
				.addParameter("param1", 12345).execute();
	}

	/**
	 * NOTE: this requires the use of {@link DefaultPageMessageHandler} or some
	 * other message event handler. This handler is automatically added when
	 * calling {@link GWTPagesSettings#addDefaultEventHandlers()}
	 */
	private void onNavigationWithSuccessMessage() {
		PageRequestSession session = new PageRequestSessionWithMessage(
				"This is a success message");
		pages.goTo(PageConstants.PAGE_REQUEST_INFO, session)
				.addParameter("param1", "bar").execute();
	}

	@Override
	public void onClick(ClickEvent event) {
		if (event.getSource().equals(dynamicNavigation1)) {
			onSimplePageNavigation();
		} else if (event.getSource().equals(dynamicNavigation2)) {
			onOrdinalParamsNavigation();
		} else if (event.getSource().equals(dynamicNavigation3)) {
			onMappedParamsNavigation();
		} else if (event.getSource().equals(dynamicNavigation4)) {
			onTransientDataNavigation();
		} else if (event.getSource().equals(dynamicNavigation5)) {
			onNavigationWithSuccessMessage();
		} else if (event.getSource().equals(dynamicNavigation6)) {
			onCommandPassing();
		}
	}
}
