package com.google.gwt.gwtpages.client;

import com.google.gwt.gwtpages.client.page.PageAttributes;

/**
 * The transient session data associate with a page request
 *  
 * @author Joe Hudson
 */
public class PageRequestSession extends PageAttributes {

	private PageAttributes pageAttributes;

	/**
	 * If the session value exists, return that.  Otherwise if alsoCheckPageAttributes=true,
	 * 	return the page attributes value for that context if exists
	 * @param <R> the object type
	 * @param context the object class
	 * @param alsoCheckPageAttributes true to check page attributes and false if not
	 */
	public <R> R get(Class<R> context, boolean alsoCheckPageAttributes) {
		if (!alsoCheckPageAttributes) return super.get(context);
		else {
			R rtn = super.get(context);
			if (null != rtn || null == pageAttributes) return rtn;
			else return pageAttributes.get(context);
		}
	}

	/**
	 * If the session value exists, return that.  Otherwise if alsoCheckPageAttributes=true,
	 * 	return the page attributes value for that context if exists
	 * @param <R> the object type
	 * @param key the data key
	 * @param alsoCheckPageAttributes true to check page attributes and false if not
	 */
	public Object get(Object key, boolean alsoCheckPageAttributes) {
		if (!alsoCheckPageAttributes) return super.get(key);
		else {
			Object rtn = super.get(key);
			if (null != rtn || null == pageAttributes) return rtn;
			else return pageAttributes.get(key);
		}
	}

	void setPageAttributes(PageAttributes pageAttributes) {
		this.pageAttributes = pageAttributes;
	}
}
