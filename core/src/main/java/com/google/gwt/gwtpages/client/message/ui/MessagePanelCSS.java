package com.google.gwt.gwtpages.client.message.ui;

import com.google.gwt.gwtpages.client.message.Message;
import com.google.gwt.resources.client.CssResource;

public interface MessagePanelCSS extends CssResource {

	/**
	 * css class always applied to the parent container
	 */
	String messages();

	/**
	 * css class applied to the parent container if the messages are not all of the same type
	 */
	String multiMessages();

	/**
	 * css class applied to the parent container if the messages are all of type {@link Message#LEVEL_INFO}
	 */
	String infoMessages();

	/**
	 * css class applied to the parent container if the messages are all of type {@link Message#LEVEL_SUCCESS}
	 */
	String successMessages();

	/**
	 * css class applied to the parent container if the messages are all of type {@link Message#LEVEL_WARN}
	 */
	String warnMessages();

	/**
	 * css class applied to the parent container if the messages are all of type {@link Message#LEVEL_ERROR}
	 */
	String errorMessages();

	/**
	 * css class applied to all message entries
	 */
	String message();

	/**
	 * css class applied to all message entries of type {@link Message#LEVEL_INFO}
	 */
	String infoMessage();

	/**
	 * css class applied to all message entries of type {@link Message#LEVEL_SUCCESS}
	 */
	String successMessage();

	/**
	 * css class applied to all message entries of type {@link Message#LEVEL_WARN}
	 */
	String warnMessage();

	/**
	 * css class applied to all message entries of type {@link Message#LEVEL_ERROR}
	 */
	String errorMessage();
}
