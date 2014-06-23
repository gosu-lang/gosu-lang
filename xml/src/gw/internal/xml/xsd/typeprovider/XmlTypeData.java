/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.xsd.typeprovider;

import gw.lang.reflect.*;
import gw.lang.reflect.FeatureManager;
import gw.lang.reflect.IAnnotationInfo;
import gw.lang.reflect.IConstructorInfo;
import gw.lang.reflect.IMethodInfo;
import gw.lang.reflect.IPropertyInfo;
import gw.lang.reflect.IRelativeTypeInfo;
import gw.lang.reflect.IType;
import gw.lang.reflect.TypeInfoBase;
import gw.lang.reflect.TypeSystem;
import gw.util.concurrent.LockingLazyVar;

import java.util.Collections;
import java.util.List;

public abstract class XmlTypeData extends TypeInfoBase implements IXmlTypeData, IRelativeTypeInfo {

  private final LockingLazyVar<IType> TYPE = new LockingLazyVar<IType>() {
    @Override
    protected IType init() {
      return TypeSystem.getByFullName( getName() );
    }
  };

  private final LockingLazyVar<FeatureManager<String>> _fm = new LockingLazyVar<FeatureManager<String>>() {
    @Override
    protected FeatureManager<String> init() {
      FeatureManager<String> fm = new FeatureManager<String>( XmlTypeData.this, true, true );
      if ( prefixSuperProperties() ) {
        fm.setSuperPropertyPrefix( "$" );
      }
      IType supertypeToCopyPropertiesFrom = getSupertypeToCopyPropertiesFrom();
      if ( supertypeToCopyPropertiesFrom != null ) {
        fm.setSupertypeToCopyPropertiesFrom( supertypeToCopyPropertiesFrom );
      }
      return fm;
    }
  };
  private int _lastSingleRefreshChecksum;

  @Override
  public final List<? extends IPropertyInfo> getProperties() {
    maybeClearTypeInfo();
    return _fm.get().getProperties( Accessibility.PUBLIC );
  }

  public abstract String getName();

  @Override
  public final IPropertyInfo getProperty( CharSequence propName ) {
    maybeClearTypeInfo();
    return _fm.get().getProperty( Accessibility.PUBLIC, propName );
  }

  @Override
  public final MethodList getMethods() {
    maybeClearTypeInfo();
    return _fm.get().getMethods( Accessibility.PUBLIC );
  }

  @Override
  public final List<? extends IConstructorInfo> getConstructors() {
    maybeClearTypeInfo();
    return _fm.get().getConstructors( Accessibility.PUBLIC );
  }

  @Override
  public final List<IAnnotationInfo> getDeclaredAnnotations() {
    maybeClearTypeInfo();
    return Collections.emptyList();
  }

  @Override
  public final boolean hasAnnotation( IType type ) {
//    maybeClearTypeInfo();
    return false;
  }

  @Override
  public final IType getOwnersType() {
    return TYPE.get();
  }

  @Override
  public final IType getType() {
    return TYPE.get();
  }

  @Override
  public List<Class<?>> getAdditionalInterfaces() {
//    maybeClearTypeInfo();
    return Collections.emptyList();
  }

  @Override
  public Accessibility getAccessibilityForType( IType whosaskin ) {
    return FeatureManager.getAccessibilityForClass(getOwnersType(), whosaskin);
  }

  @Override
  public List<? extends IPropertyInfo> getProperties( IType whosaskin ) {
    maybeClearTypeInfo();
    return _fm.get().getProperties( getAccessibilityForType( whosaskin ) );
  }

  @Override
  public IPropertyInfo getProperty( IType whosaskin, CharSequence propName ) {
    maybeClearTypeInfo();
    return _fm.get().getProperty( getAccessibilityForType( whosaskin ), propName );
  }

  @Override
  public MethodList getMethods(IType whosaskin) {
    maybeClearTypeInfo();
    return _fm.get().getMethods( getAccessibilityForType( whosaskin ) );
  }

  @Override
  public IMethodInfo getMethod( IType whosaskin, CharSequence methodName, IType... params ) {
    maybeClearTypeInfo();
    return _fm.get().getMethod( getAccessibilityForType( whosaskin ), methodName, params );
  }

  @Override
  public List<? extends IConstructorInfo> getConstructors( IType whosaskin ) {
    maybeClearTypeInfo();
    return _fm.get().getConstructors( getAccessibilityForType( whosaskin ) );
  }

  @Override
  public IConstructorInfo getConstructor( IType whosAskin, IType[] params ) {
    maybeClearTypeInfo();
    return _fm.get().getConstructor( getAccessibilityForType( whosAskin ), params );
  }

  @Override
  public List<? extends IType> getInterfaces() {
    return Collections.emptyList();
  }

  @Override
  public IType getSupertypeToCopyPropertiesFrom() {
    return null;
  }

  @Override
  public String toString() {
    return getName();
  }

  private void maybeClearTypeInfo() {
    int singleRefreshChecksum = TypeSystem.getSingleRefreshChecksum();
    if ( _lastSingleRefreshChecksum != singleRefreshChecksum ) {
      _fm.clear();
      _lastSingleRefreshChecksum = singleRefreshChecksum;
    }
  }

}
