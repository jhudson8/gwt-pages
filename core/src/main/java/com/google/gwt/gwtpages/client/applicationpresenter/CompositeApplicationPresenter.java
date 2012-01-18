package com.google.gwt.gwtpages.client.applicationpresenter;

import java.util.HashMap;

import com.google.gwt.gwtpages.client.PageRequestSession;
import com.google.gwt.gwtpages.client.Pages;
import com.google.gwt.gwtpages.client.page.ApplicationPresenter;
import com.google.gwt.gwtpages.client.page.LoadedPageContainer;
import com.google.gwt.gwtpages.client.page.PageAttributes;
import com.google.gwt.gwtpages.client.page.parameters.PageParameters;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Application presenter which can use several different application presenters
 * based on the page or session attributes. Each known presenter is mapped to a
 * String token and all available presenters are passed as entries into the
 * constructor.
 * 
 * None of the nested presenters should be layout panels as they are embedded
 * in a {@link SimplePanel}.  Consider {@link CompositeLayoutApplicationPresenter}
 * in that case.
 * 
 * To change the presenter per page, you can either
 * <ul>
 * <li>add a {@link TokenProvider} to the {@link PageAttributes} in a page loader
 * using {@link PageAttributes#put(Class, Object)} with the Class parameter
 * being {@link CompositeApplicationPresenter.TokenProvider}</li>
 * <li>add a {@link TokenProvider} to the {@link PageRequestSession} in a page request
 * event using {@link PageAttributes#put(Class, Object)} with the Class
 * parameter being {@link CompositeApplicationPresenter.TokenProvider}</li>
 * </ul>
 * 
 * If no Oracle is set, the default application presenter will be used. Note:
 * you also have the ability to directly set a page token (instead of an
 * implementation of the Oracle) by using the TOKEN_APPLICATION_PRESENTER key
 * with the value of the application presenter token String.
 * 
 * @author Joe Hudson
 */
public class CompositeApplicationPresenter
		implements ApplicationPresenter {

	private Panel panel;
	
	public static final Object PRESENTER_TOKEN_KEY = new Object();
	protected HashMap<String, ApplicationPresenter> index = new HashMap<String, ApplicationPresenter>();
	protected ApplicationPresenter currentPresenter;
	private String defaultToken;

	public CompositeApplicationPresenter() {
		panel = new SimplePanel();
	}

	public CompositeApplicationPresenter(Panel panel) {
		this.panel = panel;
	}

	public CompositeApplicationPresenter(String defaultToken) {
		this.defaultToken = defaultToken;
	}

	/**
	 * Register an application presenter with the provided token
	 * 
	 * @param token
	 *            the token
	 * @param applicationPresenter
	 *            {@link ApplicationPresenter}
	 * @return this for chaining
	 */
	public CompositeApplicationPresenter register(String token,
			ApplicationPresenter applicationPresenter) {
		index.put(token, applicationPresenter);
		return this;
	}

	public void init(Pages settings) {
		for (ApplicationPresenter presenter : index.values())
			presenter.init(settings);
	}

	public void showPage(LoadedPageContainer page, PageParameters parameters,
			PageRequestSession session) {
		TokenProvider oracle = session.get(TokenProvider.class, true);
		ApplicationPresenter presenter = null;
		String presenterToken = null;
		if (null == oracle) {
			presenterToken = (String) session.get(PRESENTER_TOKEN_KEY, true);
			if (null == presenterToken) {
				presenterToken = defaultToken;
			}
		} else {
			presenterToken = oracle
					.getPresenterToken(page, parameters, session);
		}
		if (null != presenterToken)
			presenter = index.get(presenterToken);
		if (null == presenter) {
			if (null == presenterToken)
				throw new NullPointerException(
						"Null presenter token for page '" + page.getPageToken()
								+ "'");
			else
				throw new NullPointerException(
						"No presenter located for token '" + presenterToken
								+ "'");
		}
		if (null == currentPresenter || !currentPresenter.equals(presenter)) {
			panel.clear();
			panel.add((Widget) presenter.asWidget());
			currentPresenter = presenter;
		}
		try {
			presenter.showPage(page, parameters, session);
		}
		catch (Throwable t) {
			currentPresenter.clearCurrentPage();
			currentPresenter = null;
			clearCurrentPage();
			if (t instanceof RuntimeException) throw (RuntimeException) t;
			else throw new RuntimeException(t);
		}
	}

	public void clearCurrentPage() {
		RootLayoutPanel.get().clear();
	}

	public Widget asWidget() {
		return panel;
	}

	public interface TokenProvider {
		public String getPresenterToken(LoadedPageContainer page,
				PageParameters parameters, PageRequestSession session);
	}
}
