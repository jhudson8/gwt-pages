package com.google.gwt.gwtpages.client.page.parameters;


import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import com.google.gwt.gwtpages.client.Pages;

/**
 * Interface class used to handle parsing history tokens and serializing and
 * de-serializing data from history tokens.
 * 
 * @author Joe Hudson
 */
public interface Tokenizer {

	/**
	 * Return an iterator providing all possible page tokens that can be
	 * identified by the provided history token
	 * 
	 * @param historyToken
	 *            the history token
	 */
	public Iterator<String> getPossiblePageTokens(String historyToken);

	/**
	 * Return the {@link PageParameters} which contains a representation of the
	 * provided history token and page token
	 * 
	 * @param historyToken
	 *            the history token
	 * @param pageToken
	 *            the page token (which will be a subset or equal to the history
	 *            token)
	 */
	public PageParameters getPageParameters(String historyToken,
			String pageToken);

	/**
	 * Create and return the history token represented by the page token and
	 * additional parameters
	 * 
	 * @param pageToken
	 *            the page token
	 * @param parameters
	 *            input parameters
	 */
	public String createHistoryToken(String pageToken, Serializable... parameters);

	/**
	 * Create and return the history token represented by the page token and
	 * additional parameters
	 * 
	 * @param pageToken
	 *            the page token
	 * @param parameters
	 *            input parameters
	 */
	public String createHistoryToken(String pageToken,
			HashMap<String, Serializable> parameters);

	/**
	 * Serialize the entry class to a {@link Long} and return the result
	 * 
	 * @param entry
	 *            the entry to be serialized
	 */
	public Long toLong(Serializable entry);

	/**
	 * Serialize the entry class to a {@link Integer} and return the result
	 * 
	 * @param entry
	 *            the entry to be serialized
	 */
	public Integer toInteger(Serializable entry);

	/**
	 * Serialize the entry class to a {@link Float} and return the result
	 * 
	 * @param entry
	 *            the entry to be serialized
	 */
	public Float toFloat(Serializable entry);

	/**
	 * Serialize the entry class to a {@link Double} and return the result
	 * 
	 * @param entry
	 *            the entry to be serialized
	 */
	public Double toDouble(Serializable entry);

	/**
	 * Serialize the entry class to a {@link Boolean} and return the result
	 * 
	 * @param entry
	 *            the entry to be serialized
	 */
	public Boolean toBoolean(Serializable entry);

	/**
	 * Serialize the entry class to a {@link Date} and return the result
	 * 
	 * @param entry
	 *            the entry to be serialized
	 */
	public Date toDate(Serializable entry);

	/**
	 * Serialize the entry class to a {@link Date} and return the result
	 * 
	 * @param entry
	 *            the entry to be serialized
	 */
	public Date toDateTime(Serializable entry);

	/**
	 * Initialize the loader. This is where all pages would be registered.
	 */
	public void init(Pages settings);
}
