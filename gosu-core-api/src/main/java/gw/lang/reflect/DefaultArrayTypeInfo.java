/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect;

import gw.lang.GosuShop;
import gw.util.concurrent.LockingLazyVar;

import java.util.Collections;
import java.util.List;

public class DefaultArrayTypeInfo extends TypeInfoBase implements IRelativeTypeInfo {
  private IType _type;
  private LockingLazyVar<List<IPropertyInfo>> _declaredProperties;
  private final FeatureManager _fm;

  public DefaultArrayTypeInfo( IDefaultArrayType type ) {
    _type = type;

    _declaredProperties =
      new LockingLazyVar<List<IPropertyInfo>>()
          {
            protected List<IPropertyInfo> init()
            {
              return Collections.singletonList( GosuShop.createLengthProperty( DefaultArrayTypeInfo.this ) );
            }
          };

    _fm = new FeatureManager( this, true );
  }

  protected void unloadTypeInfo() {
    _fm.clear();
  }

  public List<? extends IPropertyInfo> getProperties() {
    return getProperties( null );
  }

  public IPropertyInfo getProperty( CharSequence propName ) {
    return getProperty( null, propName );
  }

  public MethodList getMethods() {
    return getMethods( null );
  }

  public List<? extends IConstructorInfo> getConstructors() {
    return getDeclaredConstructors();
  }

  public List<? extends IEventInfo> getEvents() {
    return getOwnersType().getSupertype().getTypeInfo().getEvents();
  }

  public IEventInfo getEvent( CharSequence strEvent ) {
    return getOwnersType().getSupertype().getTypeInfo().getEvent( strEvent );
  }

  public List<IAnnotationInfo> getDeclaredAnnotations() {
    return Collections.emptyList();
  }

  public boolean hasAnnotation( IType type ) {
    return false;
  }

  public boolean isDefaultImpl() {
    return false;
  }

  public IType getOwnersType() {
    return _type;
  }

  public IRelativeTypeInfo.Accessibility getAccessibilityForType( IType whosaskin ) {
    return Accessibility.PUBLIC;
  }

  public List<? extends IConstructorInfo> getDeclaredConstructors() {
    return Collections.emptyList();
  }

  public List<? extends IMethodInfo> getDeclaredMethods() {
    return MethodList.EMPTY;
  }

  public List<? extends IPropertyInfo> getDeclaredProperties() {
    return _declaredProperties.get();
  }

  public IConstructorInfo getConstructor( IType whosAskin, IType[] params ) {
    List<? extends IConstructorInfo> ctors = getConstructors( whosAskin );
    return FIND.constructor( ctors, params );
  }
  public List<? extends IConstructorInfo> getConstructors( IType whosaskin ) {
    return _fm.getConstructors( getAccessibilityForType( whosaskin ) );
  }

  public IMethodInfo getMethod( IType whosaskin, CharSequence methodName, IType... params ) {
    MethodList methods = getMethods( whosaskin );
    return FIND.method( methods, methodName, params );
  }
  public MethodList getMethods( IType whosaskin ) {
    return _fm.getMethods( getAccessibilityForType( whosaskin ) );
  }

  public IPropertyInfo getProperty( IType whosaskin, CharSequence propName ) {
    return _fm.getProperty( getAccessibilityForType( whosaskin ), propName );
  }
  public List<? extends IPropertyInfo> getProperties( IType whosaskin ) {
    return _fm.getProperties( getAccessibilityForType( whosaskin ) );
  }

  /**
   */
  public String getName() {
    return _type.getRelativeName();
  }

  public String getDisplayName() {
    return _type.getComponentType().getTypeInfo().getDisplayName() + "[]";
  }

  public String getDescription() {
    return "Array of " + _type.getComponentType().getRelativeName() + " objects";
  }
}
