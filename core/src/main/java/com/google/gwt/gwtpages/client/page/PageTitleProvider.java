package com.google.gwt.gwtpages.client.page;

/**
 * Interface used to allow pages to dynamically provide their title
 * 
 * @author Joe Hudson
 */
public interface PageTitleProvider {

	/**
	 * Return the page title
	 */
	public String getPageTitle();
}
