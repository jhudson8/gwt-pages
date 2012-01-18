package com.google.gwt.gwtpages.client.applicationpresenter;

import com.google.gwt.gwtpages.client.PageRequestSession;
import com.google.gwt.gwtpages.client.Pages;
import com.google.gwt.gwtpages.client.page.ApplicationPresenter;
import com.google.gwt.gwtpages.client.page.LoadedPageContainer;
import com.google.gwt.gwtpages.client.page.parameters.PageParameters;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * This is the simplest application presenter.  Using this, you will see the page content
 * only with no additional layout.
 * 
 * @author Joe Hudson
 */
public class SimplePanelApplicationPresenter extends SimplePanel implements ApplicationPresenter {

	public void showPage(LoadedPageContainer page,
			PageParameters parameters, PageRequestSession session) {
		clear();
		add(page.getPage().asWidget());
	}

	public void init(Pages settings) {
	}

	public Widget asWidget() {
		return this;
	}

	public void clearCurrentPage() {
		clear();
	}
}