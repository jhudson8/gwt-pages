package com.google.gwt.gwtpages.demo.client.pages;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.gwtpages.client.GotoPageCommand;
import com.google.gwt.gwtpages.client.PageRequestSession;
import com.google.gwt.gwtpages.client.Pages;
import com.google.gwt.gwtpages.client.applicationpresenter.CompositeLayoutApplicationPresenter;
import com.google.gwt.gwtpages.client.page.AsyncPageCallback;
import com.google.gwt.gwtpages.client.page.LoadedPageContainer;
import com.google.gwt.gwtpages.client.page.impl.UiBoundPage;
import com.google.gwt.gwtpages.client.page.parameters.PageParameters;
import com.google.gwt.gwtpages.demo.client.GWT_Pages_Demo;
import com.google.gwt.gwtpages.demo.client.components.SourceLinkPanel;
import com.google.gwt.gwtpages.demo.client.components.SourceReferencePanel;
import com.google.gwt.gwtpages.demo.client.pages.layout.DefaultLayoutPage;
import com.google.gwt.gwtpages.demo.client.pages.layout.NoLayoutPage;
import com.google.gwt.gwtpages.demo.client.pages.layout.RightNavLayoutPage;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.inject.Inject;

public class LayoutsPage extends UiBoundPage<FlowPanel> implements ClickHandler {

	@Inject
	Pages pages;
	
	@UiField
	SimplePanel sourceRef;
	@UiField
	Anchor standardLayout;
	@UiField
	Anchor noLayout;
	
	@Override
	public void onConstruct(FlowPanel widget) {
		sourceRef.add(new SourceReferencePanel(
				new SourceLinkPanel(LayoutsPage.class),
				new SourceLinkPanel(GWT_Pages_Demo.class, false),
				new SourceLinkPanel(CompositeLayoutApplicationPresenter.class, false),
				new SourceLinkPanel(DefaultLayoutPage.class),
				new SourceLinkPanel(NoLayoutPage.class),
				new SourceLinkPanel(RightNavLayoutPage.class)
			));
			standardLayout.addClickHandler(this);
			noLayout.addClickHandler(this);
	}

	/**
	 * The application presenters are defined in the composite application presenter {@link GWT_Pages_Demo} 
	 */
	private void showStandardLayout() {
		GotoPageCommand cmd = pages.gotoPage(PageConstants.PAGE_LAYOUTS);
		cmd.getSession().put(CompositeLayoutApplicationPresenter.PRESENTER_TOKEN_KEY, PageConstants.LAYOUT_DEFAULT);
		cmd.execute();
	}

	/**
	 * The application presenters are defined in the composite application presenter {@link GWT_Pages_Demo} 
	 */
	private void showNoLayout() {
		// you can also use an Oracle to determine the layout.  You really wouldn't have the need to use
		// it here but could use it for your page loader if you didn't want to register a static presenter
		// token - We'll do it here as an example
		GotoPageCommand cmd = pages.gotoPage(PageConstants.PAGE_LAYOUTS);
		cmd.getSession().put(CompositeLayoutApplicationPresenter.TokenProvider.class, new CompositeLayoutApplicationPresenter.TokenProvider() {
			
			@Override
			public String getPresenterToken(LoadedPageContainer page,
					PageParameters parameters, PageRequestSession session) {
				return PageConstants.LAYOUT_NONE;
			}
		});
		cmd.execute();
	}

	@Override
	public void onEnterPage(PageParameters parameters,
			PageRequestSession pageRequestData, AsyncPageCallback callback) {
	}

	@Override
	public void onExitPage() {
	}

	@Override
	public void onClick(ClickEvent event) {
		if (event.getSource().equals(standardLayout)) {
			showStandardLayout();
		}
		else if (event.getSource().equals(noLayout)) {
			showNoLayout();
		}
	}
}
