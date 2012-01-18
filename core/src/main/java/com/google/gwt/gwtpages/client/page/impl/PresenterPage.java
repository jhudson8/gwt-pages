package com.google.gwt.gwtpages.client.page.impl;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.gwtpages.client.Pages;
import com.google.gwt.gwtpages.client.page.Page;
import com.google.gwt.gwtpages.client.ui.HandlerRegistrationCache;
import com.google.gwt.user.client.ui.Widget;

/**
 * Convenience base class for pages using the MVP Pattern
 *
 * @author Joe Hudson
 */
public abstract class PresenterPage<View> implements Page {

	protected Pages pages;
	private Widget view;
	private HandlerRegistrationCache handlerCache;

	public void fireEvent(GwtEvent<?> event) {
		pages.getEventBus().fireEvent(event);
	}

	/**
	 * This method is call after the presenter and view have been created
	 * and the view has been set on the presenter
	 * @param view the view implementation
	 */
	protected abstract void onConstruct(View view);

	public void setView(Widget view) {
		this.view = view;
	}

	public View getView() {
		return (View) view;
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

	public Object getRawView() {
		return view;
	}

	public Widget asWidget() {
		return (Widget) view;
	}

	protected void bindHandlers() {
	}

	protected HandlerRegistrationCache getHandlerCache() {
		if (null == handlerCache) handlerCache = createHandlerCache();
		return handlerCache;
	}

	protected HandlerRegistrationCache createHandlerCache() {
		return new HandlerRegistrationCache();
	}
}