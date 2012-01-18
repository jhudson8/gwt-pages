package test.com.google.gwt.gwtpages.client;

import test.com.google.gwt.gwtpages.client.page.parameters.MockTokenizer;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.gwtpages.client.Pages;
import com.google.gwt.gwtpages.client.page.ApplicationPresenter;
import com.google.gwt.gwtpages.client.page.loader.PageLoader;

public class MockPages extends Pages {

	public MockPages(PageLoader pageLoader,
			ApplicationPresenter applicationPresenter, HandlerManager bus) {
		super(pageLoader, applicationPresenter, bus, false);
	}

	@Override
	protected void init() {
		super.init();
		setPageTokenizer(new MockTokenizer());
	}
}