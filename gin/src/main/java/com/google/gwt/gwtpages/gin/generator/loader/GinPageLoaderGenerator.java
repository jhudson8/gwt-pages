package com.google.gwt.gwtpages.gin.generator.loader;

import java.io.PrintWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import test.com.google.gwt.gwtpages.client.MockPages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.ext.Generator;
import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.TypeOracle;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.gwtpages.client.PageRequestSession;
import com.google.gwt.gwtpages.client.Pages;
import com.google.gwt.gwtpages.client.message.exceptions.PageNotFoundException;
import com.google.gwt.gwtpages.client.page.ApplicationPresenter;
import com.google.gwt.gwtpages.client.page.LoadedPageContainer;
import com.google.gwt.gwtpages.client.page.Page;
import com.google.gwt.gwtpages.client.page.PageAttributes;
import com.google.gwt.gwtpages.client.page.loader.AbstractPageLoader;
import com.google.gwt.gwtpages.client.page.loader.AbstractPageLoader.PageRegistry;
import com.google.gwt.gwtpages.client.page.loader.PageLoadCallback;
import com.google.gwt.gwtpages.client.page.loader.PageLoader;
import com.google.gwt.gwtpages.client.page.parameters.PageParameters;
import com.google.gwt.gwtpages.gin.client.page.loader.GinPageLoader;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.rebind.ClassSourceFileComposerFactory;
import com.google.gwt.user.rebind.SourceWriter;

public class GinPageLoaderGenerator extends Generator {

	private static final Pages pages = new MockPages(new DummyPageLoader(),
			new DummyApplicationPresenter(), new HandlerManager(null));

	@Override
	public String generate(TreeLogger logger, GeneratorContext context,
			String typeName) throws UnableToCompleteException {

		try {
			TypeOracle typeOracle = context.getTypeOracle();
			// get classType and save instance variables
			JClassType classType = typeOracle.getType(typeName);
			String packageName = classType.getPackage().getName();

			AbstractPageLoader loader = null;
			try {
				loader = (AbstractPageLoader) Class.forName(typeName)
						.newInstance();
				loader.registerPages();
			} catch (Exception e) {
				logger.log(
						logger.ERROR,
						"The page loader could not be instantiated for deferred binding generation",
						e);
				throw new UnableToCompleteException();
			}

			Class<?> injectorClass = ((GinPageLoader) loader)
					.getInjectorClass();
			if (null == injectorClass) {
				logger.log(TreeLogger.ERROR,
						"Null injector class returned in '" + typeName + "'");
				throw new UnableToCompleteException();
			}

			Map<PageRegistry, Method> pageClassesWithInjectors = new HashMap<PageRegistry, Method>();
			Iterator<String> pageTokens = loader.getValidPageTokens();
			while (pageTokens.hasNext()) {
				PageRegistry pr = loader.getPageRegister(pageTokens.next());
				Method injectorMethod = null;
				if (null != pr) {
					for (Method m : injectorClass.getMethods()) {
						if (m.getReturnType().isAssignableFrom(
								pr.getPageClass())) {
							injectorMethod = m;
							break;
						}
					}
					if (null == injectorMethod) {
						logger.log(
								TreeLogger.WARN,
								"Could not find a matching GIN entry for page loader '"
										+ pr.getPageClass().getName()
										+ "' in '" + typeName
										+ "' using injector '"
										+ injectorClass.getName()
										+ "' - Will fallback to GWT.create()");
					}
					pageClassesWithInjectors.put(pr, injectorMethod);
				}
			}

			String newClassName = classType.getSimpleSourceName()
					+ "_Generated";
			// Generate class source code

			generateClass(logger, context, packageName, typeName, newClassName,
					injectorClass, pageClassesWithInjectors);
			return packageName + "." + newClassName;
		} catch (Exception e) {
			logger.log(TreeLogger.ERROR, "AsyncPageLoader generation Error", e);
			throw new RuntimeException("Couldn't generate async page loader", e);
		}
	}

	/**
	 * Generate source code for new class. Class extends <code>HashMap</code>.
	 * 
	 * @param logger
	 *            Logger object
	 * @param context
	 *            Generator context
	 */
	protected void generateClass(TreeLogger logger, GeneratorContext context,
			String packageName, String typeName, String newClassName,
			Class<?> injectorClass,
			Map<PageRegistry, Method> pageClassesWithInjectors)
			throws UnableToCompleteException {

		// get print writer that receives the source code
		PrintWriter printWriter = null;
		printWriter = context.tryCreate(logger, packageName, newClassName);
		// print writer if null, source code has ALREADY been generated,
		if (printWriter == null)
			return;

		// init composer, set class properties, create source writer
		ClassSourceFileComposerFactory composer = null;
		composer = new ClassSourceFileComposerFactory(packageName, newClassName);
		composer.addImport(Page.class.getName());
		composer.addImport(GWT.class.getName());
		composer.addImport(PageNotFoundException.class.getName());
		composer.setSuperclass(typeName);
		SourceWriter sourceWriter = null;
		sourceWriter = composer.createSourceWriter(context, printWriter);

		sourceWriter.println("private static final " + injectorClass.getName()
				+ " injector = GWT.create(" + injectorClass.getName()
				+ ".class);");
		sourceWriter.println();

		// generator constructor source code
		sourceWriter
				.println("protected Page newInstance(String pageToken) throws PageNotFoundException {");
		sourceWriter.indent();
		for (PageRegistry pr : pageClassesWithInjectors.keySet()) {
			emptyConstructorSanityCheck(pr, logger);
			sourceWriter.println("if (pageToken.equals(\"" + pr.getPageToken()
					+ "\")) {");
			sourceWriter.indent();
			Method m = pageClassesWithInjectors.get(pr);
			sourceWriter.println(pr.getPageClass().getName() + " page = ("
					+ pr.getPageClass().getName() + ") injector." + m.getName()
					+ "();");

			if (null != pr.getDisplayClass()) {
				boolean done = writeSetDisplayMethod(pr, sourceWriter);
				if (!done)
					done = writeSetWidgetMethod(pr, sourceWriter);
				if (!done) {
					logger.log(
							logger.ERROR,
							"Unable to determine method to set the display on the page (presenter) for '"
									+ pr.getPageClass()
									+ "' - must be setWidget(...) or setDisplay(...)");
					throw new UnableToCompleteException();
				}
			}
			sourceWriter.println("return page;");
			sourceWriter.outdent();
			sourceWriter.println("}");
		}
		sourceWriter.println("throw new PageNotFoundException(pageToken);");

		// close method
		sourceWriter.outdent();
		sourceWriter.println("}");

		// close generated class
		sourceWriter.outdent();
		sourceWriter.println("}");

		// commit generated class
		context.commit(logger, printWriter);
	}

	protected void emptyConstructorSanityCheck(PageRegistry pr,
			TreeLogger logger) throws UnableToCompleteException {
		for (Constructor c : pr.getPageClass().getDeclaredConstructors()) {
			if (c.getParameterTypes().length == 0)
				return;
		}
		logger.log(logger.ERROR, "The class '" + pr.getPageClass().getName()
				+ "' must have a no-arg constructor");
		throw new UnableToCompleteException();
	}

	protected boolean writeSetDisplayMethod(PageRegistry pr, SourceWriter sw) {
		for (Method m : pr.getPageClass().getDeclaredMethods()) {
			if (m.getName().equals("setDisplay")) {
				Class[] parameterTypes = m.getParameterTypes();
				if (null != parameterTypes && parameterTypes.length == 1) {
					// make sure the parameter type is compatible with the
					// display class
					if (parameterTypes[0]
							.isAssignableFrom(pr.getDisplayClass())) {
						writeNewDisplay(pr, sw, parameterTypes[0]);
						sw.println("page.setDisplay(display);");
						return true;
					}
				}
			}
		}
		return false;
	}

	protected boolean writeSetWidgetMethod(PageRegistry pr, SourceWriter sw) {
		for (Method m : pr.getPageClass().getDeclaredMethods()) {
			if (m.getName().equals("setWidget")) {
				Class[] parameterTypes = m.getParameterTypes();
				if (null != parameterTypes && parameterTypes.length == 1) {
					// make sure the parameter type is compatible with the
					// display class
					if (parameterTypes[0]
							.isAssignableFrom(pr.getDisplayClass())) {
						writeNewDisplay(pr, sw, parameterTypes[0]);
						sw.println("page.setWidget(display);");
						return true;
					}
				}
			}
		}
		return false;
	}

	protected void writeNewDisplay(PageRegistry pr, SourceWriter sw,
			Class defType) {
		sw.println(defType.getName().replace('$', '.') + " display = ("
				+ defType.getName().replace('$', '.') + ") GWT.create("
				+ pr.getDisplayClass().getName().replace('$', '.') + ".class);");
	}

	private static class DummyPageLoader implements PageLoader {
		public void init(Pages pages) {
		}

		public Iterator<String> getValidPageTokens() {
			return null;
		}

		public boolean isValidPageToken(String pageToken) {
			return false;
		}

		public PageAttributes getPageAttributes(String pageToken)
				throws PageNotFoundException {
			return null;
		}

		public void getPage(String pageToken, PageLoadCallback pageHandler) {
		}
	}

	private static class DummyApplicationPresenter implements
			ApplicationPresenter {
		public void showPage(LoadedPageContainer page,
				PageParameters parameters, PageRequestSession session) {
		}

		public void init(Pages settings) {
		}

		public Widget asWidget() {
			return null;
		}

		public void clearCurrentPage() {
		}
	}
}