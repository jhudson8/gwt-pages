package com.google.gwt.gwtpages.demo.client.pages;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HasHandlers;
import com.google.gwt.gwtpages.client.PageRequestSession;
import com.google.gwt.gwtpages.client.message.Message;
import com.google.gwt.gwtpages.client.message.Messages;
import com.google.gwt.gwtpages.client.page.AsyncPageCallback;
import com.google.gwt.gwtpages.client.page.impl.EventBoundPresenterPage;
import com.google.gwt.gwtpages.client.page.parameters.PageParameters;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.HasWidgets;

public class MessagesPage extends EventBoundPresenterPage {

	public interface View {
		HasWidgets sourceReferences();

		HasText successMessage();

		HasClickHandlers addSuccessMessage();

		HasClickHandlers clearAndAddSuccessMessage();

		HasText errorMessage();

		HasClickHandlers addErrorMessage();

		HasClickHandlers clearAndAddErrorMessage();

		HasText warnMessage();

		HasClickHandlers addWarnMessage();

		HasClickHandlers clearAndAddWarnMessage();

		HasText infoMessage();

		HasClickHandlers addInfoMessage();

		HasClickHandlers clearAndAddInfoMessage();

		HasClickHandlers clearMessages();
	}

	public MessagesPage() {
	}

	public void addSuccessMessage$onClick(ClickEvent event) {
		Messages.get().addMessage(
				Message.success(view().successMessage().getText()));
		resetMessages();
	}

	public void clearAndAddSuccessMessage$onClick(ClickEvent event) {
		Messages.get().success(view().successMessage().getText());
		resetMessages();
	}

	public void addErrorMessage$onClick(ClickEvent event) {
		Messages.get().addMessage(
				Message.error(view().errorMessage().getText(),
						(HasHandlers) view().errorMessage()));
		resetMessages();
	}

	public void clearAndAddErrorMessage$onClick(ClickEvent event) {
		Messages.get().error(view().errorMessage().getText(),
				(HasHandlers) view().errorMessage());
		resetMessages();
	}

	public void addWarnMessage$onClick(ClickEvent event) {
		Messages.get().addMessage(Message.warn(view().warnMessage().getText()));
		resetMessages();
	}

	public void clearAndAddWarnMessage$onClick(ClickEvent event) {
		Messages.get().warn(view().warnMessage().getText());
		resetMessages();
	}

	public void addInfoMessage$onClick(ClickEvent event) {
		Messages.get().addMessage(Message.info(view().infoMessage().getText()));
		resetMessages();
	}

	public void clearAndAddInfoMessage$onClick(ClickEvent event) {
		Messages.get().info(view().infoMessage().getText());
		resetMessages();
	}

	public void clearMessages$onClick(ClickEvent event) {
		Messages.get().clear();
	}

	@Override
	protected void onConstruct(Object view) {
	}

	@Override
	public void onEnterPage(PageParameters parameters,
			PageRequestSession pageRequestData, AsyncPageCallback callback) {
		resetMessages();
	}

	private void resetMessages() {
		view().successMessage().setText("Enter a message");
		view().errorMessage().setText("Enter a message");
		view().warnMessage().setText("Enter a message");
		view().infoMessage().setText("Enter a message");
	}

	private View view() {
		return (View) getRawView();
	}
}