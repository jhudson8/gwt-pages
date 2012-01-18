package com.google.gwt.gwtpages.client;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import com.google.gwt.user.client.Command;

/**
 * Command class used as the page navigation instruction set for {@link Pages}
 * @author Joe
 *
 */
public class GotoPageCommand implements Command {

	private Pages pages;
	private HashMap<String, Serializable> parameterMap;
	private HashMap<Object, Object> transientParameterMap;
	private ArrayList<Serializable> parameterList;
	private boolean addHistoryToken = true;
	private String pageToken;
	private PageRequestSession session;

	GotoPageCommand(String pageToken, Pages pages) {
		this(pageToken, new PageRequestSession(), pages);
	}

	GotoPageCommand(String pageToken, PageRequestSession session, Pages pages) {
		this.pageToken = pageToken;
		this.session = session;
		this.pages = pages;
	}

	/**
	 * Execute this command
	 */
	public void execute() {
		pages.goTo(this);
	}

	/**
	 * Return all parameters that will not show up in the history token
	 */
	public HashMap<Object, Object> getTransientParameterMap() {
		return getTransientParameterMap(true);
	}

	/**
	 * Return all parameters that will not show up in the history token
	 * @param doCreate create a new one if non-existing
	 */
	HashMap<Object, Object> getTransientParameterMap(boolean doCreate) {
		if (doCreate && null == transientParameterMap)
			transientParameterMap = new HashMap<Object, Object>();
		return transientParameterMap;
	}

	/**
	 * Set a map of all parameters that will not show up in the history token
	 * @param transientParameterMap the map to set
	 * @return this for chaining
	 */
	public GotoPageCommand setTransientParameterMap(
			HashMap<Object, Object> transientParameterMap) {
		this.transientParameterMap = transientParameterMap;
		return this;
	}

	/**
	 * Return a list of all ordinal page request parameters (will never be null)
	 */
	public ArrayList<Serializable> getParameterList() {
		return getParameterList(true);
	}

	/**
	 * Return the list of all ordinal page request parameters
	 * @param doCreate create a new one if non-existing
	 */
	ArrayList<Serializable> getParameterList(boolean doCreate) {
		if (doCreate && null == parameterList)
			parameterList = new ArrayList<Serializable>();
		return parameterList;
	}

	public GotoPageCommand addParameters(Serializable... parameters) {
		if (null != parameterMap) throw new RuntimeException("You can not add both list and serializable map parameters at the same time");
		if (null != parameters) {
			for (Serializable s : parameters)
				getParameterList().add(s);
		}
		return this;
	}

	/**
	 * Add an ordinal page parameter (mutually exclusive with mapped parameters)
	 * @param parameter the parameter to add
	 * @return this for chaining
	 */
	public GotoPageCommand addParameter(Serializable parameter) {
		if (null != parameterMap) throw new RuntimeException("You can not add both list and serializable map parameters at the same time");
		getParameterList().add(parameter);
		return this;
	}

	/**
	 * Set all ordinal page request parameters
	 * @param parameters the parameters
	 * @return this for chaining
	 */
	public GotoPageCommand setParameters(ArrayList<Serializable> parameters) {
		if (null != parameterMap) throw new RuntimeException("You can not add both list and serializable map parameters at the same time");
		this.parameterList = parameters;
		return this;
	}

	/**
	 * Add a single mapped parameter (mutually exclusive with ordinal parameters)
	 * @param key the parameter key
	 * @param value the parameter value
	 * @return
	 */
	public GotoPageCommand addParameter(String key, Serializable value) {
		if (null != parameterList) throw new RuntimeException("You can not add both list and serializable map parameters at the same time");
		getParameterMap().put(key, value);
		return this;
	}

	/**
	 * Add a parameter which will not show up in the history token
	 * @param key the parameter key
	 * @param value the parameter value
	 */
	public GotoPageCommand addTransientParameter(Object key, Object value) {
		getTransientParameterMap().put(key, value);
		return this;
	}

	/**
	 * Should the history token be added (should the browser URL change?)
	 */
	public boolean shouldAddHistoryToken() {
		return addHistoryToken;
	}

	/**
	 * Return the requested page token
	 */
	public String getPageToken() {
		return pageToken;
	}

	/**
	 * Return a map of all transient parameters (will never be null)
	 */
	public HashMap<String, Serializable> getParameterMap() {
		return getParameterMap(true);
	}

	/**
	 * Return the map of all standard parameters
	 * @param doCreate true to create the map if null
	 */
	HashMap<String, Serializable> getParameterMap(boolean doCreate) {
		if (doCreate && null == parameterMap)
			parameterMap = new HashMap<String, Serializable>();
		return parameterMap;
	}

	/**
	 * Set the map of page parameters (mutually exclusive with ordinal parameters)
	 * @param parameterMap the map to set
	 */
	public GotoPageCommand setParameters(
			HashMap<String, Serializable> parameterMap) {
		if (null != parameterList) throw new RuntimeException("You can not add both list and serializable map parameters at the same time");
		this.parameterMap = parameterMap;
		return this;
	}

	/**
	 * Return the session associated with this page request
	 */
	public PageRequestSession getSession() {
		return session;
	}

	/**
	 * Mark whether we should add the history token or not
	 * @param addHistoryToken true to add and false to keep the browser URL unchanged
	 */
	public GotoPageCommand addHistoryToken(boolean addHistoryToken) {
		this.addHistoryToken = addHistoryToken;
		return this;
	}
}