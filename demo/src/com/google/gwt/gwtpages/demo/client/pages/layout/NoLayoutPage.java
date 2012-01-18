package com.google.gwt.gwtpages.demo.client.pages.layout;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.gwtpages.client.PageRequestSession;
import com.google.gwt.gwtpages.client.Pages;
import com.google.gwt.gwtpages.client.message.ui.MessagePanel;
import com.google.gwt.gwtpages.client.page.ApplicationPresenter;
import com.google.gwt.gwtpages.client.page.LoadedPageContainer;
import com.google.gwt.gwtpages.client.page.parameters.PageParameters;
import com.google.gwt.gwtpages.demo.client.pages.PageConstants;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

public class NoLayoutPage extends Composite implements ApplicationPresenter {

	interface _UiBinder extends UiBinder<DockLayoutPanel, NoLayoutPage> {
	}
	private static _UiBinder uiBinder = GWT.create(_UiBinder.class);

	@UiField
	Label pageTitle;
	@UiField
	SimplePanel messages;
	@UiField
	SimplePanel bodyContent;

	public NoLayoutPage() {
		initWidget(uiBinder.createAndBindUi(this));
		messages.add(new MessagePanel());
		bodyContent.getElement().getStyle().setPadding(6, Unit.PX);
	}

	@Override
	public void clearCurrentPage() {
		bodyContent.clear();
	}
	
	@Override
	public void showPage(LoadedPageContainer pageContainer, PageParameters parameters,
			PageRequestSession session) {
		bodyContent.clear();
		bodyContent.add(pageContainer.getPage().asWidget());
		String pageTitle = (String) session.get(PageConstants.PARAM_PAGE_TITLE, true);
		if (null == pageTitle) {
			this.pageTitle.setVisible(false);
			Window.setTitle("GWT Pages Demo");
		}
		else {
			this.pageTitle.setVisible(true);
			this.pageTitle.setText(pageTitle);
			Window.setTitle(pageTitle);
		}
	}

	@Override
	public void init(Pages settings) {
	}

	@Override
	public Widget asWidget() {
		return this;
	}
}