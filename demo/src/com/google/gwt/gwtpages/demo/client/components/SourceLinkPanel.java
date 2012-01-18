package com.google.gwt.gwtpages.demo.client.components;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.VerticalPanel;

public class SourceLinkPanel extends Composite {

	interface _UiBinder extends UiBinder<VerticalPanel, SourceLinkPanel> {
	}
	private static _UiBinder uiBinder = GWT.create(_UiBinder.class);
	
	@UiField
	Anchor javaSource;
	@UiField
	Anchor uiXmlSource;

	public SourceLinkPanel (Class<?> sourceClass) {
		this(sourceClass, true);
	}

	public SourceLinkPanel (Class<?> sourceClass, boolean addUi) {
		initWidget(uiBinder.createAndBindUi(this));
		javaSource.setTarget("_blank");
		uiXmlSource.setTarget("_blank");

		String name = sourceClass.getName().substring(sourceClass.getName().lastIndexOf('.')+1, sourceClass.getName().length());
		String prefix = null;
		if (sourceClass.getName().indexOf("demo") > 0)
			prefix = "http://code.google.com/p/gwtpages/source/browse/trunk/demo/src/";
		else
			prefix = "http://code.google.com/p/gwtpages/source/browse/trunk/core/src/main/java/";
			
		javaSource.setHTML(name + ".java");
		javaSource.setHref(prefix + sourceClass.getName().replace('.', '/') + ".java");
		uiXmlSource.setHTML(name + ".ui.xml");
		uiXmlSource.setHref(prefix + sourceClass.getName().replace('.', '/') + ".ui.xml");
		if (!addUi)
			uiXmlSource.setVisible(false);
	}
}
