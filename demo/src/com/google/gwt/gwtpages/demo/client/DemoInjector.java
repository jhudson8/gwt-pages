package com.google.gwt.gwtpages.demo.client;

import com.google.gwt.gwtpages.demo.client.pages.LayoutsPage;
import com.google.gwt.gwtpages.demo.client.pages.MessagesPage;
import com.google.gwt.gwtpages.demo.client.pages.NavigationPage;
import com.google.gwt.gwtpages.demo.client.pages.RequestInfoPage;
import com.google.gwt.gwtpages.demo.client.pages.StartPage;
import com.google.gwt.gwtpages.gin.client.PagesInjector;
import com.google.gwt.inject.client.GinModules;

@GinModules(DemoModule.class)
public interface DemoInjector extends PagesInjector {

	public StartPage getStartPage();

	public MessagesPage getMessagesPage();

	public NavigationPage getNavigationPage();

	public RequestInfoPage getRequestInfoPage();

	public LayoutsPage getLayoutsPage();
}
