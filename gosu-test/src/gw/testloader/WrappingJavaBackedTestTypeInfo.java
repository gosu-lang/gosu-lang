/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.testloader;

import gw.lang.reflect.IType;
import gw.lang.reflect.IConstructorInfo;
import gw.lang.reflect.ConstructorInfoBuilder;
import gw.lang.reflect.IConstructorHandler;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.ITypeInfo;
import gw.lang.reflect.IPropertyInfo;
import gw.lang.reflect.IMethodInfo;
import gw.lang.reflect.gs.IGosuObject;

import java.util.List;
import java.util.ArrayList;

public class WrappingJavaBackedTestTypeInfo extends WrappingTestTypeInfoBase {

  public WrappingJavaBackedTestTypeInfo(WrappingJavaBackedTestType ownersType, IType wrappedType) {
    super(ownersType, wrappedType);
  }

  @Override
  public List<? extends IConstructorInfo> getDeclaredConstructors() {
    ArrayList<IConstructorInfo> constructors = new ArrayList<IConstructorInfo>();

    final WrappingJavaBackedTestType ownersType = (WrappingJavaBackedTestType) getOwnersType();

    IType wrappedType = ownersType.getWrappedType();

    ConstructorInfoBuilder builder = new ConstructorInfoBuilder();
    constructors.add(builder.withConstructorHandler(new IConstructorHandler() {
      @Override
      public Object newInstance(Object... args) {
        ownersType.fireConstructorCalled(args);
        ITypeInfo typeInfo = TypeSystem.get(ownersType.getImplClass()).getTypeInfo();
        Object obj = typeInfo.getConstructor().getConstructor().newInstance();
        IType iType = TypeSystem.getFromObject(obj);
        IMethodInfo setType = iType.getTypeInfo().getMethod("setType", TypeSystem.get(IType.class));
        if (setType != null) {
          setType.getCallHandler().handleCall(obj, this);
        }
        return obj;
      }
    }).build(this));

    return constructors;
  }

  @Override
  protected void setPropValue(IPropertyInfo prop, Object ctx, Object value) {
    prop.getAccessor().setValue(ctx, value);
  }

  @Override
  protected Object getPropValue(IPropertyInfo propertyInfo, Object ctx) {
    return propertyInfo.getAccessor().getValue(ctx);
  }

  @Override
  protected Object invokeMethod(IMethodInfo iMethodInfo, Object ctx, Object... args) {
    return iMethodInfo.getCallHandler().handleCall(ctx, args);
  }

}