package com.google.gwt.gwtpages.client.message;

import com.google.gwt.event.shared.HasHandlers;

/**
 * Interface used as the notification entity for GWT Pages. All instances are
 * used with {@link Messages} for application control.
 * 
 * @author Joe Hudson
 */
public class Message {

	public static final int LEVEL_FATAL = 10000;
	public static final int LEVEL_ERROR = 20000;
	public static final int LEVEL_WARN = 30000;
	public static final int LEVEL_INFO = 40000;
	public static final int LEVEL_SUCCESS = 50000;

	private String message;
	private HasHandlers widget;
	private int level;

	public Message(String message, int level) {
		this.message = message;
		this.level = level;
	}

	public Message(String message, int level, HasHandlers widget) {
		this.message = message;
		this.widget = widget;
		this.level = level;
	}

	/**
	 * Utility method to create a success message
	 * 
	 * @param message
	 *            the message text
	 */
	public static Message success(String message) {
		return new Message(message, LEVEL_SUCCESS, null);
	}

	/**
	 * Utility method to create an info message
	 * 
	 * @param message
	 *            the message text
	 */
	public static Message info(String message) {
		return new Message(message, LEVEL_INFO, null);
	}

	/**
	 * Utility method to create an info message
	 * 
	 * @param message
	 *            the message text
	 */
	public static Message error(String message) {
		return new Message(message, LEVEL_ERROR, null);
	}

	/**
	 * Utility method to create an warn message
	 * 
	 * @param message
	 *            the message text
	 */
	public static Message warn(String message) {
		return new Message(message, LEVEL_WARN, null);
	}

	/**
	 * Utility method to create an error message
	 * 
	 * @param message
	 *            the message text
	 * @param component
	 *            the associated component or null if N/A
	 */
	public static Message error(String message, HasHandlers component) {
		return new Message(message, LEVEL_ERROR, component);
	}

	/**
	 * Return the message text
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * Set the message text
	 * 
	 * @param message
	 *            the message text
	 */
	public Message setMessage(String message) {
		this.message = message;
		return this;
	}

	/**
	 * Return the widget associated with the message
	 */
	public HasHandlers getWidget() {
		return widget;
	}

	/**
	 * Set the associated widget
	 * 
	 * @param widget
	 *            the widget
	 */
	public Message setWidget(HasHandlers widget) {
		this.widget = widget;
		return this;
	}

	/**
	 * Return the message level.  See the LEVEL_* statics on this class
	 */
	public int getLevel() {
		return level;
	}

	/**
	 * Set the message level
	 * 
	 * @param level
	 *            the message level
	 */
	public Message setLevel(int level) {
		this.level = level;
		return this;
	}
}