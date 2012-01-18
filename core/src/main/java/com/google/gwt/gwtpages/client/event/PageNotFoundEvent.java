/*
 * GNU GENERAL PUBLIC LICENSE, Version 3, 29 June 2007
 */
package com.google.gwt.gwtpages.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

/**
 * Event representing that a page could not be found.
 * 
 * @author Joe Hudson
 */
public class PageNotFoundEvent extends GwtEvent<PageNotFoundEvent.PageNotFound> {

	public interface PageNotFound extends EventHandler {
		/**
		 * Event handler fired when a page could not be located matching the provided history token
		 * 
		 * @param historyToken the history token
		 */
		void onPageNotFound(String historyToken);
	}

	private String historyToken;

	public PageNotFoundEvent(String historyToken) {
		this.historyToken = historyToken;
	}

	public static final GwtEvent.Type<PageNotFoundEvent.PageNotFound> TYPE = new GwtEvent.Type<PageNotFound>();

	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<PageNotFound> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(PageNotFoundEvent.PageNotFound handler) {
		handler.onPageNotFound(historyToken);
	}
}