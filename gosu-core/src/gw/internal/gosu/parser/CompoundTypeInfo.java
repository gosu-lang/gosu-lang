/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.lang.reflect.FeatureManager;
import gw.lang.reflect.MethodList;
import gw.lang.reflect.BaseTypeInfo;
import gw.lang.reflect.IAnnotationInfo;
import gw.lang.reflect.IAttributedFeatureInfo;
import gw.lang.reflect.IConstructorInfo;
import gw.lang.reflect.IEventInfo;
import gw.lang.reflect.IMethodInfo;
import gw.lang.reflect.IPropertyInfo;
import gw.lang.reflect.IRelativeTypeInfo;
import gw.lang.reflect.IType;
import gw.lang.reflect.ITypeInfo;
import gw.lang.reflect.TypeInfoUtil;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.MethodInfoDelegate;
import gw.lang.reflect.PropertyInfoDelegate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 */
public class CompoundTypeInfo extends BaseTypeInfo implements IRelativeTypeInfo
{
  volatile private Map<CharSequence, IPropertyInfo> _propertiesByName;
  volatile private List<IPropertyInfo> _properties;
  volatile private MethodList _methods;

  /**
   */
  public CompoundTypeInfo( CompoundType intrType )
  {
    super( intrType );
  }

  public CompoundType getOwnersType()
  {
    return (CompoundType)super.getOwnersType();
  }

  public List<? extends IPropertyInfo> getProperties()
  {
    if( _properties == null )
    {
      extractProperties();
    }
    return _properties;
  }

  public IPropertyInfo getProperty( CharSequence propName )
  {
    if( _properties == null )
    {
      extractProperties();
    }
    return _propertiesByName.get( propName );
  }

  /**
   */
  public MethodList getMethods()
  {
    if( _methods == null )
    {
      extractMethods();
    }

    return _methods;
  }

  public IMethodInfo getMethod( CharSequence methodName, IType... params )
  {
    return FIND.method( getMethods(), methodName, params );
  }

  /**
   */
  public List<IConstructorInfo> getConstructors()
  {
    return Collections.emptyList();
  }

  public IConstructorInfo getConstructor( IType... params )
  {
    return null;
  }

  public IMethodInfo getCallableMethod( CharSequence strMethod, IType... params )
  {
    return FIND.callableMethod( getMethods(), strMethod, params );
  }

  public IConstructorInfo getCallableConstructor( IType... params )
  {
    return FIND.callableConstructor( getConstructors(), params );
  }

  /**
   */
  public List<IEventInfo> getEvents()
  {
    return Collections.emptyList();
  }

  /**
   */
  public IEventInfo getEvent( CharSequence strEvent )
  {
    return null;
  }

  private void extractProperties()
  {
    if( _properties == null )
    {
      TypeSystem.lock();
      try
      {
        if( _properties == null )
        {
          _propertiesByName = new HashMap<CharSequence, IPropertyInfo>();

          for( IType type : getOwnersType().getTypes() )
          {
            ITypeInfo ti = type.getTypeInfo();
            List<? extends IPropertyInfo> properties;
            if( ti instanceof IRelativeTypeInfo )
            {
              properties = ((IRelativeTypeInfo)ti).getProperties( type );
            }
            else
            {
              properties = ti.getProperties();
            }
            for( IPropertyInfo pi : properties )
            {
              _propertiesByName.put( pi.getName(), new PropertyInfoDelegate( this, pi ) );
            }
          }

          _properties = TypeInfoUtil.makeSortedUnmodifiableRandomAccessListFromFeatures( _propertiesByName );
        }
      }
      finally
      {
        TypeSystem.unlock();
      }
    }
  }

  private void extractMethods()
  {
    if( _methods == null )
    {
      TypeSystem.lock();
      try
      {
        if( _methods == null )
        {
          MethodList allMethods = new MethodList();

          for( IType type : getOwnersType().getTypes() )
          {
            ITypeInfo ti = type.getTypeInfo();
            List<? extends IMethodInfo> methods;
            if( ti instanceof IRelativeTypeInfo )
            {
              methods = ((IRelativeTypeInfo)ti).getMethods( type );
            }
            else
            {
              methods = ti.getMethods();
            }
            for( IMethodInfo mi : methods )
            {
              allMethods.add( new MethodInfoDelegate( this, mi ) );
            }
          }

          _methods = TypeInfoUtil.makeSortedUnmodifiableRandomAccessList( allMethods );
        }
      }
      finally
      {
        TypeSystem.unlock();
      }
    }
  }

  public List<IAnnotationInfo> getDeclaredAnnotations()
  {
    return Collections.emptyList();
  }

  public IRelativeTypeInfo.Accessibility getAccessibilityForType( IType whosAskin )
  {
    IRelativeTypeInfo.Accessibility accessibility = IRelativeTypeInfo.Accessibility.PRIVATE;

    whosAskin =  whosAskin == null ? JavaTypeInfo.getCompilingClass( getOwnersType() ) : whosAskin;

    if( whosAskin == getOwnersType() ) {
      return accessibility;
    }

    for( IType type : getOwnersType().getTypes() )
    {
      if( type == null || whosAskin == null )
      {
        accessibility = IRelativeTypeInfo.Accessibility.PUBLIC;
      }
      else
      {
        IRelativeTypeInfo.Accessibility csr = FeatureManager.getAccessibilityForClass( type, whosAskin );
        if( csr.ordinal() < accessibility.ordinal() )
        {
          accessibility = csr;
        }
      }
    }
    return accessibility;
  }

  public List<? extends IPropertyInfo> getProperties( IType whosaskin )
  {
    IRelativeTypeInfo.Accessibility accessibility = getAccessibilityForType( whosaskin );
    List<IPropertyInfo> properties = new ArrayList<IPropertyInfo>( getProperties() );
    Iterator<IPropertyInfo> iterator = properties.iterator();
    while( iterator.hasNext() )
    {
      IPropertyInfo propertyInfo = iterator.next();
      boolean hidden = isHidden( accessibility, propertyInfo );
      if( hidden )
      {
        iterator.remove();
      }
    }
    return properties;
  }

  public IPropertyInfo getProperty( IType whosaskin, CharSequence propName )
  {
    IPropertyInfo property = getProperty( propName );
    if( property == null || isHidden( getAccessibilityForType( whosaskin ), property ) )
    {
      return null;
    }
    return property;
  }

  public MethodList getMethods(IType whosaskin)
  {
    IRelativeTypeInfo.Accessibility accessibility = getAccessibilityForType( whosaskin );
    MethodList methods = new MethodList( getMethods() );
    Iterator<IMethodInfo> iterator = methods.iterator();
    while( iterator.hasNext() )
    {
      IMethodInfo methodInfo = iterator.next();
      boolean hidden = isHidden( accessibility, methodInfo );
      if( hidden )
      {
        iterator.remove();
      }
    }
    return methods;
  }

  public IMethodInfo getMethod( IType whosaskin, CharSequence methodName, IType... params )
  {
    IMethodInfo method = getMethod( methodName, params );
    if( method == null || isHidden( getAccessibilityForType( whosaskin ), method ) )
    {
      return null;
    }
    return method;
  }

  private boolean isHidden( IRelativeTypeInfo.Accessibility accessibility, IAttributedFeatureInfo fi )
  {
    boolean hidden = false;
    if( fi.isPrivate() && accessibility != IRelativeTypeInfo.Accessibility.PRIVATE )
    {
      hidden = true;
    }
    else if( fi.isInternal() && accessibility.ordinal() < IRelativeTypeInfo.Accessibility.INTERNAL.ordinal() )
    {
      hidden = true;
    }
    else if( fi.isProtected() && accessibility.ordinal() <  IRelativeTypeInfo.Accessibility.PROTECTED.ordinal() )
    {
      hidden = true;
    }
    return hidden;
  }

  @SuppressWarnings({"UnusedDeclaration"})
  public List<? extends IConstructorInfo> getConstructors( IType whosaskin )
  {
    return Collections.emptyList();
  }

  @SuppressWarnings({"UnusedDeclaration"})
  public IConstructorInfo getConstructor( IType whosAskin, IType[] params )
  {
    return null;
  }

  @Override
  public List<? extends IPropertyInfo> getDeclaredProperties() {
    return Collections.emptyList();
  }

  @Override
  public List<? extends IMethodInfo> getDeclaredMethods() {
    return Collections.emptyList();
  }

  @Override
  public List<? extends IConstructorInfo> getDeclaredConstructors() {
    return Collections.emptyList();
  }
}
