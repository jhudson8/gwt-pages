package com.google.gwt.gwtpages.client.page.impl;


/**
 * Marker class for adding deferred binding to auto-add view event handler bindings
 * in the form of {view field}${binding method}(Event).  This is a class as opposed
 * to an interface because of the use of the HandlerCache creation method.
 * 
 * @author Joe Hudson
 */
public abstract class EventBoundPresenterPage<View> extends PresenterPage<View> {
}