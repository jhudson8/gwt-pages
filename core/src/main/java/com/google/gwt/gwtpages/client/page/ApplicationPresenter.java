package com.google.gwt.gwtpages.client.page;

import com.google.gwt.gwtpages.client.PageRequestSession;
import com.google.gwt.gwtpages.client.Pages;
import com.google.gwt.gwtpages.client.applicationpresenter.CompositeApplicationPresenter;
import com.google.gwt.gwtpages.client.applicationpresenter.CompositeLayoutApplicationPresenter;
import com.google.gwt.gwtpages.client.applicationpresenter.SimplePanelApplicationPresenter;
import com.google.gwt.gwtpages.client.page.parameters.PageParameters;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

/**
 * Interface used to work with the {@link Pages} class. The function of this
 * interface is to accept a page and present it in whatever way is desired.
 * 
 * Known implementations are {@link SimplePanelApplicationPresenter},
 * {@link CompositeApplicationPresenter},
 * {@link CompositeLayoutApplicationPresenter}
 * 
 * @author Joe Hudson
 */
public interface ApplicationPresenter {

	/**
	 * Show the page
	 * 
	 * @param page
	 *            the page
	 * @param pageMetaData
	 *            the page metaData or null
	 * @param parameters
	 *            the page parameters
	 * @param session
	 *            {@link PageRequestSession}
	 */
	public void showPage(LoadedPageContainer page, PageParameters parameters,
			PageRequestSession session);

	/**
	 * Remove the current page shown in this presenter
	 */
	public void clearCurrentPage();

	/**
	 * Initialize this presenter with the pages settings
	 * 
	 * @param settings
	 *            the settings
	 */
	public void init(Pages settings);

	/**
	 * Return the widget used for this presenter
	 */
	public Widget asWidget();
}
