package com.google.gwt.gwtpages.client.message;

import com.google.gwt.gwtpages.client.PageRequestSession;

public class PageRequestSessionWithMessage extends PageRequestSession implements Messages.Data {

	private Message[] messages;

	public PageRequestSessionWithMessage() {
		this(null);
	}

	public PageRequestSessionWithMessage(String successMessage) {
		put(Messages.Data.class, this);
		this.messages = new Message[]{Message.success(successMessage)};
	}

	/**
	 * Return all messages for this session
	 */
	public Message[] getMessages() {
		return messages;
	}

	/**
	 * Add 1 or many messages to the session
	 * @param message the message
	 * @return this for chaining
	 */
	public PageRequestSessionWithMessage add(Message... message) {
		if (null == this.messages || this.messages.length == 0) {
			this.messages = message;
		}
		else {
			Message[] _messages = new Message[this.messages.length+message.length];
			System.arraycopy(this.messages, 0, _messages, 0, this.messages.length);
			System.arraycopy(message, 0, _messages, this.messages.length, message.length);
			this.messages = _messages;
		}
		return this;
	}
}
