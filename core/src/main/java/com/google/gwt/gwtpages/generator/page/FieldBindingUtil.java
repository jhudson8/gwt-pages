package com.google.gwt.gwtpages.generator.page;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.JField;
import com.google.gwt.core.ext.typeinfo.JMethod;
import com.google.gwt.core.ext.typeinfo.JParameterizedType;
import com.google.gwt.core.ext.typeinfo.NotFoundException;
import com.google.gwt.core.ext.typeinfo.TypeOracle;
import com.google.gwt.event.dom.client.DomEvent;
import com.google.gwt.gwtpages.client.ui.HandlerRegistrationCache;
import com.google.gwt.user.rebind.SourceWriter;

public class FieldBindingUtil {

	public static ViewBindings printHandlerBindings(
			JClassType classType, JClassType widgetOrViewType,
			SourceWriter sourceWriter, TypeOracle typeOracle, TreeLogger logger, boolean isViewInterface)
			throws UnableToCompleteException {
		JClassType domEventType = null;
		try {
			domEventType = typeOracle.getType(DomEvent.class.getName());
		} catch (NotFoundException e) {
			logger.log(logger.ERROR, "Could not locate source for '"
					+ DomEvent.class.getName() + "'");
			throw new UnableToCompleteException();
		}
		Map<JClassType, List<BindingData>> knownHandledTypes = new HashMap<JClassType, List<BindingData>>();
		for (JMethod m : classType.getMethods()) {
			if (m.getParameters().length == 1
					&& m.getParameters()[0].getType().isClassOrInterface()
							.isAssignableTo(domEventType)) {
				JClassType eventType = m.getParameters()[0].getType()
						.isClassOrInterface();
				JClassType fieldType = null;
				JMethod viewMethod = null;
				String[] parts = m.getName().split("[$]");
				if (parts.length >= 2) {
					// sanity check
					for (int i = 0; i < parts.length - 1; i++) {
						if (isViewInterface) {
							JClassType superclass = widgetOrViewType;
							while (superclass != null && viewMethod == null) {
								for (JMethod method : superclass.getMethods()) {
									String name = method.getName();
									if (name.startsWith("get") && name.length() > 3) {
										name = name.substring(3, 4).toLowerCase() + name.substring(4);
									}
									if (parts[i].equals(name)) {
										viewMethod = method;
										break;
									}
								}
								superclass = superclass.getSuperclass();
							}
						}
						else {
							JField field = classType.getField(parts[i]);
							if (null != field)
								fieldType = classType.getField(parts[i]).getType()
										.isClassOrInterface();
						}
						
						if (isViewInterface) {
							if (null == viewMethod) {
								logger.log(
										logger.ERROR,
										"Could not find view method '" + parts[i]
												+ "' referenced by method '"
												+ m.getName() + "' in '"
												+ classType.getName() + "'");
								throw new UnableToCompleteException();
							}
						}
						else {
							if (null == fieldType) {
								logger.log(
										logger.ERROR,
										"Could not find field '" + parts[i]
												+ "' referenced by method '"
												+ m.getName() + "' in '"
												+ classType.getName() + "'");
								throw new UnableToCompleteException();
							}
						}
					}

					// get the handler method name
					String handlerName = null;
					JClassType handlerType = null;
					JClassType superClassType = m.getParameters()[0].getType()
							.isClassOrInterface().getSuperclass();
					while (null != superClassType && null == handlerName) {
						JParameterizedType type = superClassType
								.isParameterized();
						if (null != type && type.getTypeArgs().length == 1) {
							handlerType = type.getTypeArgs()[0]
									.isClassOrInterface();
							// we need to figure out the handler name
							if (null == handlerName) {
								JClassType _check = null;
								if (isViewInterface)
									_check = viewMethod.getReturnType().isClassOrInterface();
								else	
									_check = fieldType;
								while (_check != null && handlerName == null) {
									for (JMethod method : _check.getMethods()) {
										if (method.getParameters().length == 1
												&& method.getParameters()[0]
														.getType()
														.equals(handlerType)) {
											handlerName = method.getName();
											break;
										}
									}
									if (null != handlerName)
										break;
									_check = _check.getSuperclass();
								}
							}
							if (null != handlerName)
								break;
							else {
								logger.log(
										logger.ERROR,
										"Could not find event name for '"
												+ handlerType.getQualifiedBinaryName()
												+ "' on '" + fieldType.getQualifiedBinaryName() + "'");
								throw new UnableToCompleteException();
							}
						} else {
							superClassType = superClassType.getSuperclass();
						}
					}
					if (null == handlerType) {
						logger.log(
								logger.ERROR,
								"Could not find handler type for '"
										+ m.getName()
										+ "'");
						throw new UnableToCompleteException();
					}
					if (null == handlerName) {
						logger.log(
								logger.ERROR,
								"Could not find event name for '"
										+ handlerType.getQualifiedBinaryName()
										+ "'");
						throw new UnableToCompleteException();
					}

					for (int i = 0; i < parts.length - 1; i++) {
						List<BindingData> fields = knownHandledTypes
								.get(handlerType);
						if (null == fields) {
							fields = new ArrayList<BindingData>();
							knownHandledTypes.put(handlerType, fields);
						}
						BindingData bindingData = new BindingData();
						bindingData.eventType = eventType;
						bindingData.handlerType = handlerType;
						bindingData.fieldName = parts[i];
						bindingData.method = m;
						bindingData.viewMethod = viewMethod;
						bindingData.addHandlerMethodName = handlerName;
						fields.add(bindingData);
					}
				}
			}
		}
		ViewBindings bindings = new ViewBindings();
		bindings.handledEvents = knownHandledTypes;
		bindings.viewClassType = widgetOrViewType;
		return bindings;
	}

	public static void printHandlerBindingsInnerClass(
			ViewBindings bindings,
			JClassType classType, SourceWriter sourceWriter,
			TypeOracle typeOracle, TreeLogger logger, boolean isViewInterface)
			throws UnableToCompleteException {
		Map<JClassType, List<BindingData>> handledEvents = bindings.handledEvents;

		sourceWriter.println();
		sourceWriter.println("protected void bindHandlers() {");
		sourceWriter.indent();
		
		sourceWriter
				.println("_Handler _handler = (_Handler) getHandlerCache();");
		for (Map.Entry<JClassType, List<BindingData>> entry : handledEvents.entrySet()) {
			for (BindingData bd : entry.getValue()) {
				if (isViewInterface)
					sourceWriter.println("_handler.add(((" + bindings.viewClassType.getQualifiedSourceName() + ") getView())." + bd.viewMethod.getName() + "()." + bd.addHandlerMethodName + "(_handler));");
				else
					sourceWriter.println("_handler.add(" + bd.fieldName + "." + bd.addHandlerMethodName + "(_handler));");
			}
		}

		sourceWriter.outdent();
		sourceWriter.println("}");
		
		sourceWriter.println();
		sourceWriter.println("protected " + HandlerRegistrationCache.class.getName() + " createHandlerCache() {");
		sourceWriter.indent();
		sourceWriter.println("return new _Handler();");
		sourceWriter.outdent();
		sourceWriter.println("}");
		
		sourceWriter.println();
		sourceWriter.print("private class _Handler extends "
				+ HandlerRegistrationCache.class.getName() + " ");
		boolean started = false;
		for (JClassType handlerType : handledEvents.keySet()) {
			if (started)
				sourceWriter.print(", ");
			else {
				sourceWriter.print("implements ");
				started = true;
			}
			sourceWriter.print(handlerType.getQualifiedBinaryName());
		}
		sourceWriter.println(" {");
		sourceWriter.indent();
		sourceWriter
				.println("private List<HandlerRegistration> regs = new ArrayList<HandlerRegistration>();");

		for (Map.Entry<JClassType, List<BindingData>> entry : handledEvents
				.entrySet()) {
			sourceWriter.print("public void ");
			JClassType handler = entry.getKey();
			if (handler.getMethods().length == 1) {
				sourceWriter.print(handler.getMethods()[0].getName());
				sourceWriter.print("(");
				sourceWriter.print(handler.getMethods()[0].getParameters()[0]
						.getType().isClassOrInterface()
						.getQualifiedBinaryName());
				sourceWriter.println(" event) {");
				sourceWriter.indent();
				if (isViewInterface) {
					sourceWriter.println(bindings.viewClassType.getQualifiedSourceName() + " view = (" + bindings.viewClassType.getQualifiedSourceName() + ") getView();");
				}
				for (BindingData bindingData : entry.getValue()) {
					if (isViewInterface) {
						sourceWriter.println("if (event.getSource().equals(view."
								+ bindingData.viewMethod.getName() + "())) { "
								+ bindingData.method.getName() + "(event); }");
					}
					else {
						sourceWriter.println("if (event.getSource().equals("
								+ bindingData.fieldName + ")) { "
								+ bindingData.method.getName() + "(event); }");
					}
				}
				sourceWriter.outdent();
				sourceWriter.println("}");
			} else {
				logger.log(logger.ERROR,
						"Invalid event handler - has more than 1 method '"
								+ handler.getName() + "'");
				throw new UnableToCompleteException();
			}
		}

		sourceWriter.outdent();
		sourceWriter.println("}");
	}

	public static class BindingData {
		public String fieldName;
		public JMethod method;
		public JMethod viewMethod;
		public String addHandlerMethodName;
		public JClassType eventType;
		public JClassType handlerType;
	}

	public static class ViewBindings {
		public Map<JClassType, List<BindingData>> handledEvents;
		public JClassType viewClassType;
	}
}
