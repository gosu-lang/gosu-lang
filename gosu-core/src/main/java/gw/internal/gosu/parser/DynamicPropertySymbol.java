/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.lang.parser.IDynamicFunctionSymbol;
import gw.lang.parser.IDynamicPropertySymbol;
import gw.lang.parser.IReducedDynamicPropertySymbol;
import gw.lang.parser.IScriptPartId;
import gw.lang.parser.ISymbol;
import gw.lang.reflect.IPropertyInfo;
import gw.lang.reflect.IRelativeTypeInfo;
import gw.lang.reflect.IType;
import gw.lang.reflect.ITypeInfo;
import gw.lang.reflect.gs.IGosuClass;

import java.util.List;

/**
 */
public class DynamicPropertySymbol extends AbstractDynamicSymbol implements IDynamicPropertySymbol
{
  private DynamicPropertySymbol _dpsParent;
  DynamicFunctionSymbol _dfsGetter;
  DynamicFunctionSymbol _dfsSetter;
  private String _varIdentifier;

  public DynamicPropertySymbol( DynamicFunctionSymbol dfsGetterOrSetter, boolean bGetter )
  {
    super( dfsGetterOrSetter.getSymbolTable(),
           dfsGetterOrSetter.getDisplayName().substring( 1 ),
           bGetter
           ? dfsGetterOrSetter.getReturnType()
           : dfsGetterOrSetter.getArgs().size() > 0
             ? dfsGetterOrSetter.getArgs().get( 0 ).getType()
             : ErrorType.getInstance() );
    _dfsGetter = bGetter ? dfsGetterOrSetter : null;
    _dfsSetter = bGetter ? null : dfsGetterOrSetter;
    setModifierInfo( dfsGetterOrSetter.getModifierInfo() );
    setClassMember( true );
    setName( (String)getName() );
    _scriptPartId = dfsGetterOrSetter._scriptPartId;
  }

  public DynamicPropertySymbol( DynamicPropertySymbol dps )
  {
    super( dps.getSymbolTable(), dps.getName(), dps.getType() );
    _dfsGetter = dps._dfsGetter;
    _dfsSetter = dps._dfsSetter;
    replaceModifierInfo(dps.getModifierInfo());
    setClassMember( true );
    _scriptPartId = dps._scriptPartId;
  }

  public ISymbol getLightWeightReference()
  {
    return this;
  }


  public Object getValue()
  {
    if( getGetterDfs() == null )
    {
      throw new IllegalStateException( "No getter available." );
    }
    return getGetterDfs().invoke( null );
  }

  public Object getValueDirectly()
  {
    return getValue();
  }

  public void setValue( Object value )
  {
    if( getSetterDfs() == null )
    {
      throw new IllegalStateException( "No setter available." );
    }
    getSetterDfs().invoke( new Object[]{value} );
  }

  public void setValueDirectly( Object value )
  {
    setValue( value );
  }

  public boolean isReadable()
  {
    return getGetterDfs() != null;
  }

  public boolean isWritable()
  {
    return getSetterDfs() != null;
  }

  public void clearDebugInfo()
  {
    super.clearDebugInfo();
    if( _dfsGetter != null )
    {
      _dfsGetter.clearDebugInfo();
    }
    if( _dfsSetter != null )
    {
      _dfsSetter.clearDebugInfo();
    }
    if( _dpsParent != null &&
        _dpsParent.getGosuClass() == getGosuClass() )
    {
      _dpsParent.clearDebugInfo();
    }
  }

  public boolean isPublic()
  {
    return getGetterDfs() == null
           ? super.isPublic()
           : getGetterDfs().isPublic();
  }

  public boolean isPrivate()
  {
    return getGetterDfs() == null
           ? super.isPrivate()
           : getGetterDfs().isPrivate();
  }

  public boolean isInternal()
  {
    return getGetterDfs() == null
           ? super.isInternal()
           : getGetterDfs().isInternal();
  }

  public boolean isProtected()
  {
    return getGetterDfs() == null
           ? super.isProtected()
           : getGetterDfs().isProtected();
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

  public void setGetterDfs( IDynamicFunctionSymbol dfsGetter )
  {
    _dfsGetter = (DynamicFunctionSymbol) dfsGetter;
    setModifierInfo( dfsGetter.getModifierInfo() );
    setInternal( _dfsGetter.isInternal() );
    setPrivate( _dfsGetter.isPrivate() );
    setProtected( _dfsGetter.isProtected() );
    setPublic( _dfsGetter.isPublic() );
  }

  public DynamicFunctionSymbol getGetterDfs()
  {
    return _dfsGetter == null
           ? _dpsParent != null
             ? _dpsParent.getGetterDfs()
             : null
           : _dfsGetter;
  }

  protected DynamicFunctionSymbol getImmediateGetterDfs()
  {
    return _dfsGetter;
  }

  public void setSetterDfs( IDynamicFunctionSymbol dfsSetter )
  {
    _dfsSetter = (DynamicFunctionSymbol) dfsSetter;
  }

  public DynamicFunctionSymbol getSetterDfs()
  {
    return _dfsSetter == null
           ? _dpsParent != null
             ? _dpsParent.getSetterDfs()
             : null
           : _dfsSetter;
  }

  protected DynamicFunctionSymbol getImmediateSetterDfs()
  {
    return _dfsSetter;
  }

  protected void setParent( DynamicPropertySymbol dpsParent )
  {
    _dpsParent = dpsParent;
  }

  public DynamicPropertySymbol getParent()
  {
    return _dpsParent;
  }

  public DynamicFunctionSymbol getFunction( String strFunctionName )
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

  private boolean functionNamesEqual( DynamicFunctionSymbol dfs, String strFunctionName )
  {
    return dfs != null &&
           (dfs.getName().equals( strFunctionName ) ||
            dfs.getName().toLowerCase().contains( "_duplicate_" + strFunctionName.toLowerCase() ) );
  }

  public void addMemberSymbols( ICompilableTypeInternal gsClass )
  {
    gsClass.getParseInfo().addMemberProperty( this );

    if( _dfsGetter != null && _dfsGetter.getGosuClass() == gsClass )
    {
      gsClass.getParseInfo().addMemberFunction( _dfsGetter );
    }

    if( _dfsSetter != null && _dfsSetter.getGosuClass() == gsClass )
    {
      gsClass.getParseInfo().addMemberFunction( _dfsSetter );
    }
  }

  public void setVarIdentifier( String varIdentifier )
  {
    _varIdentifier = varIdentifier;
  }

  public String getVarIdentifier()
  {
    return _varIdentifier;
  }

  public String getFullDescription()
  {
    return getModifierInfo() == null ? "" : getModifierInfo().getDescription();
  }

  public DynamicPropertySymbol getParameterizedVersion( IGosuClass gsClass )
  {
    return new ParameterizedDynamicPropertySymbol( this, (IGosuClassInternal)gsClass );
  }

  public void clearDefn() {
    if (_dfsGetter != null) {
      _dfsGetter.clearDefn();
    }

    if (_dfsSetter != null) {
      _dfsSetter.clearDefn();
    }
  }

  /*
   * We must copy the annotation information down to the getter and setter, since those annotations
   * need to head out to bytecode land
   */
  public void updateAnnotations(List<IGosuAnnotation> annotations) {
    getModifierInfo().setAnnotations(annotations);
    if (_dfsGetter != null) {
      _dfsGetter.getModifierInfo().setAnnotations(annotations);
    }
    if (_dfsSetter != null) {
      _dfsSetter.getModifierInfo().setAnnotations(annotations);
    }
  }
  
  public IReducedDynamicPropertySymbol createReducedSymbol() {
    return new ReducedDynamicPropertySymbol( this );
  }

  @Override
  public IPropertyInfo getPropertyInfo()
  {
    IScriptPartId scriptPart = getScriptPart();
    IType declaringType = scriptPart == null ? null : scriptPart.getContainingType();
    if( declaringType == null )
    {
      return null;
    }

    ITypeInfo typeInfo = declaringType.getTypeInfo();

    List<? extends IPropertyInfo> properties;
    if( typeInfo instanceof IRelativeTypeInfo )
    {
      properties = ((IRelativeTypeInfo)typeInfo).getProperties( declaringType );
    }
    else
    {
      properties = typeInfo.getProperties();
    }
    for( IPropertyInfo pi : properties )
    {
      if( pi.getName().equals( this.getName() ) )
      {
        return pi;
      }
    }
    return null;
  }
}
