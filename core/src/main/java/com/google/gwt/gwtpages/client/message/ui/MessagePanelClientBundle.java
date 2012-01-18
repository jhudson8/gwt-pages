package com.google.gwt.gwtpages.client.message.ui;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.DataResource;

public interface MessagePanelClientBundle extends ClientBundle {

	@Source("message-panel.css")
	MessagePanelCSS css();

	@Source("error32.png")
	DataResource error32();

	@Source("info32.png")
	DataResource info32();

	@Source("success32.png")
	DataResource success32();

	@Source("warn32.png")
	DataResource warn32();

	@Source("error16.png")
	DataResource error16();

	@Source("info16.png")
	DataResource info16();

	@Source("success16.png")
	DataResource success16();

	@Source("warn16.png")
	DataResource warn16();
}