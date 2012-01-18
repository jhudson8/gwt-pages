package com.google.gwt.gwtpages.client.page.impl;

import com.google.gwt.gwtpages.client.PageRequestSession;
import com.google.gwt.gwtpages.client.Pages;
import com.google.gwt.gwtpages.client.page.AsyncPageCallback;
import com.google.gwt.gwtpages.client.page.Page;
import com.google.gwt.gwtpages.client.page.parameters.PageParameters;
import com.google.gwt.gwtpages.client.ui.HandlerRegistrationCache;
import com.google.gwt.user.client.ui.Widget;

/**
 * Convenience base class for pages which are not following the MVP pattern.
 * This extends Composite and is handy for use with the UiBinder.
 * 
 * @author Joe Hudson
 * 
 */
public abstract class UiBoundPage<WidgetToCreate extends Widget> implements
		Page {

	protected Pages pages;
	protected WidgetToCreate widget;
	private HandlerRegistrationCache handlerCache;

	/**
	 * This method is call after the page and ui widget have been created and
	 * the widget has been set on this page
	 * 
	 * @param widget
	 *            the widget implementation
	 */
	protected abstract void onConstruct(WidgetToCreate view);

	public void onEnterPage(PageParameters parameters,
			PageRequestSession session, AsyncPageCallback callback) {
	}

	public void onExitPage() {
	}

	public void init(Pages pages) {
		this.pages = pages;
	}

	public void destroy(Pages pages) {
		this.pages = null;
		if (null != handlerCache)
			handlerCache.unbind();
	}

	public Widget asWidget() {
		throw new RuntimeException("You must use GWT.create(...) to create '"
				+ getClass().getName() + "'");
	}

	protected void bindHandlers() {
	}

	protected HandlerRegistrationCache getHandlerCache() {
		if (null == handlerCache)
			handlerCache = createHandlerCache();
		return handlerCache;
	}

	protected HandlerRegistrationCache createHandlerCache() {
		return new HandlerRegistrationCache();
	}
}