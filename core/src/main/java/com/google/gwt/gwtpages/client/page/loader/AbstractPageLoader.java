package com.google.gwt.gwtpages.client.page.loader;

import java.util.HashMap;
import java.util.Iterator;

import com.google.gwt.gwtpages.client.Pages;
import com.google.gwt.gwtpages.client.message.exceptions.PageNotFoundException;
import com.google.gwt.gwtpages.client.page.Page;
import com.google.gwt.gwtpages.client.page.PageAttributes;

public abstract class AbstractPageLoader implements PageLoader {

	private HashMap<String, PageRegistry> registeredTokens = new HashMap<String, PageRegistry>();

	public Iterator<String> getValidPageTokens() {
		return registeredTokens.keySet().iterator();
	}

	public boolean isValidPageToken(String pageToken) {
		return (null != registeredTokens.get(pageToken));
	}

	public PageAttributes getPageAttributes(String pageToken)
			throws PageNotFoundException {
		PageRegistry register = registeredTokens.get(pageToken);
		if (null == register)
			throw new PageNotFoundException(pageToken);
		else
			return register.pageAttributes;
	}

	/**
	 * Register a page
	 * 
	 * @param token
	 *            the page token
	 * @param pageClass
	 *            the page class (presenter and view are same class)
	 */
	public <P extends Page> void registerPage(String token, Class<P> pageClass) {
		registerPage(token, pageClass, null, null);
	}

	/**
	 * 
	 * Register a page
	 * 
	 * @param token
	 *            the page token
	 * @param pageClass
	 *            the page class (presenter and view are same class)
	 * @param pageAttributes
	 *            the {@link PageAttributes}
	 */
	public <P extends Page> void registerPage(String token, Class<P> pageClass,
			PageAttributes pageAttributes) {
		registerPage(token, pageClass, null, pageAttributes);
	}

	/**
	 * 
	 * Register a page
	 * 
	 * @param token
	 *            the page token
	 * @param pageClass
	 *            the page (presenter) class
	 * @param displayClass
	 *            the view class
	 */
	public <P extends Page> void registerPage(String token,
			Class<P> presenterClass,
			Class displayClass) {
		registerPage(token, presenterClass, displayClass, null);
	}

	/**
	 * 
	 * Register a page
	 * 
	 * @param token
	 *            the page token
	 * @param pageClass
	 *            the page class
	 * @param pageAttributes
	 *            the {@link PageAttributes}
	 */
	public <P extends Page> void registerPage(String token, Class<P> pageClass,
			Class viewClass, PageAttributes pageAttributes) {
		PageRegistry<P> register = new PageRegistry<P>(token, pageClass, viewClass,
				pageAttributes);
		registeredTokens.put(token, register);
	}

	public Class<? extends Page> getPageClass(String pageToken) {
		PageRegistry register = registeredTokens.get(pageToken);
		if (null == register)
			return null;
		else
			return register.pageClass;
	}

	/**
	 * Calls registerPages - this works this way so you can reference GWT.create in init
	 * but not in registerPages because registerPages is called during compilation
	 */
	public void init(Pages settings) {
		registerPages();
	}

	/**
	 * Register all pages in this method.  Do not use GWT.create here - override the init method
	 * to do that but make sure to call super.init
	 */
	public abstract void registerPages();

	public PageRegistry getPageRegister(String pageToken) {
		return registeredTokens.get(pageToken);
	}

	protected Page newInstance(String pageToken) throws PageNotFoundException {
		// this method will be replaced with the generator
		throw new RuntimeException(
				"You must use GWT.create() to create an instance of "
						+ getClass().getName());
	}

	public class PageRegistry<P extends Page> {
		private String pageToken;
		private Class<P> pageClass;
		private Class viewClass;
		private PageAttributes pageAttributes;

		public PageRegistry(String pageToken, Class<P> pageClass,
				Class viewClass,
				PageAttributes pageAttributes) {
			this.pageToken = pageToken;
			this.pageClass = pageClass;
			this.viewClass = viewClass;
			this.pageAttributes = pageAttributes;
		}

		public String getPageToken() {
			return pageToken;
		}

		public Class<P> getPageClass() {
			return pageClass;
		}

		public Class getDisplayClass() {
			return viewClass;
		}

		public PageAttributes getPageAttributes() {
			return pageAttributes;
		}
	}
}