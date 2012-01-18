package com.google.gwt.gwtpages.demo.client.pages;

import java.io.Serializable;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.gwtpages.client.PageRequestSession;
import com.google.gwt.gwtpages.client.page.AsyncPageCallback;
import com.google.gwt.gwtpages.client.page.impl.UiBoundPage;
import com.google.gwt.gwtpages.client.page.parameters.PageParameters;
import com.google.gwt.gwtpages.demo.client.components.SourceLinkPanel;
import com.google.gwt.gwtpages.demo.client.components.SourceReferencePanel;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;

public class RequestInfoPage extends UiBoundPage<FlowPanel> implements ClickHandler {

	@UiField
	Anchor gotoPage;
	@UiField
	Label historyToken;
	@UiField
	Label pageToken;
	@UiField
	Label ordinalParameters;
	@UiField
	Label mappedParameters;
	@UiField
	Label transientParameters;
	@UiField
	SimplePanel sourceRef;
	
	private Command backCommand;

	@Override
	public void onConstruct(FlowPanel widget) {
		gotoPage.addClickHandler(this);
		sourceRef.add(new SourceReferencePanel(new SourceLinkPanel(RequestInfoPage.class)));
	}

	@Override
	public void onEnterPage(PageParameters parameters,
			PageRequestSession session, AsyncPageCallback callback) {
		historyToken.setText(parameters.getHistoryToken());
		pageToken.setText(parameters.getPageToken());
		if (null != parameters.getParameters())
			ordinalParameters.setText(serialize(parameters.getParameters()));
		else
			ordinalParameters.setText("N/A");
		if (null != parameters.getParameterMap())
			mappedParameters.setText(parameters.getParameterMap().toString());
		else
			mappedParameters.setText("N/A");
		transientParameters.setText(session.getData().toString());
		
		backCommand = (Command) session.get(PageConstants.PARAM_BACK_COMMAND);
		if (null != backCommand)
			gotoPage.setText("Back to " + session.get(PageConstants.PARAM_BACK_TITLE));
		else
			gotoPage.setText("Back to previous page");
	}

	private void gotoPage() {
		if (null == backCommand) History.back();
		else backCommand.execute();
	}

	@Override
	public void onClick(ClickEvent event) {
		if (event.getSource().equals(gotoPage)) {
			gotoPage();
		}
	}

	private String serialize(Serializable[] list) {
		StringBuilder sb = new StringBuilder();
		for (Serializable s : list) {
			if (sb.length() > 0) sb.append(", ");
			sb.append(s.toString());
		}
		if (sb.length() == 0) return "N/A";
		else return sb.toString();
	}
}
