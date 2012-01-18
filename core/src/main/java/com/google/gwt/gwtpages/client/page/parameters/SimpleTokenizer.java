package com.google.gwt.gwtpages.client.page.parameters;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import com.google.gwt.gwtpages.client.Pages;
import com.google.gwt.http.client.URL;
import com.google.gwt.i18n.client.DateTimeFormat;

/**
 * Default implementation of the {@link Tokenizer}. Standard history
 * token formats are: 1) ordinal parameters: {page token}/param1/param2... 2)
 * mapped parameters: {page token}/&param1Key=param1Value&param2Key=param2Value
 * 
 * @author Joe Hudson
 */
public class SimpleTokenizer implements Tokenizer {

	private String separator = "/";
	private String namedParameterToken = "&";
	private DateTimeFormat dateFormat;
	private DateTimeFormat dateTimeFormat;
	private DateTimeFormat timeOnlyFormat;

	public SimpleTokenizer() {
		this(DateTimeFormat.getShortDateFormat(), DateTimeFormat
				.getShortDateTimeFormat());
	}

	public void init(Pages settings) {
	}

	public SimpleTokenizer(DateTimeFormat dateFormat,
			DateTimeFormat dateTimeFormat) {
		this.dateFormat = dateFormat;
		this.dateTimeFormat = dateTimeFormat;
	}

	public Iterator<String> getPossiblePageTokens(String token) {
		if (null == token)
			return new ArrayList<String>().iterator();
		else {
			String[] tokens = token.split(separator);
			ArrayList<String> rtn = new ArrayList<String>();
			String _token = null;
			for (String s : tokens) {
				if (null != _token)
					_token = _token + "/" + s;
				else
					_token = s;
				rtn.add(_token);
			}
			return rtn.iterator();
		}
	}

	public PageParameters getPageParameters(String historyToken,
			String pageToken) {

		Serializable[] parameters = null;
		HashMap<String, Serializable> parametersMap = null;

		String parametersPart = (null == historyToken || historyToken
				.equals(pageToken)) ? "" : historyToken.substring(
				pageToken.length()).trim();
		while (parametersPart.startsWith(separator))
			parametersPart = parametersPart.substring(1);
		while (parametersPart.endsWith(separator))
			parametersPart = parametersPart.substring(0,
					parametersPart.length() - 1);
		if (parametersPart.length() == 0) {
			parameters = new Serializable[0];
		} else {
			String[] _parameters = parametersPart.split(separator);
			if (_parameters.length == 1
					&& _parameters[0].startsWith(namedParameterToken)) {
				parametersMap = new HashMap<String, Serializable>();
				// parameters are stored as a map
				_parameters = _parameters[0].split(namedParameterToken);
				for (String s : _parameters) {
					int index = s.indexOf('=');
					if (index > 0) {
						String key = s.substring(0, index);
						String value = s.substring(index + 1);
						parametersMap.put(decode(key), decode(value));
					}
				}
			} else {
				for (int i = 0; i < _parameters.length; i++) {
					// decode
					_parameters[i] = decode(_parameters[i]);
				}
				parameters = new Serializable[_parameters.length];
				System.arraycopy(_parameters, 0, parameters, 0,
						_parameters.length);
			}
		}
		return new SimplePageParameters(pageToken, historyToken, parameters,
				parametersMap);
	}

	public String createHistoryToken(String pageToken,
			HashMap<String, Serializable> parameters) {
		StringBuilder sb = new StringBuilder();
		sb.append(pageToken);
		if (null != parameters && parameters.size() > 0) {
			sb.append(separator);
			for (String key : parameters.keySet()) {
				Serializable value = parameters.get(key);
				if (null != value) {
					sb.append(namedParameterToken)
							.append(encode(key))
							.append('=')
							.append(encode(toString(value, false)));
				}
			}
		}
		return sb.toString();
	}

	public String createHistoryToken(String pageToken,
			Serializable... parameters) {
		StringBuilder sb = new StringBuilder();
		sb.append(pageToken);
		if (null != parameters && parameters.length > 0) {
			for (Serializable parameter : parameters) {
				if (null != parameter) {
					sb.append(separator).append(
							encode(toString(parameter, false)));
				}
			}
		}
		return sb.toString();
	}

	public Long toLong(Serializable entry) {
		if (null == entry)
			return null;
		else if (entry instanceof Long)
			return (Long) entry;
		else if (entry instanceof Number)
			return ((Number) entry).longValue();
		else
			return new Long(entry.toString());
	}

	public Integer toInteger(Serializable entry) {
		if (null == entry)
			return null;
		else if (entry instanceof Integer)
			return (Integer) entry;
		else if (entry instanceof Number)
			return ((Number) entry).intValue();
		else
			return new Integer(entry.toString());
	}

	public Float toFloat(Serializable entry) {
		if (null == entry)
			return null;
		else if (entry instanceof Float)
			return (Float) entry;
		else if (entry instanceof Number)
			return ((Number) entry).floatValue();
		else
			return new Float(entry.toString());
	}

	public Double toDouble(Serializable entry) {
		if (null == entry)
			return null;
		else if (entry instanceof Double)
			return (Double) entry;
		else if (entry instanceof Number)
			return ((Number) entry).doubleValue();
		else
			return new Double(entry.toString());
	}

	public Boolean toBoolean(Serializable entry) {
		if (null == entry)
			return null;
		else if (entry instanceof Boolean)
			return (Boolean) entry;
		else
			return new Boolean(entry.toString());

	}

	public Date toDate(Serializable entry) {
		if (null == entry)
			return null;
		else if (entry instanceof Date)
			return (Date) entry;
		else if (entry instanceof Long)
			return new Date((Long) entry);
		else
			return dateFormat.parse(entry.toString());
	}

	public Date toDateTime(Serializable entry) {
		if (null == entry)
			return null;
		else if (entry instanceof Date)
			return (Date) entry;
		else if (entry instanceof Long)
			return new Date((Long) entry);
		else
			return dateTimeFormat.parse(entry.toString());
	}

	public String toString(Serializable entry, boolean allowNull) {
		if (null == entry)
			return allowNull ? null : "";
		else if (entry instanceof Long)
			return toString((Long) entry, allowNull);
		else if (entry instanceof Integer)
			return toString((Integer) entry, allowNull);
		else if (entry instanceof Float)
			return toString((Float) entry, allowNull);
		else if (entry instanceof Double)
			return toString((Double) entry, allowNull);
		else if (entry instanceof Date)
			return toString((Date) entry, allowNull);
		else
			return entry.toString();
	}

	public String toString(Long entry, boolean allowNull) {
		if (null == entry)
			return allowNull ? null : "";
		else
			return entry.toString();
	}

	public String toString(Integer entry, boolean allowNull) {
		if (null == entry)
			return allowNull ? null : "";
		else
			return entry.toString();
	}

	public String toString(Float entry, boolean allowNull) {
		if (null == entry)
			return allowNull ? null : "";
		else
			return entry.toString();
	}

	public String toString(Double entry, boolean allowNull) {
		if (null == entry)
			return allowNull ? null : "";
		else
			return entry.toString();
	}

	public String toString(Date entry, boolean allowNull) {
		if (null == timeOnlyFormat)
			timeOnlyFormat = DateTimeFormat.getFormat("HmsS");
		if (null == entry)
			return allowNull ? null : "";
		else {
			Date date = (Date) entry;
			Integer timeInt = new Integer(timeOnlyFormat.format(date));
			if (timeInt == 0)
				return dateFormat.format(entry);
			else
				return dateTimeFormat.format(entry);
		}
	}

	public String encode(String s) {
		return URL.encode(s);
	}

	public String decode(String s) {
		return URL.decode(s);
	}
}