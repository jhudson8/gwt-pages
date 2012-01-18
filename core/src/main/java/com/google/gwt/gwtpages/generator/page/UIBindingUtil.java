package com.google.gwt.gwtpages.generator.page;

import java.net.URL;

import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;

public class UIBindingUtil {

	public static String findUiTemplate(JClassType start, TreeLogger logger)
			throws UnableToCompleteException {
		try {
			JClassType superclass = start;
			while (null != superclass) {
				String templateName = superclass.getQualifiedSourceName().replace('.', '/') + ".ui.xml";
				URL url = Thread.currentThread().getContextClassLoader().getResource(templateName);
				if (null != url)
					return superclass.getQualifiedSourceName() + ".ui.xml";
				else
					superclass = superclass.getSuperclass();
			}
			logger.log(logger.ERROR, "Unable to determine ui template for '" + start.getQualifiedSourceName() + "'");
			throw new UnableToCompleteException();
		}
		catch (Exception e) {
			logger.log(logger.ERROR, "Unable to determine ui template for '" + start.getQualifiedSourceName() + "'", e);
			throw new UnableToCompleteException();
		}
	}
}
