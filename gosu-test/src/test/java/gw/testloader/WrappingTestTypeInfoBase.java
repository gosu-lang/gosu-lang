/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.testloader;

import gw.lang.reflect.FeatureManager;
import gw.lang.reflect.IType;
import gw.lang.reflect.IPropertyInfo;
import gw.lang.reflect.ITypeInfo;
import gw.lang.reflect.IRelativeTypeInfo;
import gw.lang.reflect.IMethodInfo;
import gw.lang.reflect.IConstructorInfo;
import gw.lang.reflect.IAttributedFeatureInfo;
import gw.lang.reflect.IAnnotationInfo;
import gw.lang.reflect.IPropertyAccessor;
import gw.lang.reflect.IPresentationInfo;
import gw.lang.reflect.IScriptabilityModifier;
import gw.lang.reflect.IFeatureInfo;
import gw.lang.reflect.IParameterInfo;
import gw.lang.reflect.IMethodCallHandler;
import gw.lang.reflect.IExceptionInfo;
import gw.lang.reflect.MethodList;
import gw.lang.reflect.TypeInfoBase;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public abstract class WrappingTestTypeInfoBase extends TypeInfoBase implements IRelativeTypeInfo {

  FeatureManager _fm;
  private WrappingTestTypeBase _ownersType;

  public WrappingTestTypeInfoBase(WrappingTestTypeBase ownersType, IType wrappedType) {
    _fm = new FeatureManager(this, false);
    _ownersType = ownersType;
  }

  @Override
  public List<? extends IPropertyInfo> getDeclaredProperties() {
    ArrayList<IPropertyInfo> props = new ArrayList<IPropertyInfo>();
    for (IPropertyInfo propertyInfo : getPropsFromWrappedClass()) {
      props.add(new WrappingPropertyInfo(propertyInfo));
    }
    return props;
  }

  private List<? extends IPropertyInfo> getPropsFromWrappedClass() {
    IType wrappedType = _ownersType.getWrappedType();
    ITypeInfo typeInfo = wrappedType.getTypeInfo();
    if (typeInfo instanceof IRelativeTypeInfo) {
      return ((IRelativeTypeInfo) typeInfo).getProperties(wrappedType);
    } else {
      return typeInfo.getProperties();
    }
  }

  @Override
  public List<? extends IMethodInfo> getDeclaredMethods() {
    ArrayList<IMethodInfo> methods = new ArrayList<IMethodInfo>();
    for (IMethodInfo methodInfo : getMethodsFromWrappedClass()) {
      methods.add(new WrappingMethodInfo(methodInfo));
    }
    return methods;
  }

  private List<? extends IMethodInfo> getMethodsFromWrappedClass() {
    IType wrappedType = _ownersType.getWrappedType();
    ITypeInfo typeInfo = wrappedType.getTypeInfo();
    if (typeInfo instanceof IRelativeTypeInfo) {
      return ((IRelativeTypeInfo) typeInfo).getMethods(wrappedType);
    } else {
      return typeInfo.getMethods();
    }
  }

  protected abstract void setPropValue(IPropertyInfo prop, Object ctx, Object value);

  protected abstract Object getPropValue(IPropertyInfo propertyInfo, Object ctx);

  protected abstract Object invokeMethod(IMethodInfo iMethodInfo, Object ctx, Object... args);

  // ==========================================================================
  // Delegating methods
  // ==========================================================================

  public void clear() {
    _fm.clear();
  }

  public Accessibility getAccessibilityForClass(IType ownersClass, IType whosAskin) {
    return _fm.getAccessibilityForClass(ownersClass, whosAskin);
  }

  public boolean isInSameNamespace(IType ownersClass, IType whosAskin) {
    return _fm.isInSameNamespace(ownersClass, whosAskin);
  }

  public boolean isInEnclosingClassHierarchy(IType ownersClass, IType whosAskin) {
    return _fm.isInEnclosingClassHierarchy(ownersClass, whosAskin);
  }

  public List getConstructors(Accessibility accessibility) {
    return _fm.getConstructors(accessibility);
  }

  public IConstructorInfo getConstructor(Accessibility accessibility, IType[] params) {
    return _fm.getConstructor(accessibility, params);
  }

  public boolean isFeatureAccessible(IAttributedFeatureInfo property, Accessibility accessibility) {
    return _fm.isFeatureAccessible(property, accessibility);
  }

  @Override
  public Accessibility getAccessibilityForType(IType whosaskin) {
    return _fm.getAccessibilityForClass(getOwnersType(), whosaskin);
  }

  @Override
  public List<? extends IPropertyInfo> getProperties(IType whosaskin) {
    return _fm.getProperties(getAccessibilityForType(whosaskin));
  }

  @Override
  public IPropertyInfo getProperty(IType whosaskin, CharSequence propName) {
    return _fm.getProperty(getAccessibilityForType(whosaskin), propName);
  }

  @Override
  public MethodList getMethods(IType whosaskin) {
    return _fm.getMethods(getAccessibilityForType(whosaskin));
  }

  @Override
  public IMethodInfo getMethod(IType whosaskin, CharSequence methodName, IType... params) {
    return _fm.getMethod(getAccessibilityForType(whosaskin), methodName, params);
  }

  @Override
  public List<? extends IConstructorInfo> getConstructors(IType whosaskin) {
    return _fm.getConstructors(getAccessibilityForType(whosaskin));
  }

  @Override
  public IConstructorInfo getConstructor(IType whosAskin, IType[] params) {
    return _fm.getConstructor(getAccessibilityForType(whosAskin), params);
  }

  @Override
  public List<? extends IPropertyInfo> getProperties() {
    return getProperties( null );
  }

  @Override
  public IPropertyInfo getProperty(CharSequence propName) {
    return getProperty( null, propName );
  }

  @Override
  public MethodList getMethods() {
    return getMethods( null );
  }

  @Override
  public List<? extends IConstructorInfo> getConstructors() {
    return _fm.getConstructors(Accessibility.PUBLIC);
  }

  @Override
  public List<IAnnotationInfo> getDeclaredAnnotations() {
    return Collections.emptyList();
  }

  @Override
  public boolean hasAnnotation(IType type) {
    return false;
  }

  @Override
  public WrappingTestTypeBase getOwnersType() {
    return _ownersType;
  }

  private class WrappingPropertyInfo implements IPropertyInfo {
    private IPropertyInfo _wrappedProperty;

    public WrappingPropertyInfo(IPropertyInfo propertyInfo) {
      _wrappedProperty = propertyInfo;
    }

    public boolean isReadable() {
      return _wrappedProperty.isReadable();
    }

    public boolean isWritable() {
      return _wrappedProperty.isWritable();
    }

    public boolean isWritable(IType whosAskin) {
      return _wrappedProperty.isWritable(whosAskin);
    }

    public IPropertyAccessor getAccessor() {
      return new IPropertyAccessor() {
        @Override
        public Object getValue(Object ctx) {
          _ownersType.firePropertyAccess(WrappingPropertyInfo.this, ctx, false, null);
          return getPropValue(_wrappedProperty, ctx);
        }

        @Override
        public void setValue(Object ctx, Object value) {
          _ownersType.firePropertyAccess(WrappingPropertyInfo.this, ctx, true, value);
          setPropValue(_wrappedProperty, ctx, value);
        }
      };
    }

    public IPresentationInfo getPresentationInfo() {
      return _wrappedProperty.getPresentationInfo();
    }

    public boolean isScriptable() {
      return _wrappedProperty.isScriptable();
    }

    public boolean isVisible(IScriptabilityModifier constraint) {
      return _wrappedProperty.isVisible(constraint);
    }

    public boolean isHidden() {
      return _wrappedProperty.isHidden();
    }

    public boolean isStatic() {
      return _wrappedProperty.isStatic();
    }

    public boolean isPrivate() {
      return _wrappedProperty.isPrivate();
    }

    public boolean isInternal() {
      return _wrappedProperty.isInternal();
    }

    public boolean isProtected() {
      return _wrappedProperty.isProtected();
    }

    public boolean isPublic() {
      return _wrappedProperty.isPublic();
    }

    public boolean isAbstract() {
      return _wrappedProperty.isAbstract();
    }

    public boolean isFinal() {
      return _wrappedProperty.isFinal();
    }

    public List<IAnnotationInfo> getAnnotations() {
      return _wrappedProperty.getAnnotations();
    }

    public List<IAnnotationInfo> getDeclaredAnnotations() {
      return _wrappedProperty.getDeclaredAnnotations();
    }

    public List<IAnnotationInfo> getAnnotationsOfType(IType type) {
      return _wrappedProperty.getAnnotationsOfType(type);
    }

    @Override
    public IAnnotationInfo getAnnotation(IType type) {
      return _wrappedProperty.getAnnotation(type);
    }

    @Override
    public boolean hasDeclaredAnnotation(IType type) {
      return _wrappedProperty.hasDeclaredAnnotation(type);
    }

    public boolean hasAnnotation(IType type) {
      return _wrappedProperty.hasAnnotation(type);
    }

    public boolean isDeprecated() {
      return _wrappedProperty.isDeprecated();
    }

    public String getDeprecatedReason() {
      return _wrappedProperty.getDeprecatedReason();
    }

    public boolean isDefaultImpl() {
      return _wrappedProperty.isDefaultImpl();
    }

    public IFeatureInfo getContainer() {
      return null;
    }

    public IType getOwnersType() {
      return _ownersType;
    }

    public String getName() {
      return _wrappedProperty.getName();
    }

    public String getDisplayName() {
      return _wrappedProperty.getDisplayName();
    }

    public String getDescription() {
      return _wrappedProperty.getDescription();
    }

    public IType getFeatureType() {
      return _wrappedProperty.getFeatureType();
    }

    @Override
    public String toString() {
      return getDisplayName();
    }
  }

  private class WrappingMethodInfo implements IMethodInfo {
    private IMethodInfo _wrappedMethodInfo;

    public WrappingMethodInfo(IMethodInfo methodInfo) {
      _wrappedMethodInfo = methodInfo;
    }

    public IParameterInfo[] getParameters() {
      return _wrappedMethodInfo.getParameters();
    }

    public IType getReturnType() {
      return _wrappedMethodInfo.getReturnType();
    }

    public IMethodCallHandler getCallHandler() {
      return new IMethodCallHandler() {
        @Override
        public Object handleCall(Object ctx, Object... args) {
          _ownersType.fireMethodCalled(WrappingMethodInfo.this, ctx, args );
          return invokeMethod(_wrappedMethodInfo, ctx, args);
        }
      };
    }

    public String getReturnDescription() {
      return _wrappedMethodInfo.getReturnDescription();
    }

    public List<IExceptionInfo> getExceptions() {
      return _wrappedMethodInfo.getExceptions();
    }

    public String getName() {
      return _wrappedMethodInfo.getName();
    }

    public boolean isScriptable() {
      return _wrappedMethodInfo.isScriptable();
    }

    public boolean isVisible(IScriptabilityModifier constraint) {
      return _wrappedMethodInfo.isVisible(constraint);
    }

    public boolean isHidden() {
      return _wrappedMethodInfo.isHidden();
    }

    public boolean isStatic() {
      return _wrappedMethodInfo.isStatic();
    }

    public boolean isPrivate() {
      return _wrappedMethodInfo.isPrivate();
    }

    public boolean isInternal() {
      return _wrappedMethodInfo.isInternal();
    }

    public boolean isProtected() {
      return _wrappedMethodInfo.isProtected();
    }

    public boolean isPublic() {
      return _wrappedMethodInfo.isPublic();
    }

    public boolean isAbstract() {
      return _wrappedMethodInfo.isAbstract();
    }

    public boolean isFinal() {
      return _wrappedMethodInfo.isFinal();
    }

    public List<IAnnotationInfo> getAnnotations() {
      return _wrappedMethodInfo.getAnnotations();
    }

    public List<IAnnotationInfo> getDeclaredAnnotations() {
      return _wrappedMethodInfo.getDeclaredAnnotations();
    }

    public List<IAnnotationInfo> getAnnotationsOfType(IType type) {
      return _wrappedMethodInfo.getAnnotationsOfType(type);
    }

    @Override
    public IAnnotationInfo getAnnotation(IType type) {
      return _wrappedMethodInfo.getAnnotation(type);
    }

    @Override
    public boolean hasDeclaredAnnotation(IType type) {
      return _wrappedMethodInfo.hasDeclaredAnnotation(type);
    }

    public boolean hasAnnotation(IType type) {
      return _wrappedMethodInfo.hasAnnotation(type);
    }

    public boolean isDeprecated() {
      return _wrappedMethodInfo.isDeprecated();
    }

    public String getDeprecatedReason() {
      return _wrappedMethodInfo.getDeprecatedReason();
    }

    @Override
    public boolean isDefaultImpl() {
      return _wrappedMethodInfo.isDefaultImpl();
    }

    public IFeatureInfo getContainer() {
      return _wrappedMethodInfo.getContainer();
    }

    public IType getOwnersType() {
      return _ownersType;
    }

    public String getDisplayName() {
      return _wrappedMethodInfo.getDisplayName();
    }

    public String getDescription() {
      return _wrappedMethodInfo.getDescription();
    }

    @Override
    public String toString() {
      return getDisplayName();
    }
  }

}
