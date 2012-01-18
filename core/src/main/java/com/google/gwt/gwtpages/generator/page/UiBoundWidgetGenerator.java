package com.google.gwt.gwtpages.generator.page;

import java.io.PrintWriter;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.ext.Generator;
import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.JParameterizedType;
import com.google.gwt.core.ext.typeinfo.TypeOracle;
import com.google.gwt.gwtpages.client.page.impl.UiBoundPage;
import com.google.gwt.gwtpages.generator.page.FieldBindingUtil.ViewBindings;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.rebind.ClassSourceFileComposerFactory;
import com.google.gwt.user.rebind.SourceWriter;

public class UiBoundWidgetGenerator extends Generator {

	@Override
	public String generate(TreeLogger logger, GeneratorContext context,
			String typeName) throws UnableToCompleteException {

		try {
			TypeOracle typeOracle = context.getTypeOracle();
			// get classType and save instance variables
			JClassType classType = typeOracle.getType(typeName);
			JClassType superClassType = classType.getSuperclass();
			String packageName = classType.getPackage().getName();
			JParameterizedType type = null;
			while (true) {
				type = superClassType.isParameterized();
				if (null == type || type.getTypeArgs().length == 0) {
					String qualifiedSuperClassName = superClassType + "."
							+ superClassType.getName();
					if (qualifiedSuperClassName.equals(UiBoundPage.class
							.getName())) {
						logger.log(logger.ERROR, "Class '" + typeName
								+ "' must have it's type defined");
						throw new UnableToCompleteException();
					} else {
						superClassType = superClassType.getSuperclass();
					}
				} else {
					break;
				}
			}
			String newClassName = classType.getSimpleSourceName()
					+ "_Generated";
			// Generate class source code

			generateClass(logger, context, packageName, typeName, newClassName,
					classType, type.getTypeArgs()[0], typeOracle);
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
			JClassType classType, JClassType widgetType, TypeOracle typeOracle)
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
		composer.addImport(GWT.class.getName());
		composer.addImport(widgetType.getPackage().getName() + "."
				+ widgetType.getName().replace('$', '.'));
		composer.addImport(Widget.class.getName());
		composer.addImport(UiBinder.class.getName());
		composer.addImport(UiTemplate.class.getName());
		composer.setSuperclass(typeName);
		SourceWriter sourceWriter = null;
		sourceWriter = composer.createSourceWriter(context, printWriter);
		String templateName = UIBindingUtil.findUiTemplate(classType, logger);
		String widgetClassName = widgetType.getName().replace('$', '.');
		sourceWriter.println("@UiTemplate(\"" + templateName + "\")");
		sourceWriter.println("interface _UiBinder extends UiBinder<"
				+ widgetClassName + ", " + newClassName + "> {}");
		sourceWriter
				.println("private static _UiBinder uiBinder = GWT.create(_UiBinder.class);");

		sourceWriter.println();
		sourceWriter.println("public " + newClassName + "() {");
		sourceWriter.indent();
		sourceWriter.println(widgetType.getName()
				+ " widget = uiBinder.createAndBindUi(this);");
		ViewBindings bindings = FieldBindingUtil
				.printHandlerBindings(widgetType, widgetType, sourceWriter,
						typeOracle, logger, false);
		sourceWriter.println("initWidget(widget);");
		sourceWriter.println("onConstruct(widget);");
		sourceWriter.outdent();
		sourceWriter.println("}");

		if (bindings.handledEvents.size() > 0)
			FieldBindingUtil.printHandlerBindingsInnerClass(bindings,
					widgetType, sourceWriter, typeOracle, logger, false);

		// close generated class
		sourceWriter.outdent();
		sourceWriter.println("}");

		// commit generated class
		context.commit(logger, printWriter);
	}
}