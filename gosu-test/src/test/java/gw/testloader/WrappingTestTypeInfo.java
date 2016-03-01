/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.testloader;

import gw.lang.reflect.IType;
import gw.lang.reflect.IPropertyInfo;
import gw.lang.reflect.IMethodInfo;
import gw.lang.reflect.IConstructorInfo;
import gw.lang.reflect.ConstructorInfoBuilder;
import gw.lang.reflect.IConstructorHandler;
import gw.lang.reflect.ParameterInfoBuilder;
import gw.lang.reflect.gs.IGosuObject;

import java.util.List;
import java.util.ArrayList;

public class WrappingTestTypeInfo extends WrappingTestTypeInfoBase {

  public WrappingTestTypeInfo(WrappingTestType ownersType, IType wrappedType) {
    super(ownersType, wrappedType);
  }

  @Override
  public List<? extends IConstructorInfo> getDeclaredConstructors() {
    ArrayList<IConstructorInfo> constructors = new ArrayList<IConstructorInfo>();

    final WrappingTestTypeBase ownersType = getOwnersType();
    
    IType wrappedType = ownersType.getWrappedType();

    //Create an empty constructor if there is a default constructor for the type
    final IConstructorInfo emptyConstructor = wrappedType.getTypeInfo().getConstructor();

    if (emptyConstructor != null) {
      ConstructorInfoBuilder builder = new ConstructorInfoBuilder();
      constructors.add(builder.withConstructorHandler(new IConstructorHandler() {
        @Override
        public Object newInstance(Object... args) {
          ownersType.fireConstructorCalled(args);
          return ObjectWrapper.HELPER.createWrapper(ownersType, emptyConstructor.getConstructor().newInstance(args), ownersType.getWrappedInterfaces());
        }
      }).build(this));
    }

    // Create a constructor that takes the wrapped object
    ConstructorInfoBuilder builder = new ConstructorInfoBuilder();
    constructors.add(builder.
            withParameters(new ParameterInfoBuilder().withName("wrappedObj").withType(ownersType.getWrappedType())).
            withConstructorHandler(new IConstructorHandler() {
              @Override
              public Object newInstance(Object... args) {
                ownersType.fireConstructorCalled(args);
                return ObjectWrapper.HELPER.createWrapper(ownersType, args[0], ownersType.getWrappedInterfaces());
              }
            }).build(this));

    return constructors;
  }

  @Override
  protected void setPropValue(IPropertyInfo prop, Object ctx, Object value) {
    Object obj = null;
    if (!prop.isStatic()) {
      obj = ((ObjectWrapper) ctx).getWrappedObj();
    }
    prop.getAccessor().setValue(obj, value);
  }

  @Override
  protected Object getPropValue(IPropertyInfo prop, Object ctx) {
    Object obj = null;
    if (!prop.isStatic()) {
      obj = ((ObjectWrapper) ctx).getWrappedObj();
    }
    return prop.getAccessor().getValue(obj);
  }

  @Override
  protected Object invokeMethod(IMethodInfo methodInfo, Object ctx, Object... args) {
    Object obj = null;
    if (!methodInfo.isStatic()) {
      obj = ((ObjectWrapper) ctx).getWrappedObj();
    }
    return methodInfo.getCallHandler().handleCall(obj, args);
  }
}
