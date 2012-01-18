package com.google.gwt.gwtpages.demo.client.components;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLPanel;

public class SourceReferencePanel extends Composite {

	interface _UiBinder extends UiBinder<HTMLPanel, SourceReferencePanel> {
	}
	private static _UiBinder uiBinder = GWT.create(_UiBinder.class);

	@UiField
	FlowPanel refs;
	
	public SourceReferencePanel(SourceLinkPanel... sourceLinks) {
		initWidget(uiBinder.createAndBindUi(this));
		
		for (SourceLinkPanel slp : sourceLinks)
			refs.add(slp);
	}
}
