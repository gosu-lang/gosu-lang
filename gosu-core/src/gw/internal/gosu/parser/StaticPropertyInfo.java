/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.config.CommonServices;
import gw.lang.reflect.BaseFeatureInfo;
import gw.lang.parser.EvaluationException;
import gw.lang.reflect.IAnnotationInfo;
import gw.lang.reflect.IFeatureInfo;
import gw.lang.reflect.IPresentationInfo;
import gw.lang.reflect.IPropertyAccessor;
import gw.lang.reflect.IPropertyInfo;
import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;
import gw.util.GosuExceptionUtil;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

/**
 */
public class StaticPropertyInfo extends BaseFeatureInfo implements IPropertyInfo
{
  private Method _getter;
  private Method _setter;
  private IType _propertyType;
  private String _strName;
  private String _deprecated;
  private IType _type;
  private IPropertyAccessor _accessor;

  /**
   * @param container    Typically the ITypeInfo instance exposing this property
   * @param type         This is the IType class having the method[s] you want to expose
   *                     as a static property e.g., TypeListIntrinsicType.getTypeKeys().
   * @param propertyType This is the type of the property. You only need to set this if
   *                     you want to override the getter method's return type. Otherwise set this to null.
   * @param strName      The name for the property.
   * @param strGetter    The name of the getter method defined in the IType impl.
   * @param strSetter    Optional setter method name.
   * @param deprecated   The property's deprecation message, or null if the property is not deprecated.
   */
  StaticPropertyInfo( IFeatureInfo container, IType type, IType propertyType,
                      String strName, String strGetter, String strSetter, String deprecated )
    throws NoSuchMethodException
  {
    super( container );
    _type = type;
    _propertyType = propertyType;
    _strName = strName;
    _deprecated = deprecated;
    _accessor = new StaticAccessor( strGetter, strSetter );
  }

  public String getName()
  {
    return _strName;
  }

  public IType getFeatureType()
  {
    return _propertyType;
  }

  public List<IAnnotationInfo> getDeclaredAnnotations()
  {
    return Collections.emptyList();
  }

  public boolean isScriptable()
  {
    return true;
  }

  public boolean isStatic()
  {
    return true;
  }

  public boolean isReadable()
  {
    return true;
  }

  public boolean isWritable(IType whosAskin) {
    return _setter != null;
  }

  public boolean isWritable()
  {
    return isWritable(null);
  }

  public boolean isDeprecated()
  {
    return _deprecated != null;
  }

  public String getDeprecatedReason() {
    return _deprecated;
  }

  public IPropertyAccessor getAccessor()
  {
    return _accessor;
  }

  public IPresentationInfo getPresentationInfo()
  {
    return IPresentationInfo.Default.GET;
  }

  private class StaticAccessor implements IPropertyAccessor
  {
    private StaticAccessor( String strGetter, String strSetter ) throws NoSuchMethodException
    {
      Class intrinsicTypeClass = _type.getClass();
      _getter = intrinsicTypeClass.getMethod( strGetter, null );
      if( strSetter != null && strSetter.length() > 0 )
      {
        _setter = intrinsicTypeClass.getMethod( strSetter, new Class[]{_getter.getReturnType()} );
      }
      if( _propertyType == null )
      {
        _propertyType = TypeSystem.get( _getter.getReturnType() );
      }
    }

    public Object getValue( Object ctx )
    {
      try
      {
        return _getter.invoke( _type, null );
      }
      catch( Exception e )
      {
        throw GosuExceptionUtil.forceThrow( e );
      }
    }

    public void setValue( Object ctx, Object value )
    {
      if( !isWritable( getOwnersType()) )
      {
        throw new EvaluationException( "Property, " + getName() + ", is not writable!" );
      }

      try
      {
        value = CommonServices.getCoercionManager().convertValue(value, getFeatureType());
        _setter.invoke( _type, new Object[]{value} );
      }
      catch( Exception e )
      {
        throw GosuExceptionUtil.forceThrow( e );
      }
    }
  }
}
