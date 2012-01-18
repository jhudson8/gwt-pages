package com.google.gwt.gwtpages.client.message.ui;

import java.util.HashMap;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;
import com.google.gwt.gwtpages.client.Pages;
import com.google.gwt.gwtpages.client.event.MessageChangeEvent;
import com.google.gwt.gwtpages.client.event.MessageChangeEvent.MessageChangeHandler;
import com.google.gwt.gwtpages.client.message.Message;
import com.google.gwt.gwtpages.client.message.Messages;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBoxBase;

/**
 * Default drop-in component used to display GWT Page messages. These messages
 * can be applied using the {@link Messages} class. Messages will automatically
 * be applied to this component as it receives {@link MessageChangeEvent}
 * events.
 * 
 * The DOM structure of the rendered output is: <div> <div class=
 * "messages {messages-success|messages-info|messages-warn|messages-error|messages-fatal}"
 * > <div class=
 * "message {message-success|message-info|message-warn|message-error|message-fatal}"
 * >message text</div> </div> </div>
 * 
 * @author Joe Hudson
 */
public class MessagePanel extends Composite implements MessageChangeHandler {

	protected SimplePanel outerContainer;
	protected FlowPanel messagesContainer;
	protected boolean listenForEvents = true;
	protected HandlerRegistration registration;
	protected HandlerManager eventBus = null;

	private static MessagePanelClientBundle bundle = GWT
			.create(MessagePanelClientBundle.class);
	static {
		bundle.css().ensureInjected();
	}

	public MessagePanel() {
		this(((MessagePanelClientBundle) (MessagePanelClientBundle) GWT
				.create(MessagePanelClientBundle.class)).css(), null);
	}

	public MessagePanel(MessagePanelCSS css, HandlerManager eventBus) {
		outerContainer = new SimplePanel();
		initWidget(outerContainer);
		outerContainer.add(this.messagesContainer = new FlowPanel());
		this.eventBus = eventBus;
		getCss().ensureInjected();
	}

	public MessagePanel listenForEvents(boolean listenForEvents) {
		this.listenForEvents = listenForEvents;
		if (!listenForEvents && null != registration) registration.removeHandler();
		else if (listenForEvents && null == registration && isAttached()) {
			registration = eventBus.addHandler(MessageChangeEvent.TYPE, this);
		}
		return this;
	}

	public void onReplaceMessages(Message[] messages, HasHandlers scopedWidget) {
		clearMessages(scopedWidget);
		addMessages(messages);
		resetStyle();
	}

	public void onAddMessages(Message[] messages) {
		addMessages(messages);
		resetStyle();
	}

	public void onClearMessages(HasHandlers scopedWidget) {
		clearMessages(scopedWidget);
		resetStyle();
	}

	protected void addMessages(Message[] messages) {
		if (null != messages) {
			for (Message message : messages)
				messagesContainer.add(new MessageEntry(message));
		}
	}

	protected void clearMessages(HasHandlers scopedWidget) {
		if (null != scopedWidget) {
			for (int i = messagesContainer.getWidgetCount() - 1; i >= 0; i--) {
				MessageEntry entry = (MessageEntry) messagesContainer
						.getWidget(i);
				if (null != entry.message.getWidget()
						&& entry.message.getWidget().equals(scopedWidget)) {
					messagesContainer.remove(i);
				}
			}
		} else {
			messagesContainer.clear();
		}
	}

	@Override
	protected void onLoad() {
		super.onLoad();
		registerHandler();
	}

	@Override
	protected void onUnload() {
		super.onUnload();
		messagesContainer.clear();
		outerContainer.setVisible(false);
		if (null != registration) {
			registration.removeHandler();
			registration = null;
		}
	}

	protected void registerHandler() {
		if (listenForEvents) {
			if (null == eventBus)
				eventBus = Pages.get().getEventBus();
			if (null == registration)
				registration = eventBus.addHandler(MessageChangeEvent.TYPE, this);
		}
	}

	protected void resetStyle() {
		if (messagesContainer.getWidgetCount() == 0) {
			outerContainer.setVisible(false);
		} else {
			messagesContainer.setStyleName(null);
			HashMap<Integer, Integer> messageLevels = new HashMap<Integer, Integer>();
			outerContainer.setVisible(true);

			for (int i = messagesContainer.getWidgetCount() - 1; i >= 0; i--) {
				MessageEntry entry = (MessageEntry) messagesContainer
						.getWidget(i);
				messageLevels.put(entry.message.getLevel(),
						entry.message.getLevel());
			}

			if (messageLevels.size() == 1) {
				int level = messageLevels.values().iterator().next();
				if (level == Message.LEVEL_SUCCESS)
					messagesContainer.setStyleName(getCss().messages() + " "
							+ getCss().successMessages());
				else if (level == Message.LEVEL_ERROR)
					messagesContainer.setStyleName(getCss().messages() + " "
							+ getCss().errorMessages());
				else if (level == Message.LEVEL_WARN)
					messagesContainer.setStyleName(getCss().messages() + " "
							+ getCss().warnMessages());
				else if (level == Message.LEVEL_INFO)
					messagesContainer.setStyleName(getCss().messages() + " "
							+ getCss().infoMessages());
				else
					messagesContainer.setStyleName(getCss().messages());
			} else {
				messagesContainer.setStyleName(getCss().messages() + " "
						+ getCss().multiMessages());
			}
			messagesContainer.getElement().scrollIntoView();
		}
	}

	public class MessageEntry extends SimplePanel implements ClickHandler {
		private Message message;

		public MessageEntry(Message message) {
			this.message = message;
			MessagePanelCSS css = getCss();
			if (null != message.getWidget()
					&& message.getWidget() instanceof Focusable) {
				Anchor anchor = new Anchor(message.getMessage());
				anchor.addClickHandler(this);
				add(anchor);
			} else {
				add(new Label(message.getMessage()));
			}
			if (message.getLevel() == Message.LEVEL_FATAL) {
				addStyleName(css.message() + " " + css.errorMessage());
			} else if (message.getLevel() == Message.LEVEL_ERROR) {
				addStyleName(css.message() + " " + css.errorMessage());
			} else if (message.getLevel() == Message.LEVEL_WARN) {
				addStyleName(css.message() + " " + css.warnMessage());
			} else if (message.getLevel() == Message.LEVEL_INFO) {
				addStyleName(css.message() + " " + css.infoMessage());
			} else if (message.getLevel() == Message.LEVEL_SUCCESS) {
				addStyleName(css.message() + " " + css.successMessage());
			} else {
				addStyleName(css.message());
			}
		}

		public void onClick(ClickEvent event) {
			((Focusable) message.getWidget()).setFocus(true);
			if (message.getWidget() instanceof TextBoxBase)
				((TextBoxBase) message.getWidget()).selectAll();
		}
	}

	protected MessagePanelCSS getCss() {
		return bundle.css();
	}
}