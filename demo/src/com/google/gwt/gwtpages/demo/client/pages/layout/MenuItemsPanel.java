package com.google.gwt.gwtpages.demo.client.pages.layout;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.gwtpages.client.GotoPageCommand;
import com.google.gwt.gwtpages.client.Pages;
import com.google.gwt.gwtpages.client.event.PageShownEvent;
import com.google.gwt.gwtpages.client.event.PageShownEvent.PageShownHandler;
import com.google.gwt.gwtpages.client.page.LoadedPageContainer;
import com.google.gwt.gwtpages.client.page.loader.PageLoader;
import com.google.gwt.gwtpages.client.page.parameters.PageParameters;
import com.google.gwt.gwtpages.demo.client.pages.PageConstants;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.SimplePanel;

public class MenuItemsPanel extends FlowPanel implements PageShownHandler {

	private List<MenuItemLink> links;
	private boolean leftSide;
	private boolean initialized = false;
	
	public MenuItemsPanel (boolean isLeftSide) {
		this.leftSide = isLeftSide;

		links = new ArrayList<MenuItemsPanel.MenuItemLink>();
		links.add(new MenuItemLink(PageLoader.PAGE_DEFAULT, "Home"));
		links.add(new MenuItemLink(PageConstants.PAGE_NAVIGATION, "Navigation"));
		links.add(new MenuItemLink(PageConstants.PAGE_MESSAGES, "Messages"));
		links.add(new MenuItemLink(PageConstants.PAGE_LAYOUTS, "Layouts"));
		links.add(new MenuItemLink(PageConstants.PAGE_LOADERS, "Page Loaders"));
		links.add(new MenuItemLink(PageConstants.PAGE_EVENTS, "Page Events"));
		links.add(new MenuItemLink(PageConstants.PAGE_SECURITY, "Security"));
		links.add(new MenuItemLink(PageConstants.PAGE_MVP, "MVP"));

		for (MenuItemLink link : links)
			add(link);
	}

	@Override
	protected void onAttach() {
		super.onAttach();
		if (!initialized) {
			Pages.get().getEventBus().addHandler(PageShownEvent.TYPE, this);
			initialized = true;
		}
	}

	@Override
	public void onPageShown(LoadedPageContainer currentPageData,
			PageParameters parameters, GotoPageCommand command,
			LoadedPageContainer previousPageData) {
		boolean matchFound = false;
		for (MenuItemLink link : links) {
			if (link.pageToken.equals(currentPageData.getPageToken())) {
				matchFound = true;
				break;
			}
		}
		if (matchFound) {
			for (MenuItemLink link : links) {
				if (link.pageToken.equals(currentPageData.getPageToken())) {
					link.select();
				}
				else {
					link.reset();
				}
			}
		}
	}

	private class MenuItemLink extends SimplePanel implements MouseOverHandler, MouseOutHandler {
		private String pageToken;
		private Hyperlink link;
		
		public MenuItemLink(String pageToken, String label) {
			this.pageToken = pageToken;
			link = new Hyperlink(label, pageToken);
			addDomHandler(this, MouseOverEvent.getType());
			addDomHandler(this, MouseOutEvent.getType()); 
			if (leftSide) {
				addStyleName("menu-item left-side-menu-item");
			}
			else {
				addStyleName("menu-item right-side-menu-item");
			}
			add(link);
		}

		public void select() {
			if (leftSide) {
				addStyleName("menu-item-selected left-side-menu-item-selected");
			}
			else {
				addStyleName("menu-item-selected right-side-menu-item-selected");
			}
		}

		public void reset() {
			removeStyleName("menu-item-selected");
			if (leftSide) {
				removeStyleName("left-side-menu-item-selected");
				removeStyleName("left-side-menu-item-hover");
			}
			else {
				removeStyleName("right-side-menu-item-selected");
				removeStyleName("right-side-menu-item-hover");
			}
		}

		@Override
		public void onMouseOver(MouseOverEvent event) {
			if (leftSide) {
				addStyleName("menu-item-hover left-side-menu-item-hover");
			}
			else {
				addStyleName("menu-item-hover right-side-menu-item-hover");
			}
		}

		@Override
		public void onMouseOut(MouseOutEvent event) {
			removeStyleName("menu-item-hover");
			if (leftSide) {
				removeStyleName("left-side-menu-item-hover");
			}
			else {
				addStyleName("right-side-menu-item-hover");
			}
		}
	}
}
