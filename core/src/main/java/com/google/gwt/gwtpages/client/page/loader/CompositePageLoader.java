package com.google.gwt.gwtpages.client.page.loader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import com.google.gwt.gwtpages.client.Pages;
import com.google.gwt.gwtpages.client.message.exceptions.PageNotFoundException;
import com.google.gwt.gwtpages.client.page.PageAttributes;

/**
 * Page loader that can support many child loaders. They will be queried in the
 * order that they are added.
 * 
 * @author Joe Hudson
 */
public class CompositePageLoader implements PageLoader {

	private ArrayList<PageLoader> pageLoaders;
	
	private HashMap<String, PageLoader> index;

	public CompositePageLoader(PageLoader... pageLoaders) {
		this.pageLoaders = new ArrayList<PageLoader>();
		for (PageLoader pageLoader : pageLoaders)
			this.pageLoaders.add(pageLoader);
	}

	public CompositePageLoader(ArrayList<PageLoader> pageLoaders) {
		this.pageLoaders = pageLoaders;
	}

	public void init(Pages settings) {
		for (PageLoader loader : pageLoaders) {
			loader.init(settings);
		}
		index = new HashMap<String, PageLoader>();
		for (PageLoader loader : pageLoaders) {
			Iterator<String> pageTokens = loader.getValidPageTokens();
			while (pageTokens.hasNext()) {
				String token = pageTokens.next();
				PageLoader _l = index.get(token);
				if (null == _l) {
					index.put(token, loader);
				}
			}
		}
	}

	public Iterator<String> getValidPageTokens() {
		return index.keySet().iterator();
	}

	public boolean isValidPageToken(String pageToken) {
		return (null != index.get(pageToken));
	}

	public PageAttributes getPageAttributes(String pageToken)
			throws PageNotFoundException {
		PageLoader loader = index.get(pageToken);
		if (null == loader) return null;
		else return loader.getPageAttributes(pageToken);
	}

	public void getPage(String pageToken, PageLoadCallback pageHandler) {
		PageLoader loader = index.get(pageToken);
		if (null == loader) pageHandler.onPageNotFound(pageToken);
		else loader.getPage(pageToken, pageHandler);
	}
}