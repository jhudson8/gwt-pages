package com.google.gwt.gwtpages.generator.loader;

import java.io.PrintWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.ext.Generator;
import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.TypeOracle;
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
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.rebind.ClassSourceFileComposerFactory;
import com.google.gwt.user.rebind.SourceWriter;

public class PageLoaderGenerator extends Generator {

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
			List<PageRegistry> registeredPages = new ArrayList<PageRegistry>();
			Iterator<String> pageTokens = loader.getValidPageTokens();
			while (pageTokens.hasNext()) {
				PageRegistry pageRegister = loader.getPageRegister(pageTokens
						.next());
				registeredPages.add(pageRegister);
			}

			String newClassName = classType.getSimpleSourceName()
					+ "_Generated";
			// Generate class source code

			generateClass(logger, context, packageName, typeName, newClassName,
					registeredPages);
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
			List<PageRegistry> registeredPages)
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

		// generator constructor source code
		sourceWriter
				.println("protected Page newInstance(String pageToken) throws PageNotFoundException {");
		sourceWriter.indent();
		for (PageRegistry pr : registeredPages) {
			sourceWriter.println("if (pageToken.equals(\"" + pr.getPageToken()
					+ "\")) {");
			sourceWriter.indent();
			if (null != pr.getDisplayClass()) {
				boolean done = writeSetDisplayMethod(pr, sourceWriter, logger);
				if (!done)
					done = writeSetViewMethod(pr, sourceWriter, logger);
				if (!done)
					done = writeConstructorMethod(pr, sourceWriter);
				if (!done) {
					logger.log(
							logger.ERROR,
							"Unable to determine method to set the display on the page (presenter) for '"
									+ pr.getPageClass()
									+ "' - must be setView(...), setDisplay(...) or single argument constructor");
					throw new UnableToCompleteException();
				}
			} else {
				sourceWriter.println("return (" + pr.getPageClass().getName()
						+ ") GWT.create(" + pr.getPageClass().getName()
						+ ".class);");
			}
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

	protected boolean writeConstructorMethod(PageRegistry pr, SourceWriter sw) {
		for (Constructor c : pr.getPageClass().getDeclaredConstructors()) {
			Class[] parameterTypes = c.getParameterTypes();
			if (null != parameterTypes && parameterTypes.length == 1) {
				// make sure the parameter type is compatible with the display
				// class
				if (parameterTypes[0].isAssignableFrom(pr.getDisplayClass())) {
					sw.println(pr.getDisplayClass().getName() + " display = ("
							+ pr.getDisplayClass().getName() + ") GWT.create("
							+ pr.getDisplayClass().getName() + ".class);");
					sw.println("Page page = new " + pr.getPageClass().getName()
							+ "(display);");
					sw.println("return page;");
					return true;
				}
			}
		}
		return false;
	}

	protected void emptyConstructorSanityCheck(PageRegistry pr, TreeLogger logger) throws UnableToCompleteException {
		for (Constructor c : pr.getPageClass().getDeclaredConstructors()) {
			if (c.getParameterTypes().length == 0) return;
		}
		logger.log(logger.ERROR, "The class '" + pr.getPageClass().getName() + "' must have a no-arg constructor");
		throw new UnableToCompleteException();
	}

	protected boolean writeSetDisplayMethod(PageRegistry pr, SourceWriter sw, TreeLogger logger) throws UnableToCompleteException {
		for (Method m : pr.getPageClass().getMethods()) {
			if (m.getName().equals("setDisplay")) {
				Class[] parameterTypes = m.getParameterTypes();
				if (null != parameterTypes && parameterTypes.length == 1) {
					// make sure the parameter type is compatible with the
					// display class
					if (parameterTypes[0]
							.isAssignableFrom(pr.getDisplayClass())) {
						writeNewPageAndDisplay(pr, sw, parameterTypes[0]);
						sw.println("page.setDisplay(display);");
						sw.println("return page;");
						emptyConstructorSanityCheck(pr, logger);
						return true;
					}
				}
			}
		}
		return false;
	}

	protected boolean writeSetViewMethod(PageRegistry pr, SourceWriter sw, TreeLogger logger) throws UnableToCompleteException {
		for (Method m : pr.getPageClass().getMethods()) {
			if (m.getName().equals("setView")) {
				Class[] parameterTypes = m.getParameterTypes();
				if (null != parameterTypes && parameterTypes.length == 1) {
					// make sure the parameter type is compatible with the
					// display class
					if (parameterTypes[0]
							.isAssignableFrom(pr.getDisplayClass())) {
						writeNewPageAndDisplay(pr, sw, parameterTypes[0]);
						sw.println("page.setView(display);");
						sw.println("return page;");
						return true;
					}
				}
			}
		}
		return false;
	}

	protected void writeNewPageAndDisplay(PageRegistry pr, SourceWriter sw, Class defType) {
		sw.println(pr.getPageClass().getName() + " page = ("
				+ pr.getPageClass().getName() + ") GWT.create("
				+ pr.getPageClass().getName() + ".class);");
		sw.println(defType.getName().replace('$','.') + " display = ("
				+ defType.getName().replace('$','.') + ") GWT.create("
				+ pr.getDisplayClass().getName() + ".class);");
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