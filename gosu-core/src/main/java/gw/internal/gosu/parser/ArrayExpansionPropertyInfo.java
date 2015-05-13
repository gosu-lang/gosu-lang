/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.config.CommonServices;
import gw.internal.gosu.parser.statements.LoopStatement;
import gw.lang.parser.IExpansionPropertyInfo;
import gw.lang.reflect.IAnnotationInfo;
import gw.lang.reflect.IFeatureInfo;
import gw.lang.reflect.IPresentationInfo;
import gw.lang.reflect.IPropertyAccessor;
import gw.lang.reflect.IPropertyInfo;
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
public class ArrayExpansionPropertyInfo implements IExpansionPropertyInfo
{
  private IPropertyInfo _delegate;
  private IPropertyAccessor _arrayExpansionAccessor;

  public ArrayExpansionPropertyInfo( IPropertyInfo delegate )
  {
    _delegate = delegate;
  }

  public boolean isReadable()
  {
    return _delegate.isReadable();
  }

  public boolean isWritable( IType whosAskin )
  {
    return false;
  }

  public boolean isWritable()
  {
    return isWritable( null );
  }

  public IPropertyAccessor getAccessor()
  {
    if( _arrayExpansionAccessor == null )
    {
      _arrayExpansionAccessor = new ArrayExpansionAccessor();
    }
    return _arrayExpansionAccessor;
  }

  public IPresentationInfo getPresentationInfo()
  {
    return IPresentationInfo.Default.GET;
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

  public List<IAnnotationInfo> getAnnotationsOfType( IType type )
  {
    return Collections.emptyList();
  }

  @Override
  public IAnnotationInfo getAnnotation( IType type )
  {
    return null;
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

  /**
   * Make an array type from the delegate's type, if it's not already an array.
   * The idea with array expansion is to allow access to properties of X from an
   * array of X. We call the accessor For each X in the array we call the
   */
  public IType getFeatureType()
  {
    if( _delegate.getFeatureType().isArray() )
    {
      return _delegate.getFeatureType();
    }
    return _delegate.getFeatureType().getArrayType();
  }

  public IPropertyInfo getDelegate()
  {
    return _delegate;
  }

  class ArrayExpansionAccessor implements IPropertyAccessor
  {
    public Object getValue( Object ctx )
    {
      IPropertyAccessor accessor = _delegate.getAccessor();
      if( ctx == null )
      {
        return null;
      }
      IType type = _delegate.getFeatureType();
      IType objArrayType = TypeSystem.getFromObject( ctx );
      boolean bArray = type.isArray();
      List results = new ArrayList();
      for( Iterator iter = LoopStatement.makeIterator( ctx, objArrayType ); iter.hasNext(); )
      {
        Object elem = iter.next();
        Object value = elem == null ? null : accessor.getValue( elem );
        if( value != null && bArray )
        {
          results.addAll( (Collection)CommonServices.getCoercionManager().convertValue( value, JavaTypes.ARRAY_LIST() ) );
        }
        else
        {
          results.add( value );
        }
      }
      return CommonServices.getCoercionManager().convertValue( results, getFeatureType() );
    }

    public void setValue( Object ctx, Object value )
    {
      throw new UnsupportedOperationException( "Array expansion properties are not writable." );
    }
  }
}
