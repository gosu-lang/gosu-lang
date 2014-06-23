/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.expressions;

import gw.config.CommonServices;
import gw.internal.gosu.parser.BeanAccess;
import gw.internal.gosu.parser.Expression;
import gw.internal.gosu.parser.Statement;
import gw.lang.parser.EvaluationException;
import gw.lang.parser.exceptions.ParseException;
import gw.lang.parser.statements.ITerminalStatement;
import gw.lang.parser.expressions.IInitializerAssignment;
import gw.lang.reflect.IPropertyInfo;
import gw.lang.reflect.IType;
import gw.lang.reflect.ITypeInfo;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.java.JavaTypes;
import gw.util.GosuExceptionUtil;

import java.util.Collection;
import java.util.Map;

final public class InitializerAssignment extends Statement implements IInitializerAssignment
{
  private IType _ownerType;
  private String _propertyName;
  private Expression _rhs;
  private IPropertyInfo _pi;

  public InitializerAssignment( IType ownerType, String propertyName )
  {
    _ownerType = ownerType;
    _propertyName = propertyName;
  }

  public IType getOwnerType()
  {
    return _ownerType;
  }

  public String getPropertyName()
  {
    return _propertyName;
  }
  public String toString()
  {
    return _propertyName;
  }

  public Expression getRhs()
  {
    return _rhs;
  }
  public void setRhs( Expression rhs )
  {
    _rhs = rhs;
  }

  protected ITerminalStatement getLeastSignificantTerminalStatement_internal( boolean[] bAbsolute )
  {
    bAbsolute[0] = false;
    return null;
  }

  public Object execute()
  {
    return null;
  }

  public void execute( Object contextValue )
  {
    ITypeInfo typeInfo = getOwnerType().getTypeInfo();
    if( typeInfo == null )
    {
      throw new EvaluationException( "No type info found for, " + getOwnerType().getName() );
    }

    IPropertyInfo pi = getPropertyInfo();
    if( pi != null && !BeanAccess.isDescriptorHidden( pi ) )
    {
      if( !pi.isWritable() )
      {
        //## todo: this is dicey
        if( JavaTypes.COLLECTION().isAssignableFrom(pi.getFeatureType()) )
        {
          addAll( contextValue, pi );
        }
        else if( JavaTypes.MAP().isAssignableFrom(pi.getFeatureType()) )
        {
          putAll( contextValue, pi );
        }
      }
      else
      {
        Object value = getValue( pi );
        pi.getAccessor().setValue( contextValue, value );
      }
    }
    else
    {
      throw new EvaluationException( "No writable property descriptor found for property, " + getPropertyName() +
                                     ", on class, " + TypeSystem.getUnqualifiedClassName( getOwnerType() ) );
    }
  }

  public IPropertyInfo getPropertyInfo() {
    try {
      return _pi != null ? _pi : BeanAccess.getPropertyInfo( getOwnerType(), getPropertyName(), null, null, null );
    } catch (ParseException e) {
      return null;
    }
  }

  private void putAll( Object contextValue, IPropertyInfo pi )
  {
    Map value = (Map)getValue( pi );
    Map existingMap = (Map)pi.getAccessor().getValue( contextValue );
    if( existingMap != null )
    {
      //noinspection unchecked
      existingMap.putAll( value );
    }
    else
    {
      throw new EvaluationException( getPropertyName() +
                                     ", on class, " + TypeSystem.getUnqualifiedClassName( getOwnerType() ) +
                                     "is not writable and does not have a current Map value to add values to." );
    }
  }

  private void addAll( Object contextValue, IPropertyInfo pi )
  {
    Collection value = (Collection)getValue( pi );
    Collection existingCollection = null;
    if( pi.isReadable() )
    {
      existingCollection = (Collection)pi.getAccessor().getValue( contextValue );
    }
    if( existingCollection != null )
    {
      //noinspection unchecked
      existingCollection.addAll( value );
    }
    else
    {
      throw new EvaluationException( getPropertyName() +
                                     ", on class, " + TypeSystem.getUnqualifiedClassName( getOwnerType() ) +
                                     "is not writable and does not have a current Collection value to add values to." );
    }
  }

  private Object getValue( IPropertyInfo pi )
  {
    Object value;
    try
    {
      value = _rhs.evaluate();
    }
    catch( Exception e )
    {
      throw GosuExceptionUtil.forceThrow( e );
    }
    value = CommonServices.getCoercionManager().convertValue( value, pi.getFeatureType() );
    return value;
  }

  public void setPropertyInfo( IPropertyInfo propertyInfo )
  {
    _pi = propertyInfo;
  }
}
