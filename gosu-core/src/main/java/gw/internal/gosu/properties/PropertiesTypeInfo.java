/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.properties;

import gw.lang.reflect.BaseTypeInfo;
import gw.lang.reflect.IMethodCallHandler;
import gw.lang.reflect.IMethodInfo;
import gw.lang.reflect.IPropertyInfo;
import gw.lang.reflect.IType;
import gw.lang.reflect.MethodInfoBuilder;
import gw.lang.reflect.MethodList;
import gw.lang.reflect.ParameterInfoBuilder;
import gw.lang.reflect.java.JavaTypes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Type info based on a {@link PropertyNode}
 */
public class PropertiesTypeInfo extends BaseTypeInfo {

  private final Map<CharSequence,IPropertyInfo> _properties = new HashMap<CharSequence, IPropertyInfo>();
  private final MethodList _methods;
  
  public PropertiesTypeInfo(PropertiesType type) {
    super(type);
    PropertyNode node = type.getPropertyNode();
    for (PropertyNode childNode : node.getChildren()) {
      PropertiesPropertyInfo info = new PropertiesPropertyInfo(this, childNode, node.isRoot());
      _properties.put(info.getName(), info);
    }
    _methods = createMethodInfos(node);
  }

  @Override
  public MethodList getMethods() {
    return _methods;
  }

  @Override
  public IMethodInfo getCallableMethod(CharSequence strMethod, IType... params) {
    return FIND.callableMethod(getMethods(), strMethod, params);
  }

  @Override
  public IMethodInfo getMethod(CharSequence methodName, IType... params) {
    return FIND.method(getMethods(), methodName, params);
  }

  @Override
  public List<? extends IPropertyInfo> getProperties() {
    return new ArrayList<IPropertyInfo>(_properties.values());
  }

  @Override
  public IPropertyInfo getProperty(CharSequence propName) {
    return _properties.get(propName);
  }

  @Override
  public boolean isStatic() {
    return true;
  }

  private MethodList createMethodInfos(PropertyNode node) {
    MethodList result = new MethodList();
    if (!node.isLeaf() || node.isRoot()) {
      result.add(createGetValueByNameMethod(node));
    }
    if (!node.isLeaf() && node.hasValue()) {
      result.add(createGetValueMethod(node));
    }
    return result;
  }

  private IMethodInfo createGetValueByNameMethod(final PropertyNode node) {
    return new MethodInfoBuilder()
            .withName("getValueByName")
            .withDescription("Get the value of the named sub property")
            .withParameters(new ParameterInfoBuilder()
                    .withName("propertyName")
                    .withType(JavaTypes.STRING())
                    .withDescription("Name of the child property"))
            .withReturnType(JavaTypes.STRING())
            .withStatic(node.isRoot())
            .withCallHandler(new IMethodCallHandler() {
              @Override
              public Object handleCall(Object ctx, Object... args) {
                return node.getChildValue((String) args[0]);
              }
            })
            .build(this);
  }

  private IMethodInfo createGetValueMethod(final PropertyNode node) {
    return new MethodInfoBuilder()
            .withName("getValue")
            .withDescription("Get the value of this property")
            .withParameters()
            .withReturnType(JavaTypes.STRING())
            .withStatic(node.isRoot())
            .withCallHandler(new IMethodCallHandler() {
              @Override
              public Object handleCall(Object ctx, Object... args) {
                return node.getValue();
              }
            })
            .build(this);
  }

}
