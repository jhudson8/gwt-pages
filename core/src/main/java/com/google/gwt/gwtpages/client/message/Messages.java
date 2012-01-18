package com.google.gwt.gwtpages.client.message;

import java.util.ArrayList;
import java.util.Iterator;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HasHandlers;
import com.google.gwt.gwtpages.client.Pages;
import com.google.gwt.gwtpages.client.event.MessageChangeEvent;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

/**
 * Controller class for application notification messages. All messages must
 * implement the {@link Message} interface. In most cases, you can use
 * {@link SimpleMessage} as the standard implementation.
 * 
 * @author Joe Hudson
 */
public class Messages {

	private static Messages instance;

	protected HandlerManager eventBus;

	public static Messages get() {
		if (null == instance)
			instance = new Messages(Pages.get());
		return instance;
	}

	public Messages(Pages pages) {
		if (null == pages)
			throw new IllegalArgumentException(
					"The GWT Pages instance must not be null");
		this.eventBus = pages.getEventBus();
		if (null == instance)
			instance = this;
	}

	public Messages() {
		if (null == instance)
			instance = this;
	}

	/**
	 * Clear all application messages and fire the {@link MessageChangeEvent}
	 * event
	 */
	public void clear() {
		fireChangeEvent(MessageChangeEvent.ACTION_CLEAR, null, null);
	}

	/**
	 * Clear all application messages and fire the {@link MessageChangeEvent}
	 * event
	 * 
	 * @param parentContainer
	 *            an optional parent container to use as a root for looking for
	 *            a message handler (and bypass firing the event if found)
	 */
	public void clear(HasWidgets parentContainer) {
		if (!parentContainerCheck(parentContainer,
				MessageChangeEvent.ACTION_CLEAR, null, null))
			fireChangeEvent(MessageChangeEvent.ACTION_CLEAR, null, null);
	}

	/**
	 * Clear all application messages associated with the provided widget and
	 * fire the {@link MessageChangeEvent} event
	 * 
	 * @param widget
	 *            the associated widget
	 */
	public void clearFor(Widget widget) {
		fireChangeEvent(MessageChangeEvent.ACTION_CLEAR, null, widget);
	}

	/**
	 * Clear all application messages associated with the provided widget and
	 * fire the {@link MessageChangeEvent} event
	 * 
	 * @param widget
	 *            the associated widget
	 * @param parentContainer
	 *            an optional parent container to use as a root for looking for
	 *            a message handler (and bypass firing the event if found)
	 */
	public void clearFor(Widget widget, HasWidgets parentContainer) {
		if (!parentContainerCheck(parentContainer,
				MessageChangeEvent.ACTION_CLEAR, null, widget))
			fireChangeEvent(MessageChangeEvent.ACTION_CLEAR, null, widget);
	}

	/**
	 * Erase any existing messages and set the new messages. Then fire the
	 * {@link MessageChangeEvent} event
	 * 
	 * @param parentContainer
	 *            an optional parent container to use as a root for looking for
	 *            a message handler (and bypass firing the event if found)
	 * @param message
	 *            the messages
	 */
	public void setMessage(Message... message) {
		fireChangeEvent(MessageChangeEvent.ACTION_REPLACE, message, null);
	}

	/**
	 * Erase any existing messages and set the new messages. Then fire the
	 * {@link MessageChangeEvent} event.
	 * 
	 * @param parentContainer
	 *            an optional parent container to use as a root for looking for
	 *            a message handler (and bypass firing the event if found)
	 * @param message
	 *            the messages
	 */
	public void setMessage(HasWidgets parentContainer, Message... message) {
		if (!parentContainerCheck(parentContainer,
				MessageChangeEvent.ACTION_REPLACE, message, null))
			fireChangeEvent(MessageChangeEvent.ACTION_REPLACE, message, null);
	}

	/**
	 * Erase any existing messages and set the new messages. Then fire the
	 * {@link MessageChangeEvent} event
	 * 
	 * @param parentContainer
	 *            an optional parent container to use as a root for looking for
	 *            a message handler (and bypass firing the event if found)
	 * @param messages
	 *            the messages
	 */
	public void setMessages(HasWidgets parentContainer,
			ArrayList<Message> messages) {
		Message[] _messages = null != messages ? messages
				.toArray(new Message[messages.size()]) : null;
		if (!parentContainerCheck(parentContainer,
				MessageChangeEvent.ACTION_REPLACE, _messages, null))
			fireChangeEvent(MessageChangeEvent.ACTION_REPLACE, _messages, null);
	}

	/**
	 * Do not erase any existing messages but just add the new messages. Then
	 * fire the {@link MessageChangeEvent} event
	 * 
	 * @param parentContainer
	 *            an optional parent container to use as a root for looking for
	 *            a message handler (and bypass firing the event if found)
	 * @param message
	 *            the messages
	 */
	public void addMessage(Message... message) {
		fireChangeEvent(MessageChangeEvent.ACTION_ADD, message, null);
	}

	/**
	 * Do not erase any existing messages but just add the new messages. Then
	 * fire the {@link MessageChangeEvent} event.
	 * 
	 * @param parentContainer
	 *            an optional parent container to use as a root for looking for
	 *            a message handler (and bypass firing the event if found)
	 * @param message
	 *            the messages
	 */
	public void addMessage(HasWidgets parentContainer, Message... message) {
		if (!parentContainerCheck(parentContainer,
				MessageChangeEvent.ACTION_ADD, message, null))
			fireChangeEvent(MessageChangeEvent.ACTION_ADD, message, null);
	}

	/**
	 * Do not erase any existing messages but just add the new messages. Then
	 * fire the {@link MessageChangeEvent} event
	 * 
	 * @param parentContainer
	 *            an optional parent container to use as a root for looking for
	 *            a message handler (and bypass firing the event if found)
	 * @param messages
	 *            the messages
	 */
	public void addMessages(HasWidgets parentContainer,
			ArrayList<Message> messages) {
		Message[] _messages = null != messages ? messages
				.toArray(new Message[messages.size()]) : null;
		if (!parentContainerCheck(parentContainer,
				MessageChangeEvent.ACTION_ADD, _messages, null))
			fireChangeEvent(MessageChangeEvent.ACTION_ADD, _messages, null);
	}

	/**
	 * Auto-set a success message. Then fire the {@link MessageChangeEvent}
	 * event
	 * 
	 * @param message
	 *            the message text
	 */
	public void success(String message) {
		setMessage(Message.success(message));
	}

	/**
	 * Auto-set an error message. Then fire the {@link MessageChangeEvent} event
	 * 
	 * @param message
	 *            the message text
	 */
	public void error(String message) {
		setMessage(Message.error(message));
	}

	/**
	 * Auto-set an error message. Then fire the {@link MessageChangeEvent} event
	 * 
	 * @param message
	 *            the message text
	 * @param widget
	 *            the widget that is in error
	 */
	public void error(String message, HasHandlers widget) {
		setMessage(Message.error(message, widget));
	}

	/**
	 * Auto-set a warning message. Then fire the {@link MessageChangeEvent}
	 * event
	 * 
	 * @param message
	 *            the message text
	 */
	public void warn(String message) {
		setMessage(Message.warn(message));
	}

	/**
	 * Auto-set an info message. Then fire the {@link MessageChangeEvent} event
	 * 
	 * @param message
	 *            the message text
	 */
	public void info(String message) {
		setMessage(Message.info(message));
	}

	/**
	 * See if a message handler is a child of the parent container and call it
	 * directly without firing the event
	 * 
	 * @param parentContainer
	 *            a parent container or null if N/A
	 * @param message
	 *            the messages
	 * @return true if the messages were handled and false if not
	 */
	private boolean parentContainerCheck(HasWidgets parentContainer,
			int action, Message[] messages, Widget scopedWidget) {
		if (null == parentContainer)
			return false;
		Iterator<Widget> i = parentContainer.iterator();
		while (i.hasNext()) {
			Widget child = i.next();
			if (child instanceof MessageChangeEvent.MessageChangeHandler) {
				// let the child deal with the messages
				MessageChangeEvent.MessageChangeHandler handler = (MessageChangeEvent.MessageChangeHandler) child;
				if (action == MessageChangeEvent.ACTION_REPLACE)
					handler.onReplaceMessages(messages, scopedWidget);
				else if (action == MessageChangeEvent.ACTION_ADD)
					handler.onAddMessages(messages);
				else
					handler.onClearMessages(scopedWidget);
				return true;
			} else if (child instanceof HasWidgets) {
				boolean rtn = parentContainerCheck((HasWidgets) child, action,
						messages, scopedWidget);
				if (rtn)
					return true;
			}
		}
		return false;
	}

	private void fireChangeEvent(int action, Message[] messages,
			Widget scopedWidget) {
		if (null == eventBus)
			eventBus = Pages.get().getEventBus();
		eventBus.fireEvent(new MessageChangeEvent(messages, action,
				scopedWidget));
	}

	/**
	 * Interface defining a container for messages which can be attached to a
	 * request seesion for example
	 * 
	 * @author Joe Hudson
	 */
	public interface Data {
		public Message[] getMessages();
	}
}