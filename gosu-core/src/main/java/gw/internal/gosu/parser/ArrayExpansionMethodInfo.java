/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.config.CommonServices;
import gw.internal.gosu.parser.statements.LoopStatement;
import gw.lang.reflect.IAnnotationInfo;
import gw.lang.reflect.IExceptionInfo;
import gw.lang.reflect.IFeatureInfo;
import gw.lang.reflect.IMethodCallHandler;
import gw.lang.reflect.IMethodInfo;
import gw.lang.reflect.IParameterInfo;
import gw.lang.reflect.IScriptabilityModifier;
import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.java.JavaTypes;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 */
public class ArrayExpansionMethodInfo implements IMethodInfo
{
  private IMethodInfo _delegate;
  private IMethodCallHandler _callHandler;

  public ArrayExpansionMethodInfo( IMethodInfo delegate )
  {
    _delegate = delegate;
  }

  public IMethodCallHandler getCallHandler()
  {
    return _callHandler == null
           ? _callHandler = new ArrayExpansionCallHandler()
           : _callHandler;
  }

  public List<IExceptionInfo> getExceptions()
  {
    return _delegate.getExceptions();
  }

  public IParameterInfo[] getParameters()
  {
    return _delegate.getParameters();
  }

  public String getReturnDescription()
  {
    return _delegate.getReturnDescription();
  }

  public IType getReturnType()
  {
    IType type = _delegate.getReturnType();
    if( type.isArray() || type == JavaTypes.pVOID() )
    {
      return type;
    }
    return type.getArrayType();
  }

  public boolean isScriptable()
  {
    return _delegate.isScriptable();
  }

  public boolean isDeprecated()
  {
    return _delegate.isDeprecated();
  }

  public String getDeprecatedReason() {
    return _delegate.getDeprecatedReason();
  }

  @Override
  public boolean isDefaultImpl() {
    return _delegate.isDefaultImpl();
  }

  public boolean isVisible( IScriptabilityModifier constraint )
  {
    return _delegate.isVisible( constraint );
  }

  public boolean isHidden()
  {
    return _delegate.isHidden();
  }

  public boolean isStatic()
  {
    return _delegate.isStatic();
  }

  public boolean isPrivate()
  {
    return _delegate.isPrivate();
  }

  public boolean isInternal()
  {
    return _delegate.isInternal();
  }

  public boolean isProtected()
  {
    return _delegate.isProtected();
  }

  public boolean isPublic()
  {
    return _delegate.isPublic();
  }

  public boolean isAbstract()
  {
    return _delegate.isAbstract();
  }

  public boolean isFinal()
  {
    return _delegate.isFinal();
  }

  public List<IAnnotationInfo> getAnnotations()
  {
    return Collections.emptyList();
  }

  public List<IAnnotationInfo> getDeclaredAnnotations()
  {
    return Collections.emptyList();
  }

  @Override
  public IAnnotationInfo getAnnotation( IType type )
  {
    return null;
  }

  public List<IAnnotationInfo> getAnnotationsOfType( IType type )
  {
    return Collections.emptyList();
  }

  public boolean hasAnnotation( IType type )
  {
    return false;
  }

  @Override
  public boolean hasDeclaredAnnotation( IType type )
  {
    return false;
  }

  public IFeatureInfo getContainer()
  {
    return _delegate.getContainer();
  }

  public IType getOwnersType()
  {
    return _delegate.getOwnersType();
  }

  public String getName()
  {
    return _delegate.getName();
  }

  public String getDisplayName()
  {
    return _delegate.getDisplayName();
  }

  public String getDescription()
  {
    return _delegate.getDescription();
  }

  public IMethodInfo getDelegate()
  {
    return _delegate;
  }


  class ArrayExpansionCallHandler implements IMethodCallHandler
  {
    public Object handleCall( Object ctx, Object... args )
    {
      IMethodCallHandler callHandler = _delegate.getCallHandler();
      if( ctx == null )
      {
        return null;
      }
      IType type = _delegate.getReturnType();
      IType objArrayType = TypeSystem.getFromObject( ctx );
      boolean bArray = type.isArray();
      List<Object> results = new ArrayList<Object>();
      for( Iterator iter = LoopStatement.makeIterator( ctx, objArrayType ); iter.hasNext(); )
      {
        Object elem = iter.next();
        Object value = elem == null ? null : callHandler.handleCall( elem, args );
        if( type != JavaTypes.pVOID() )
        {
          if( value != null && bArray )
          {
            //noinspection unchecked
            results.addAll( (Collection)CommonServices.getCoercionManager().convertValue( value, JavaTypes.ARRAY_LIST() ) );
          }
          else
          {
            results.add( value );
          }
        }
      }
      return type != JavaTypes.pVOID() ? coerceValue( getReturnType(), results ) : null;
    }

    public Object coerceValue( IType typeToCoerceTo, List<Object> list )
    {
      if( list == null )
      {
        return null;
      }
      Object returnArray = typeToCoerceTo.getComponentType().makeArrayInstance( list.size() );
      int i = 0;
      for( Object val : list )
      {
        typeToCoerceTo.setArrayComponent( returnArray, i++, CommonServices.getCoercionManager().convertValue( val, typeToCoerceTo.getComponentType() ) );
      }
      return returnArray;
    }
  }
}
