package com.google.gwt.gwtpages.client.page.parameters;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;

/**
 * Represents an encapsulation of the input parameters associated with the
 * current page.
 * 
 * @author Joe Hudson
 */
public interface PageParameters {

	/**
	 * Return the substring of the history token that identifies this page
	 */
	public String getPageToken();

	/**
	 * Return the history token for this page
	 */
	public String getHistoryToken();

	/**
	 * Return the ordinal parameters
	 */
	public Serializable[] getParameters();

	/**
	 * Return the mapped parameters
	 */
	public HashMap<String, Serializable> getParameterMap();

	/**
	 * Return an ordinal parameter as an {@link Integer} or null
	 * 
	 * @param index
	 *            the parameter index
	 */
	public Integer asInteger(int index);

	/**
	 * Return an ordinal parameter as a {@link Long} or null
	 * 
	 * @param index
	 *            the parameter index
	 */
	public Long asLong(int index);

	/**
	 * Return an ordinal parameter as a {@link Float} or null
	 * 
	 * @param index
	 *            the parameter index
	 */
	public Float asFloat(int index);

	/**
	 * Return an ordinal parameter as a {@link Double} or null
	 * 
	 * @param index
	 *            the parameter index
	 */
	public Double asDouble(int index);

	/**
	 * Return an ordinal parameter as a {@link Date} or null
	 * 
	 * @param index
	 *            the parameter index
	 */
	public Date asDate(int index);

	/**
	 * Return an ordinal parameter as a {@link Date} or null
	 * 
	 * @param index
	 *            the parameter index
	 */
	public Date asDateTime(int index);

	/**
	 * Return an ordinal parameter as a {@link String} or null
	 * 
	 * @param index
	 *            the parameter index
	 */
	public String asString(int index);

	/**
	 * Return an ordinal parameter as a {@link Boolean} or null
	 * 
	 * @param index
	 *            the parameter index
	 */
	public Boolean asBoolean(int index);

	/**
	 * Return a mapped parameter as an {@link Integer} or null
	 * 
	 * @param paramName
	 *            the parameter name
	 */
	public Integer asInteger(String paramName);

	/**
	 * Return a mapped parameter as a {@link Long} or null
	 * 
	 * @param paramName
	 *            the parameter name
	 */
	public Long asLong(String paramName);

	/**
	 * Return a mapped parameter as a {@link Float} or null
	 * 
	 * @param paramName
	 *            the parameter name
	 */
	public Float asFloat(String paramName);

	/**
	 * Return a mapped parameter as a {@link Double} or null
	 * 
	 * @param paramName
	 *            the parameter name
	 */
	public Double asDouble(String paramName);

	/**
	 * Return a mapped parameter as a {@link String} or null
	 * 
	 * @param paramName
	 *            the parameter name
	 */
	public String asString(String paramName);

	/**
	 * Return a mapped parameter as a {@link Boolean} or null
	 * 
	 * @param paramName
	 *            the parameter name
	 */
	public Boolean asBoolean(String paramName);

	/**
	 * Return a mapped parameter as a {@link Date} or null
	 * 
	 * @param paramName
	 *            the parameter name
	 */
	public Date asDate(String paramName);

	/**
	 * Return a mapped parameter as a {@link Date} or null
	 * 
	 * @param paramName
	 *            the parameter name
	 */
	public Date asDateTime(String paramName);

	/**
	 * Return the ordinal parameters size
	 */
	public int listSize();

	/**
	 * Return the mapped parameters size
	 */
	public int mapSize();
}