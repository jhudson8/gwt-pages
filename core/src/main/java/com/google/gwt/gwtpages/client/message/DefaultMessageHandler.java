package com.google.gwt.gwtpages.client.message;

import java.io.Serializable;

import com.google.gwt.gwtpages.client.GotoPageCommand;
import com.google.gwt.gwtpages.client.PageRequestSession;
import com.google.gwt.gwtpages.client.Pages;
import com.google.gwt.gwtpages.client.page.LoadedPageContainer;
import com.google.gwt.gwtpages.client.page.event.PageEventHandler;
import com.google.gwt.gwtpages.client.page.parameters.PageParameters;
import com.google.gwt.user.client.Command;

/**
 * Default event handler to handle request session messages. This uses
 * {@link Messages} as the message handler. It is very simple to add request
 * session messages.
 * <ul>
 * <li>If you want a simple success message, you can use:
 * <code>{@link Pages#gotoPage(String, PageRequestSession)}.
 * 		For the {@link PageRequestSession} instance, you can use {@link PageRequestSessionWithMessage#PageRequestSessionWithMessage(String)}
 * </li>
 * <li>If you want some kind of message that is not a success message, use the
 * code from above but change the constructor to
 * {@link PageRequestSessionWithMessage#PageRequestSessionWithMessage()} and
 * then call {@link PageRequestSessionWithMessage#add(Message...)}. To easily
 * make messages of different types, take a look at
 * {@link SimpleMessage#error(String)}, {@link SimpleMessage#info(String)},
 * {@link SimpleMessage#success(String)}</li>
 * <li>
 * If you are already using your own instance of a {@link PageRequestSession},
 * you can simply call {@link PageRequestSession#put(Class, Object)} using the
 * class type of {@link Messages.Data} and your own instance of that class.</li>
 * </ul>
 * 
 * @author Joe Hudson
 */
public class DefaultMessageHandler implements PageEventHandler {

	private Messages messages;

	public DefaultMessageHandler() {
	}

	public DefaultMessageHandler(Messages messages) {
		this.messages = messages;
	}
	
	public void init(Pages pages) {
	}

	public void onAfterPageEnter(LoadedPageContainer pageLoadResult,
			PageParameters parameters, GotoPageCommand command) {
	}

	public void onPageEnterSuccess(LoadedPageContainer pageLoadResult,
			PageParameters parameters, GotoPageCommand command) {
	}

	public Command onPageEnterFailure(LoadedPageContainer pageLoadResult,
			PageParameters parameters, GotoPageCommand command) {
		return null;
	}

	public void onPageRequest(String pageToken, String historyToken,
			PageRequestSession session) {
		getMessages().clear();
	}

	public void onBeforePageEnter(LoadedPageContainer pageLoadResult,
			PageParameters parameters, GotoPageCommand command) {
		Messages.Data messageData = command.getSession().get(
				Messages.Data.class);
		if (null != messageData) {
			Message[] messages = messageData.getMessages();
			if (null != messages && messages.length > 0) {
				getMessages().setMessage(messages);
			}
		}
	}

	public void onPageLoaded(LoadedPageContainer result) {
	}

	public Command onPageNotFound(String historyToken) {
		return null;
	}

	public Command onPageLoadFailure(String historyToken, Throwable cause) {
		return null;
	}

	public void onPageWaitForAsync(LoadedPageContainer loadedPageContainer,
			PageParameters parameters, GotoPageCommand command) {
	}

	public Command onIllegalPageAccess(LoadedPageContainer loadedPageContainer,
			PageParameters pageParameters, GotoPageCommand command,
			Serializable... parameters) {
		return null;
	}

	public void onPageRedirect(LoadedPageContainer currentLoadedPageContainer,
			PageParameters currentParameters, GotoPageCommand currentCommand,
			GotoPageCommand forwardCommand) {
	};

	protected Messages getMessages() {
		if (null == messages) messages = Messages.get();
		return messages;
	}
}