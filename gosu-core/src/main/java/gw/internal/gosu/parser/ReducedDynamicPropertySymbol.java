/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.lang.parser.*;

import java.util.ArrayList;
import java.util.List;

/**
 */
public class ReducedDynamicPropertySymbol extends ReducedSymbol implements IReducedDynamicPropertySymbol
{
  private ReducedDynamicPropertySymbol _dpsParent;
  ReducedDynamicFunctionSymbol _dfsGetter;
  ReducedDynamicFunctionSymbol _dfsSetter;
  private String _varIdentifier;

  protected ReducedDynamicPropertySymbol(DynamicPropertySymbol dps)
  {
    super( dps );
    _dfsGetter = dps._dfsGetter == null ? null : (ReducedDynamicFunctionSymbol) dps._dfsGetter.createReducedSymbol();
    _dfsSetter = dps._dfsSetter == null ? null : (ReducedDynamicFunctionSymbol) dps._dfsSetter.createReducedSymbol();
    _dpsParent = dps.getParent() == null ? null : (ReducedDynamicPropertySymbol) dps.getParent().createReducedSymbol();
    _varIdentifier = dps.getVarIdentifier();
    _fullDescription = dps.getFullDescription();
  }

  public boolean isReadable()
  {
    return getGetterDfs() != null;
  }

  public boolean isWritable()
  {
    return getSetterDfs() != null;
  }

  public boolean isPublic()
  {
    return getGetterDfs() == null
           ? getSetterDfs() == null
             ? super.isPublic()
             : getSetterDfs().isPublic()
           : getSetterDfs() == null
             ? getGetterDfs().isPublic()
             : getGetterDfs().isPublic() ||
               getSetterDfs().isPublic();
  }

  public boolean isPrivate()
  {
    return getGetterDfs() == null
           ? getSetterDfs() == null
             ? super.isPrivate()
             : getSetterDfs().isPrivate()
           : getSetterDfs() == null
             ? getGetterDfs().isPrivate()
             : getGetterDfs().isPrivate() &&
               getSetterDfs().isPrivate();
  }

  public boolean isInternal()
  {
    return getGetterDfs() == null
           ? getSetterDfs() == null
             ? super.isInternal()
             : getSetterDfs().isInternal()
           : getSetterDfs() == null
             ? getGetterDfs().isInternal()
             : (getGetterDfs().isInternal() && (getSetterDfs().isInternal() || getSetterDfs().isPrivate())) ||
               (getSetterDfs().isInternal() && (getGetterDfs().isInternal() || getGetterDfs().isPrivate()));
  }

  public boolean isProtected()
  {
    return getGetterDfs() == null
           ? getSetterDfs() == null
             ? super.isProtected()
             : getSetterDfs().isProtected()
           : getSetterDfs() == null
             ? getGetterDfs().isProtected()
             : (getGetterDfs().isProtected() && !getSetterDfs().isPublic()) ||
               (getSetterDfs().isProtected() && !getGetterDfs().isPublic());
  }

  public boolean isStatic()
  {
    return getGetterDfs() == null
           ? super.isStatic()
           : getGetterDfs().isStatic();
  }

  public boolean isAbstract()
  {
    return getGetterDfs() == null
           ? super.isAbstract()
           : getGetterDfs().isAbstract();
  }

  public boolean isFinal()
  {
    return getGetterDfs() == null
           ? super.isFinal()
           : getGetterDfs().isFinal();
  }

  public ReducedDynamicFunctionSymbol getGetterDfs()
  {
    return _dfsGetter == null
           ? _dpsParent != null
             ? _dpsParent.getGetterDfs()
             : null
           : _dfsGetter;
  }

  protected ReducedDynamicFunctionSymbol getImmediateGetterDfs()
  {
    return _dfsGetter;
  }

  public ReducedDynamicFunctionSymbol getSetterDfs()
  {
    return _dfsSetter == null
           ? _dpsParent != null
             ? _dpsParent.getSetterDfs()
             : null
           : _dfsSetter;
  }

  protected ReducedDynamicFunctionSymbol getImmediateSetterDfs()
  {
    return _dfsSetter;
  }

  public ReducedDynamicPropertySymbol getParent()
  {
    return _dpsParent;
  }

  public ReducedDynamicFunctionSymbol getFunction( String strFunctionName )
  {
    if( functionNamesEqual( _dfsGetter, strFunctionName ) )
    {
      return _dfsGetter;
    }
    if( functionNamesEqual( _dfsSetter, strFunctionName ) )
    {
      return _dfsSetter;
    }
    return null;
  }

  private boolean functionNamesEqual( ReducedDynamicFunctionSymbol dfs, String strFunctionName )
  {
    return dfs != null && dfs.getName().equals( strFunctionName );
  }

  public String getVarIdentifier()
  {
    return _varIdentifier;
  }
//
//  public List<IGosuAnnotation> getAnnotations()
//  {
//    return getModifierInfo().getAnnotations();
//  }
//
//  public String getFullDescription()
//  {
//    return getModifierInfo() == null ? "" : getModifierInfo().getDescription();
//  }

  public List<IGosuAnnotation> getAnnotations() {
    ArrayList<IGosuAnnotation> annotations = new ArrayList<IGosuAnnotation>();
    if( _dfsGetter != null )
    {
      annotations.addAll( _dfsGetter.getAnnotations() );
    }
    if( _dfsSetter != null && !pureVarBasedProperty() )
    {
      annotations.addAll( _dfsSetter.getAnnotations() );
    }
    return annotations;
  }

  private boolean pureVarBasedProperty()
  {
    return (_dfsGetter != null && _dfsGetter.isVarPropertyGet()) &&
            (_dfsSetter != null && _dfsSetter.isVarPropertySet());
  }

}
