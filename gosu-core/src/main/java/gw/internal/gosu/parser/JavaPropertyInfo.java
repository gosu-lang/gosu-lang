/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.config.CommonServices;
import gw.lang.Deprecated;
import gw.lang.GosuShop;
import gw.lang.PublishedName;
import gw.lang.javadoc.IDocRef;
import gw.lang.javadoc.IMethodNode;
import gw.lang.parser.EvaluationException;
import gw.lang.parser.TypeVarToTypeMap;
import gw.lang.reflect.IAnnotationInfo;
import gw.lang.reflect.IFeatureInfo;
import gw.lang.reflect.IMethodInfo;
import gw.lang.reflect.IPresentationInfo;
import gw.lang.reflect.IPropertyAccessor;
import gw.lang.reflect.IRelativeTypeInfo;
import gw.lang.reflect.IScriptabilityModifier;
import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.java.ClassInfoUtil;
import gw.lang.reflect.java.IJavaAnnotatedElement;
import gw.lang.reflect.java.IJavaClassField;
import gw.lang.reflect.java.IJavaClassInfo;
import gw.lang.reflect.java.IJavaClassMethod;
import gw.lang.reflect.java.IJavaClassType;
import gw.lang.reflect.java.IJavaMethodInfo;
import gw.lang.reflect.java.IJavaPropertyDescriptor;
import gw.lang.reflect.java.IJavaPropertyInfo;
import gw.util.GosuExceptionUtil;
import gw.util.GosuStringUtil;
import gw.util.concurrent.LockingLazyVar;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.List;

/**
 */
public class JavaPropertyInfo extends JavaBaseFeatureInfo implements IJavaPropertyInfo
{
  private IJavaPropertyDescriptor _pd;
  private IType _propertyType;
  private IPropertyAccessor _accessor;
  private Boolean _bStatic;
  private boolean _bExternal;
  private boolean _bReadable;
  private IJavaClassMethod _getMethod;
  private IJavaClassMethod _setMethod;
  private IJavaClassField _publicField;
  private IPresentationInfo _presInfo;
  private String _name;
  private LockingLazyVar<IJavaAnnotatedElement> _annotatedElement = new LockingLazyVar<IJavaAnnotatedElement>() {
    @Override
    protected IJavaAnnotatedElement init() {
      return _pd.getReadMethod() != null ? _pd.getReadMethod() : _pd.getWriteMethod();
    }
  };

  /**
   * @param container Typically this will be the containing ITypeInfo
   * @param pd        The property descriptor (from BeanInfo)
   */
  JavaPropertyInfo( IFeatureInfo container, IJavaPropertyDescriptor pd )
  {
    super( container );
    _pd = pd;
    initFlags();
  }

  private JavaPropertyInfo( IFeatureInfo container, IJavaPropertyDescriptor pd, IType propertyType )
  {
    super( container );
    _pd = pd;
    _propertyType = propertyType;
    initFlags();
  }

  private JavaPropertyInfo( IFeatureInfo container, IJavaPropertyDescriptor pd, IType propertyType, IPresentationInfo presInfo )
  {
    this( container, pd, propertyType );
    _presInfo = presInfo;
  }

  public static IJavaPropertyInfo newInstance(IFeatureInfo container, IJavaPropertyDescriptor pd) {
    return JavaTypeExtensions.maybeExtendProperty(new JavaPropertyInfo(container, pd));
  }

  public static IJavaPropertyInfo newInstance(IFeatureInfo container, IJavaPropertyDescriptor pd, IType propertyType) {
    return JavaTypeExtensions.maybeExtendProperty(new JavaPropertyInfo(container, pd, propertyType));
  }

  public static IJavaPropertyInfo newInstance(IFeatureInfo container, IJavaPropertyDescriptor pd, IType propertyType, IPresentationInfo presInfo) {
    return JavaTypeExtensions.maybeExtendProperty(new JavaPropertyInfo(container, pd, propertyType, presInfo));
  }

  private void initFlags()
  {
    _name = _pd.getName();

    _getMethod = _pd.getReadMethod();
    if( _getMethod != null ) {
      if (_getMethod instanceof MethodJavaClassMethod) {
        ((MethodJavaClassMethod)_getMethod).setAccessible(true);
      }
      IAnnotationInfo property = _getMethod.getAnnotation(PublishedName.class);
      if (property != null) {
        _name = (String) property.getFieldValue("value");
        if (_name.startsWith("get") || _name.startsWith("set")) {
          _name = _name.substring(3);
        } else if (_name.startsWith("is")) {
          _name = _name.substring(2);
        }
      }
    }
    _setMethod = _pd.getWriteMethod();
    if( _setMethod != null ) {
      if (_setMethod instanceof MethodJavaClassMethod) {
        ((MethodJavaClassMethod)_setMethod).setAccessible(true);
      }
      IAnnotationInfo property = _setMethod.getAnnotation(PublishedName.class);
      if (property != null) {
        _name = (String) property.getFieldValue("value");
        if (_name.startsWith("get") || _name.startsWith("set")) {
          _name = _name.substring(3);
        } else if (_name.startsWith("is")) {
          _name = _name.substring(2);
        }
      }
    }

    // check for underlying public field to fill in "missing" getter/setter
    if (_getMethod == null && _setMethod != null) {
      findFieldOn(_setMethod.getEnclosingClass(), false);
    }
    if (_setMethod == null && _getMethod != null) {
      findFieldOn(_getMethod.getEnclosingClass(), true);
    }

    if( isWritable( getOwnersType()) ) {
      if (_pd.getWriteMethod() != null) {
        IJavaClassInfo declClass = _pd.getWriteMethod().getEnclosingClass();
        _bExternal = declClass instanceof ClassJavaClassInfo && CommonServices.getEntityAccess().isExternal(((ClassJavaClassInfo)declClass).getJavaClass());
      } else {
        IJavaClassInfo declClass = _publicField.getEnclosingClass();
        _bExternal = declClass instanceof ClassJavaClassInfo && CommonServices.getEntityAccess().isExternal(((ClassJavaClassInfo)declClass).getJavaClass());
      }
    }
    else if( isReadable() ) {
      if (_pd.getReadMethod() != null) {
        IJavaClassInfo declClass = _pd.getReadMethod().getEnclosingClass();
        _bExternal = declClass instanceof ClassJavaClassInfo && CommonServices.getEntityAccess().isExternal(((ClassJavaClassInfo)declClass).getJavaClass());
      } else {
        if (_publicField == null) {
          _bExternal = false;
        } else {
          IJavaClassInfo declClass = _publicField.getEnclosingClass();
          _bExternal = declClass instanceof ClassJavaClassInfo && CommonServices.getEntityAccess().isExternal(((ClassJavaClassInfo) declClass).getJavaClass());
        }
      }
    }
    _bReadable = isReadable();
  }

  private void findFieldOn(IJavaClassInfo clazz, boolean setter) {
    for (IJavaClassField field : clazz.getFields()) {
      if (field.getName().equals(_name) && Modifier.isStatic( field.getModifiers() ) == isStatic() ) {
        IType lhs, rhs;
        if (setter) {
          lhs = getFeatureType();
          rhs = TypeSystem.get(field.getType());
        } else {
          rhs = getFeatureType();
          lhs = TypeSystem.get(field.getType());
        }
        if(CommonServices.getCoercionManager().canCoerce(lhs, rhs)) {
          _publicField = field;
          break;
        }
      }
    }
  }

  @Override
  public IType getGenericIntrinsicType()
  {
    return getIntrinsicType( true );
  }

  @Override
  public IType getFeatureType()
  {
    IType ownerType = getOwnersType();
    return getIntrinsicType( !ownerType.isGenericType() || ownerType.isParameterizedType() );
  }

  private IType getIntrinsicType( boolean bKeepTypeVars )
  {
    if( _propertyType != null && !bKeepTypeVars )
    {
      return _propertyType;
    }

    IType propType;
    IType ownersType = getOwnersType();
    if( _getMethod != null )
    {
      IJavaClassType genericType = _getMethod.getGenericReturnType();
      if( genericType instanceof IJavaClassInfo )
      {
        propType = _pd.getPropertyType();
      }
      else if( genericType != null )
      {
        TypeVarToTypeMap actualParamByVarName =
          TypeLord.mapTypeByVarName( ownersType, _getMethod.getEnclosingClass().getJavaType(), bKeepTypeVars );
        propType = genericType.getActualType( actualParamByVarName, bKeepTypeVars );
      }
      else
      {
        propType = TypeSystem.getErrorType();
      }
    }
    else if( _setMethod != null )
    {
      TypeVarToTypeMap actualParamByVarName =
        TypeLord.mapTypeByVarName( ownersType, _setMethod.getEnclosingClass().getJavaType(), bKeepTypeVars );
      propType = _setMethod.getGenericParameterTypes()[0].getActualType( actualParamByVarName, bKeepTypeVars );
    }
    else
    {
      propType = _pd.getPropertyType();
    }

    if( propType.isGenericType() && !propType.isParameterizedType() )
    {
      propType = TypeLord.getDefaultParameterizedType( propType );
    }

    IJavaClassInfo declaringClass = getDeclaringClass();
    if( declaringClass != null )
    {
      propType = ClassInfoUtil.getPublishedType( propType, declaringClass );
    }

    if( !bKeepTypeVars )
    {
      //Cache the non-generic value.
      _propertyType = propType;
    }
    return propType;
  }

  private IJavaClassInfo getDeclaringClass() {
    if (_getMethod != null) {
      return _getMethod.getEnclosingClass();
    } else if (_setMethod != null) {
      return _setMethod.getEnclosingClass();
    } else {
      return null;
    }
  }

  @Override
  public boolean isReadable()
  {
    IJavaClassMethod get = _pd.getReadMethod();
    return (get != null && !_pd.isHidden()) || _publicField != null;
  }

  @Override
  public boolean isWritable(IType whosAskin) {
    /* causes stack overflow loading the parent type info...
    IMethodInfo methodInfo = getWriteMethodInfo();
    if (methodInfo != null) {
      return methodInfo.isVisible(scriptabilityLevel);
    }
    */
    IJavaClassMethod set = _pd.getWriteMethod();
    if ((set != null && !_pd.isHidden()) || (_publicField != null && !Modifier.isFinal(_publicField.getModifiers()))) {
      if (getContainer() instanceof IRelativeTypeInfo) {
        IRelativeTypeInfo.Accessibility accessibilityForType = ((IRelativeTypeInfo) getContainer()).getAccessibilityForType(whosAskin);
        int mods;
        if (set != null) {
          mods = set.getModifiers();
        } else {
          mods = _publicField.getModifiers();
        }
        boolean isAccessible = false;
        boolean isInternal = !Modifier.isPrivate(mods) && !Modifier.isPublic(mods) && !Modifier.isProtected(mods);
        switch (accessibilityForType) {
          case PUBLIC:
            if (Modifier.isPublic(mods)) {
              isAccessible = true;
            }
            break;
          case PROTECTED:
            if (Modifier.isPublic(mods) || Modifier.isProtected(mods)) {
              isAccessible = true;
            }
            break;
          case INTERNAL:
            if (Modifier.isPublic(mods) || isInternal || Modifier.isProtected(mods)) {
              isAccessible = true;
            }
            break;
          case PRIVATE:
            if (Modifier.isPublic(mods) || isInternal || Modifier.isProtected(mods) || Modifier.isPrivate(mods)) {
              isAccessible = true;
            }
            break;
        }
        return isAccessible;
      } else {
        return true;
      }
    }
    return false;
  }

  @Override
  public boolean isWritable() {
    return isWritable(null);
  }

  @Override
  public List<IAnnotationInfo> getDeclaredAnnotations() {
    List<IAnnotationInfo> annotations = super.getDeclaredAnnotations();
    if (getMethodDocs() != null && getMethodDocs().get() != null && getMethodDocs().get().isDeprecated()) {
      annotations.add(GosuShop.getAnnotationInfoFactory().createJavaAnnotation(makeDeprecated(getMethodDocs().get().getDeprecated()), this));
    }
    return annotations;
  }

  @Override
  public IDocRef<IMethodNode> getMethodDocs() {
    IMethodInfo methodInfo = getReadMethodInfo();
    if (methodInfo instanceof IJavaMethodInfo) {
      return ((IJavaMethodInfo)methodInfo).getMethodDocs();
    }

    methodInfo = getWriteMethodInfo();
    if (methodInfo instanceof IJavaMethodInfo) {
      return ((IJavaMethodInfo)methodInfo).getMethodDocs();
    }

    return null;
  }

  @Override
  public String getReturnDescription()
  {
    IDocRef<IMethodNode> methodDocs = getMethodDocs();
    return (methodDocs == null || methodDocs.get() == null) ? "" : methodDocs.get().getReturnDescription();
  }


  @Override
  public boolean isStatic()
  {
    if( _bStatic == null )
    {
      _bStatic = Boolean.FALSE;
      IJavaClassMethod getter = _pd.getReadMethod();
      if( getter != null && Modifier.isStatic( getter.getModifiers() ) )
      {
        _bStatic = Boolean.TRUE;
      }
    }
    return _bStatic;
  }

  @Override
  public boolean isPrivate()
  {
    IJavaClassMethod getter = _pd.getReadMethod();
    return getter == null ? super.isPrivate() : Modifier.isPrivate( getter.getModifiers() );
  }

  @Override
  public boolean isInternal()
  {
    return !isPrivate() && !isPublic() && !isProtected();
  }

  @Override
  public boolean isProtected()
  {
    IJavaClassMethod getter = _pd.getReadMethod();
    if (getter != null) {
      return Modifier.isProtected(getter.getModifiers());
    }

    IJavaClassMethod setter = _pd.getWriteMethod();
    if (setter != null) {
      return Modifier.isProtected(setter.getModifiers());
    }

    return super.isProtected();
  }

  @Override
  public boolean isPublic()
  {
    IJavaClassMethod getter = _pd.getReadMethod();
    if (getter != null) {
      return Modifier.isPublic(getter.getModifiers());
    }

    IJavaClassMethod setter = _pd.getWriteMethod();
    if (setter != null) {
      return Modifier.isPublic(setter.getModifiers());
    }

    return super.isPublic();
  }

  @Override
  public boolean isAbstract()
  {
    IJavaClassMethod getter = _pd.getReadMethod();
    return getter == null ? super.isAbstract() : Modifier.isAbstract( getter.getModifiers() );
  }

  @Override
  public boolean isFinal()
  {
    IJavaClassMethod getter = _pd.getReadMethod();
    return getter == null ? super.isFinal() : Modifier.isFinal( getter.getModifiers() );
  }

  @Override
  protected IJavaAnnotatedElement getAnnotatedElement()
  {
    return _annotatedElement.get();
  }

  @Override
  protected boolean isVisibleViaFeatureDescriptor(IScriptabilityModifier constraint) {
    return _pd.isVisibleViaFeatureDescriptor(constraint);
  }

  @Override
  protected boolean isHiddenViaFeatureDescriptor() {
    return _pd.isHiddenViaFeatureDescriptor();
  }

  @Override
  protected boolean isDefaultEnumFeature()
  {
    return false;
  }

  @Override
  public boolean isDeprecated()
  {
    if (super.isDeprecated()) {
      return true;
    }

    //noinspection SimplifiableIfStatement
    if (_pd.getReadMethod() != null) {
      return _pd.isDeprecated();
    }
    return false;
  }

  @Override
  public String getDeprecatedReason() {
    String deprecated = super.getDeprecatedReason();
    if (isDeprecated() && deprecated == null) {
      IAnnotationInfo annotation = _pd.getReadMethod() == null ? null : _pd.getReadMethod().getAnnotation(Deprecated.class);
      return annotation == null ? "" : (String) annotation.getFieldValue("value");
    }
    return deprecated;
  }

  @Override
  public IPropertyAccessor getAccessor()
  {
    if( _accessor == null )
    {
      _accessor = new PropertyAccessorAdaptor();
    }
    return _accessor;
  }

  @Override
  public IPresentationInfo getPresentationInfo()
  {
    return _presInfo == null ? IPresentationInfo.Default.GET : _presInfo;
  }

  @Override
  public String getName()
  {
    return _name;
  }

  @Override
  public String getDisplayName()
  {
    return _pd.getDisplayName();
  }

  @Override
  public String getShortDescription()
  {
    return _pd.getShortDescription();
  }

  @Override
  public String getDescription() {
    String description = null;
    IMethodInfo method = getReadMethodInfo();
    if (method != null) {
      description = method.getDescription();
      if ( GosuStringUtil.isEmpty(description)) {
        description = method.getReturnDescription();
      }
    }
    if ( GosuStringUtil.isEmpty(description)) {
      method = getWriteMethodInfo();
      if (method != null) {
        description = method.getDescription();
      }
    }
    return description;
  }

  @Override
  public IMethodInfo getReadMethodInfo() {
    IJavaClassMethod method = getPropertyDescriptor().getReadMethod();
    if (method != null) {
      return getOwnersType().getTypeInfo().getMethod(method.getName(), getTypesFromClasses(method.getParameterTypes()));
    }

    return null;
  }

  @Override
  public IJavaClassField getPublicField()
  {
    return _publicField;
  }
  
  @Override
  public IMethodInfo getWriteMethodInfo() {
    IJavaClassMethod method = getPropertyDescriptor().getWriteMethod();
    if (method != null) {
      return getOwnersType().getTypeInfo().getMethod(method.getName(), getTypesFromClasses(method.getParameterTypes()));
    }

    return null;
  }

  private IType[] getTypesFromClasses(IJavaClassInfo[] types) {
    IType retValue[] = new IType[types.length];
    for (int i = 0; i < types.length; i++) {
      IJavaClassInfo type = types[i];
      retValue[i] = type.getJavaType();
    }

    return retValue;
  }

  @Override
  public String toString() {
    return getName();
  }

  @Override
  public IJavaPropertyDescriptor getPropertyDescriptor()
  {
    return _pd;
  }

  public class PropertyAccessorAdaptor implements IPropertyAccessor
  {
    @Override
    public Object getValue( Object ctx )
    {
      if( !_bReadable )
      {
        throw new EvaluationException( "Property, " + getName() + ", is not readable!" );
      }

      Object[] args = null;
      try
      {
        Object rVal;
        if (_getMethod != null) {
          rVal = _getMethod.invoke( ctx, args );
        } else {
          rVal = ((FieldJavaClassField)_publicField).get(ctx);
          rVal = CommonServices.getCoercionManager().convertValue(rVal, getFeatureType());
        }
        if( _bExternal )
        {
          return CommonServices.getEntityAccess().convertToInternalIfNecessary( rVal, getOwningClass() );
        }
        else
        {
          return rVal;
        }
      }
      catch( InvocationTargetException ite )
      {
        throw GosuExceptionUtil.forceThrow( ite.getCause() );
      }
      catch( Throwable t )
      {
        throw GosuExceptionUtil.forceThrow( t );
      }
    }

    @Override
    public void setValue( Object ctx, Object value )
    {
      if( !isWritable( getOwnersType()) )
      {
        throw new EvaluationException( "Property, " + getName() + ", is not writable!" );
      }

      try
      {
        Object[] args = new Object[]{value};
        if (_setMethod != null) {
          if( _bExternal )
          {
            args = CommonServices.getEntityAccess().convertToExternalIfNecessary(
                    args, ((MethodJavaClassMethod)_setMethod).getJavaParameterTypes(), getOwningClass() );
          }

          _setMethod.invoke( ctx, args );
        } else {
          value = CommonServices.getCoercionManager().convertValue(value, TypeSystem.get(_publicField.getType()));
          ((FieldJavaClassField)_publicField).set(ctx, value);
        }
      }
      catch( InvocationTargetException ite )
      {
        throw GosuExceptionUtil.forceThrow( ite.getCause() );
      }
      catch( Throwable t )
      {
        throw GosuExceptionUtil.forceThrow( t );
      }
    }

    private Class getOwningClass()
    {
      if( isWritable( getOwnersType()) )
      {
        return ((ClassJavaClassInfo)_pd.getWriteMethod().getEnclosingClass()).getJavaClass();
      }
      else
      {
        return ((ClassJavaClassInfo)_pd.getReadMethod().getEnclosingClass()).getJavaClass();
      }
    }

    public IJavaClassMethod getGetterMethod()
    {
      return _getMethod;
    }

    public IJavaClassMethod getSetterMethod()
    {
      return _setMethod;
    }
  }
}
