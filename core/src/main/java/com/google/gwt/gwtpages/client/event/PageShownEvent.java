/*
 * GNU GENERAL PUBLIC LICENSE, Version 3, 29 June 2007
 */
package com.google.gwt.gwtpages.client.event;


import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.gwtpages.client.GotoPageCommand;
import com.google.gwt.gwtpages.client.page.LoadedPageContainer;
import com.google.gwt.gwtpages.client.page.parameters.PageParameters;

/**
 * Event representing that a page has been shown.
 * 
 * @author Joe Hudson
 */
public class PageShownEvent extends GwtEvent<PageShownEvent.PageShownHandler> {

	public interface PageShownHandler extends EventHandler {
		/**
		 * Event handler fired when a page is shown using the display manager
		 * 
		 * @param currentPageData
		 *            the page load result for the page to be shown
		 * @param previousPageData
		 *            the page load result for the previous page
		 * @param pageStack
		 *            the current page stack
		 */
		void onPageShown(LoadedPageContainer currentPageData, PageParameters parameters, GotoPageCommand command,
				LoadedPageContainer previousPageData);
	}

	private LoadedPageContainer previousPageData;
	private LoadedPageContainer currentPageData;
	private PageParameters parameters;
	private GotoPageCommand command;

	public PageShownEvent(LoadedPageContainer currentPageData, PageParameters parameters, GotoPageCommand command,
			LoadedPageContainer previousPageData) {
		this.currentPageData = currentPageData;
		this.parameters = parameters;
		this.command = command;
		this.previousPageData = previousPageData;
	}

	public static final GwtEvent.Type<PageShownEvent.PageShownHandler> TYPE = new GwtEvent.Type<PageShownHandler>();

	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<PageShownHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(PageShownEvent.PageShownHandler handler) {
		handler.onPageShown(currentPageData, parameters, command, previousPageData);
	}
}