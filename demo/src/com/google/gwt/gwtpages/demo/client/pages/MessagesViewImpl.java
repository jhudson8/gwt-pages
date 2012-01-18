package com.google.gwt.gwtpages.demo.client.pages;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.gwtpages.client.widget.ui.UiBoundWidget;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;

public class MessagesViewImpl extends UiBoundWidget<FlowPanel> implements MessagesPage.View {

	@UiField
	SimplePanel sourceRef;
	@UiField
	TextBox successMessage;
	@UiField
	Button addSuccessMessage;
	@UiField
	Button clearAndAddSuccessMessage;
	@UiField
	TextBox errorMessage;
	@UiField
	Button addErrorMessage;
	@UiField
	Button clearAndAddErrorMessage;
	@UiField
	TextBox warnMessage;
	@UiField
	Button addWarnMessage;
	@UiField
	Button clearAndAddWarnMessage;
	@UiField
	TextBox infoMessage;
	@UiField
	Button addInfoMessage;
	@UiField
	Button clearAndAddInfoMessage;
	@UiField
	Button clearMessages;

	@Override
	public HasWidgets sourceReferences() {
		return sourceRef;
	}
	@Override
	public HasText successMessage() {
		return successMessage;
	}
	@Override
	public HasClickHandlers addSuccessMessage() {
		return addSuccessMessage;
	}
	@Override
	public HasClickHandlers clearAndAddSuccessMessage() {
		return clearAndAddSuccessMessage;
	}
	@Override
	public HasText errorMessage() {
		return errorMessage;
	}
	@Override
	public HasClickHandlers addErrorMessage() {
		return addErrorMessage;
	}
	@Override
	public HasClickHandlers clearAndAddErrorMessage() {
		return clearAndAddErrorMessage;
	}
	@Override
	public HasText warnMessage() {
		return warnMessage;
	}
	@Override
	public HasClickHandlers addWarnMessage() {
		return addWarnMessage;
	}
	@Override
	public HasClickHandlers clearAndAddWarnMessage() {
		return clearAndAddWarnMessage;
	}
	@Override
	public HasText infoMessage() {
		return infoMessage;
	}
	@Override
	public HasClickHandlers addInfoMessage() {
		return addInfoMessage;
	}
	@Override
	public HasClickHandlers clearAndAddInfoMessage() {
		return clearAndAddInfoMessage;
	}
	@Override
	public HasClickHandlers clearMessages() {
		return clearMessages;
	}
	@Override
	protected void onConstruct(FlowPanel widget) {
	}
}