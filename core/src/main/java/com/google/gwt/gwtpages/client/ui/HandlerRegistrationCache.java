package com.google.gwt.gwtpages.client.ui;

import java.util.ArrayList;

import com.google.gwt.event.shared.HandlerRegistration;

public class HandlerRegistrationCache {

	private ArrayList<HandlerRegistration> registrations;

	public void add(HandlerRegistration... registration) {
		ArrayList<HandlerRegistration> l = get(true);
		for (HandlerRegistration hr : registration)
			l.add(hr);
	}

	public ArrayList<HandlerRegistration> get(boolean createIfNull) {
		if (null == registrations && createIfNull) registrations = new ArrayList<HandlerRegistration>();
		return registrations;
	}

	public void unbind() {
		if (null != registrations) {
			for (HandlerRegistration hr : registrations) {
				hr.removeHandler();
			}
			registrations = null;
		}
	}
}
