package com.google.gwt.gwtpages.demo.client.loaders;

import com.google.gwt.gwtpages.client.applicationpresenter.CompositeLayoutApplicationPresenter;
import com.google.gwt.gwtpages.client.page.Page;
import com.google.gwt.gwtpages.client.page.PageAttributes;
import com.google.gwt.gwtpages.client.page.loader.StandardPageLoader;
import com.google.gwt.gwtpages.demo.client.pages.LayoutsPage;
import com.google.gwt.gwtpages.demo.client.pages.MessagesPage;
import com.google.gwt.gwtpages.demo.client.pages.MessagesViewImpl;
import com.google.gwt.gwtpages.demo.client.pages.NavigationPage;
import com.google.gwt.gwtpages.demo.client.pages.PageConstants;
import com.google.gwt.gwtpages.demo.client.pages.RequestInfoPage;
import com.google.gwt.gwtpages.demo.client.pages.StartPage;
import com.google.gwt.gwtpages.demo.client.pages._404Page;
import com.google.gwt.gwtpages.demo.client.pages._500Page;
import com.google.gwt.gwtpages.demo.client.pages.layout.DefaultLayoutPage;
import com.google.gwt.gwtpages.demo.client.pages.layout.RightNavLayoutPage;

public class DemoPageLoader extends StandardPageLoader {

	@Override
	public void registerPages() {
		registerPage(PAGE_DEFAULT, "Welcome", StartPage.class);
		registerPage(PageConstants.PAGE_NAVIGATION, "Navigation",
				NavigationPage.class);
		registerPage(PageConstants.PAGE_MESSAGES, "Messages", null,
				MessagesPage.class, MessagesViewImpl.class);
		registerPage(PageConstants.PAGE_REQUEST_INFO,
				"View Request Information", RequestInfoPage.class);

		// change the default layout for this page
		registerPage(PageConstants.PAGE_LAYOUTS, "Multiple Layouts",
				PageConstants.LAYOUT_NAV_RIGHT, LayoutsPage.class);
		
		registerPage(PageConstants.PAGE_404, _404Page.class);
		registerPage(PageConstants.PAGE_500, _500Page.class);
	}

	private void registerPage(String token, String title,
			Class<? extends Page> pageClass) {
		registerPage(token, title, null, pageClass);
	}

	/**
	 * We'll add some data to the page attributes
	 * 
	 * @param token
	 *            the page token
	 * @param title
	 *            the page title (this is referenced in the application
	 *            presenters - {@link DefaultLayoutPage} and
	 *            {@link RightNavLayoutPage}
	 * @param layoutToken
	 *            the token representing which application presenter (layout) we
	 *            will use - {@link CompositeLayoutApplicationPresenter}
	 * @param pageClass
	 *            the page class
	 */
	private void registerPage(String token, String title, String layoutToken,
			Class<? extends Page> pageClass) {
		PageAttributes attributes = new PageAttributes();
		attributes.put(PageConstants.PARAM_PAGE_TITLE, title);
		if (null != layoutToken)
			attributes.put(
					CompositeLayoutApplicationPresenter.PRESENTER_TOKEN_KEY,
					layoutToken);
		super.registerPage(token, pageClass, attributes);
	}

	private void registerPage(String token, String title, String layoutToken,
			Class<? extends Page> pageClass, Class<?> viewClass) {
		PageAttributes attributes = new PageAttributes();
		attributes.put(PageConstants.PARAM_PAGE_TITLE, title);
		if (null != layoutToken)
			attributes.put(
					CompositeLayoutApplicationPresenter.PRESENTER_TOKEN_KEY,
					layoutToken);
		super.registerPage(token, pageClass, viewClass, attributes);
	}
}