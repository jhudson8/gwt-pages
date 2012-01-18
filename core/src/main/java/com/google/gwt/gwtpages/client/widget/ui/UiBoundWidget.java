package com.google.gwt.gwtpages.client.widget.ui;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

/**
 * {@link Composite} which has the {@link UiBinder} boilerplate code written automatically
 * using deferred binding.  Note: do not use the constructor to deal with {@link UiField}
 * bound fields - you need to use the {@link UiBoundWidget#onConstruct(Widget)}.
 * 
 * @author Joe Hudson
 */
public abstract class UiBoundWidget<WidgetToCreate extends Widget> extends Composite {

	protected abstract void onConstruct(WidgetToCreate widget);
}