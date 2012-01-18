/*
 * GNU GENERAL PUBLIC LICENSE, Version 3, 29 June 2007
 */
package com.google.gwt.gwtpages.client.event;

import java.io.Serializable;

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
public class PageIllegalAccessEvent extends
		GwtEvent<PageIllegalAccessEvent.IllegalAccessHandler> {

	public interface IllegalAccessHandler extends EventHandler {
		/**
		 * Event handler fired when a user tried to view a page they did not
		 * have the rights to do so
		 * 
		 * @param pageContainer
		 *            the page data
		 * @param parameters
		 *            the request parameters
		 * @param command
		 *            the {@link GotoPageCommand}
		 * @param exceptionParameters
		 *            any additional parameters provided when declaring the
		 *            illegal access
		 */
		void onIllegalAccess(LoadedPageContainer pageContainer,
				PageParameters parameters, GotoPageCommand command,
				Serializable[] exceptionParameters);
	}

	private LoadedPageContainer pageData;
	private PageParameters parameters;
	private GotoPageCommand command;
	private Serializable[] exceptionParameters;

	public PageIllegalAccessEvent(LoadedPageContainer pageData,
			PageParameters parameters, GotoPageCommand command, Serializable[] exceptionParameters) {
		this.pageData = pageData;
		this.parameters = parameters;
		this.command = command;
		this.exceptionParameters = exceptionParameters;
	}

	public static final GwtEvent.Type<PageIllegalAccessEvent.IllegalAccessHandler> TYPE = new GwtEvent.Type<IllegalAccessHandler>();

	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<IllegalAccessHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(PageIllegalAccessEvent.IllegalAccessHandler handler) {
		handler.onIllegalAccess(pageData, parameters, command, exceptionParameters);
	}
}