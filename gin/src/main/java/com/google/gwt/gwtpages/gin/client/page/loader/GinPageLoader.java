package com.google.gwt.gwtpages.gin.client.page.loader;

import com.google.gwt.gwtpages.client.page.loader.PageLoader;
import com.google.gwt.inject.client.Ginjector;

public interface GinPageLoader extends PageLoader {

	/**
	 * Return the class that will be used as the injector class
	 */
	public abstract Class<? extends Ginjector> getInjectorClass();
}
