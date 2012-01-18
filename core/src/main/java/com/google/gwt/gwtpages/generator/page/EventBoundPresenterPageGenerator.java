package com.google.gwt.gwtpages.generator.page;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.ext.Generator;
import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.JParameterizedType;
import com.google.gwt.core.ext.typeinfo.TypeOracle;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.gwtpages.client.Pages;
import com.google.gwt.gwtpages.generator.page.FieldBindingUtil.ViewBindings;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.rebind.ClassSourceFileComposerFactory;
import com.google.gwt.user.rebind.SourceWriter;

public class EventBoundPresenterPageGenerator extends Generator {

	@Override
	public String generate(TreeLogger logger, GeneratorContext context,
			String typeName) throws UnableToCompleteException {

		try {
			TypeOracle typeOracle = context.getTypeOracle();
			// get classType and save instance variables
			JClassType classType = typeOracle.getType(typeName);
			String packageName = classType.getPackage().getName();
			JClassType viewClass = null;

			// see if the view class is referenced as the class type
			JClassType superClassType = classType;
			while (superClassType != null) {
				JParameterizedType type = classType.isParameterized();
				if (null == type || type.getTypeArgs().length == 0) {
					superClassType = superClassType.getSuperclass();
				} else {
					viewClass = type.getTypeArgs()[0].isClassOrInterface();
				}
			}
			// look for an inner class with the name of View
			superClassType = classType;
			while (superClassType != null) {
				for (JClassType type : superClassType.getNestedTypes()) {
					JClassType _interface = type.isInterface();
					if (null != _interface
							&& _interface.getName().endsWith("View")) {
						viewClass = _interface;
						break;
					} else if (null != _interface
							&& _interface.getName().endsWith("Display")) {
						viewClass = _interface;
						break;
					}
				}
				if (null != viewClass)
					break;
			}

			String newClassName = classType.getSimpleSourceName()
					+ "_Generated";
			// Generate class source code

			generateClass(logger, context, packageName, typeName, newClassName,
					classType, viewClass, typeOracle);
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
			JClassType classType, JClassType viewInterfaceType,
			TypeOracle typeOracle) throws UnableToCompleteException {

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
		composer.addImport(Pages.class.getName());
		composer.addImport(List.class.getName());
		composer.addImport(ArrayList.class.getName());
		composer.addImport(HandlerRegistration.class.getName());
		composer.addImport(Widget.class.getName());
		composer.addImport(UiBinder.class.getName());
		composer.addImport(UiTemplate.class.getName());
		composer.setSuperclass(typeName);
		SourceWriter sourceWriter = null;
		sourceWriter = composer.createSourceWriter(context, printWriter);

		sourceWriter.println();
		sourceWriter.println("public void init(Pages settings) {");
		sourceWriter.indent();
		ViewBindings bindings = FieldBindingUtil.printHandlerBindings(
				classType, viewInterfaceType, sourceWriter, typeOracle, logger,
				true);
		sourceWriter.println("bindHandlers();");
		sourceWriter.println("onConstruct(("
				+ viewInterfaceType.getQualifiedSourceName() + ") getView());");
		sourceWriter.println("super.init(settings);");
		sourceWriter.outdent();
		sourceWriter.println("}");
		sourceWriter.println();
		sourceWriter.println("public Widget asWidget() {");
		sourceWriter.indent();
		sourceWriter.println("return (Widget) getView();");
		sourceWriter.outdent();
		sourceWriter.println("}");
		if (bindings.handledEvents.size() > 0)
			FieldBindingUtil.printHandlerBindingsInnerClass(bindings,
					classType, sourceWriter, typeOracle, logger, true);

		// close generated class
		sourceWriter.outdent();
		sourceWriter.println("}");

		// commit generated class
		context.commit(logger, printWriter);
	}
}