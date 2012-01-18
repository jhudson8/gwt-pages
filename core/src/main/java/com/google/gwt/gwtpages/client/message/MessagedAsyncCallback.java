package com.google.gwt.gwtpages.client.message;

import com.google.gwt.user.client.rpc.AsyncCallback;

public abstract class MessagedAsyncCallback<T> implements AsyncCallback<T> {

	public void onFailure(Throwable caught) {
		if (null == caught.getMessage())
			Messages.get().error("An error has occured");
		else
			Messages.get().error(caught.getMessage());
	}
}
