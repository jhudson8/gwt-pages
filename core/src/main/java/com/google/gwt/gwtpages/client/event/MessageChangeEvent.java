/*
 * GNU GENERAL PUBLIC LICENSE, Version 3, 29 June 2007
 */
package com.google.gwt.gwtpages.client.event;


import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;
import com.google.gwt.gwtpages.client.message.Message;
import com.google.gwt.user.client.ui.Widget;

/**
 * Event representing a change in notification messages
 * 
 * @author Joe Hudson
 */
public class MessageChangeEvent extends
		GwtEvent<MessageChangeEvent.MessageChangeHandler> {

	public static final int ACTION_ADD = 1;
	public static final int ACTION_REPLACE = 2;
	public static final int ACTION_CLEAR = 3;
	
	public interface MessageChangeHandler extends EventHandler {
		/**
		 * Replace all existing messages with the provided messages
		 * 
		 * @param messages
		 *            the messages
		 * @param scopedWidget only erase the messages associated with this widget if not null
		 */
		void onReplaceMessages(Message[] messages, HasHandlers scopedWidget);

		/**
		 * Add the provided messages but do not erase the existing messages
		 * 
		 * @param messages
		 *            the messages
		 */
		void onAddMessages(Message[] messages);

		/**
		 * Erase all existing messages
		 * 
		 * @param scopedWidget only erase the messages associated with this widget if not null
		 */
		void onClearMessages(HasHandlers scopedWidget);
	}

	private int action;
	private Message[] messages;
	private HasHandlers scopedWidget;

	public MessageChangeEvent(Message[] messages, int action, Widget scopedWidget) {
		this.action = action;
		this.messages = messages;
		this.scopedWidget = scopedWidget;
	}

	public static final GwtEvent.Type<MessageChangeEvent.MessageChangeHandler> TYPE = new GwtEvent.Type<MessageChangeHandler>();

	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<MessageChangeHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(MessageChangeEvent.MessageChangeHandler handler) {
		if (action == ACTION_REPLACE)
			handler.onReplaceMessages(messages, scopedWidget);
		else if (action == ACTION_CLEAR)
			handler.onClearMessages(scopedWidget);
		else
			handler.onAddMessages(messages);
	}
}