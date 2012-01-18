package com.google.gwt.gwtpages.client.page.parameters;


import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;

import com.google.gwt.gwtpages.client.Pages;
import com.google.gwt.http.client.URL;

/**
 * Simple implementation of the {@link PageParameters}
 * 
 * @author Joe Hudson
 */
public class SimplePageParameters implements PageParameters {

	private Pages pages;

	public SimplePageParameters (Pages pages) {
		this.pages = pages;
	}
	
	private String pageToken;
	private String historyToken;
	private Serializable[] parameters;
	private HashMap<String, Serializable> parametersMap;

	public SimplePageParameters(String pageToken, String historyToken, Serializable[] parameters,
			HashMap<String, Serializable> parametersMap) {
		this.pageToken = pageToken;
		this.historyToken = historyToken;
		this.parameters = parameters;
		this.parametersMap = parametersMap;
	}

	public String getPageToken() {
		return pageToken;
	}

	public String getHistoryToken() {
		return historyToken;
	}

	public Serializable[] getParameters() {
		return parameters;
	}

	public HashMap<String, Serializable> getParameterMap() {
		return parametersMap;
	}

	public Integer asInteger(int index) {
		return pages.getSettings().getTokenizer().toInteger(get(index));
	}

	public Long asLong(int index) {
		return pages.getSettings().getTokenizer().toLong(get(index));
	}

	public Float asFloat(int index) {
		return pages.getSettings().getTokenizer().toFloat(get(index));
	}

	public Double asDouble(int index) {
		return pages.getSettings().getTokenizer().toDouble(get(index));
	}

	public String asString(int index) {
		Serializable rtn = get(index);
		return (null == rtn) ? null : rtn.toString();
	}

	public Boolean asBoolean(int index) {
		return pages.getSettings().getTokenizer().toBoolean(get(index));
	}

	public Date asDate(int index) {
		return pages.getSettings().getTokenizer().toDate(get(index));
	}

	public Date asDateTime(int index) {
		return pages.getSettings().getTokenizer().toDateTime(get(index));
	}

	public Integer asInteger(String parameterName) {
		return pages.getSettings().getTokenizer().toInteger(get(parameterName));
	}

	public Long asLong(String parameterName) {
		return pages.getSettings().getTokenizer().toLong(get(parameterName));
	}

	public Float asFloat(String parameterName) {
		return pages.getSettings().getTokenizer().toFloat(get(parameterName));
	}

	public Double asDouble(String parameterName) {
		return pages.getSettings().getTokenizer().toDouble(get(parameterName));
	}

	public String asString(String parameterName) {
		Serializable rtn = get(parameterName);
		return (null == rtn) ? null : rtn.toString();
	}

	public Boolean asBoolean(String parameterName) {
		return pages.getSettings().getTokenizer().toBoolean(get(parameterName));
	}

	public Date asDate(String parameterName) {
		return pages.getSettings().getTokenizer().toDate(get(parameterName));
	}

	public Date asDateTime(String parameterName) {
		return pages.getSettings().getTokenizer().toDateTime(get(parameterName));
	}

	private Serializable get(int index) {
		if (null == parameters || parameters.length <= index)
			return null;
		else
			return parameters[index];
	}

	private Serializable get(String key) {
		if (null == parametersMap)
			return null;
		else
			return parametersMap.get(key);
	}

	public int listSize() {
		if (null != parameters)
			return parameters.length;
		else
			return 0;
	}

	public int mapSize() {
		if (null != parametersMap)
			return parametersMap.size();
		else
			return 0;
	}

	protected String encode(String s) {
		return URL.encode(s);
	}

	protected String decode(String s) {
		return URL.decode(s);
	}
}