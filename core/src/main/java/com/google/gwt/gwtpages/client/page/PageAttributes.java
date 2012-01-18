package com.google.gwt.gwtpages.client.page;


import java.util.HashMap;

import com.google.gwt.gwtpages.client.page.loader.PageLoader;

/**
 * Meta data that can be used to provide additional information related to a
 * page that is actually decoupled from the page. This is information which is
 * provided by the {@link PageLoader}.
 * 
 * It is meant to be context sensitive. All information referenced with the meta
 * data is keyed by the object class and can then by retrieved using the object
 * class as a key.
 * 
 * @author Joe Hudson
 * 
 */
public class PageAttributes {
	
	private HashMap<Class<?>, Object> impls = new HashMap<Class<?>, Object>();
	private HashMap<Object, Object> data = new HashMap<Object, Object>();

	/**
	 * Add a data attribute
	 * 
	 * @param <R>
	 *            the class type
	 * @param context
	 *            the class (used to key the data)
	 * @param data
	 *            the data class implementation
	 * @return this for chaining
	 */
	public <R> PageAttributes put(Class<R> context, R data) {
		impls.put(context, data);
		return this;
	}

	/**
	 * Add a data attribute
	 * 
	 * @param <R>
	 *            the class type
	 * @param context
	 *            the class (used to key the data)
	 * @param data
	 *            the data class implementation
	 * @return this for chaining
	 */
	public <R> PageAttributes put(Object key, Object data) {
		this.data.put(key, data);
		return this;
	}

	/**
	 * Return the keyed data using the class as the token key
	 * 
	 * @param <R>
	 *            the class type
	 * @param context
	 *            the data class
	 * @return the data instance
	 */
	@SuppressWarnings("unchecked")
	public <R> R get(Class<R> context) {
		return (R) impls.get(context);
	}

	public Object get(Object key) {
		return data.get(key);
	}

	public HashMap<Object, Object> getData() {
		return data;
	}

	public PageAttributes copy() {
		PageAttributes rtn = new PageAttributes();
		rtn.impls.putAll(impls);
		rtn.data.putAll(data);
		return rtn;
	}
}