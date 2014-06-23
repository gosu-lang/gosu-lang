/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.internal.gosu.parser.statements.VarStatement;
import gw.lang.parser.GlobalScope;
import gw.lang.parser.TypeVarToTypeMap;
import gw.lang.parser.expressions.INewExpression;
import gw.lang.parser.expressions.IVarStatement;
import gw.lang.reflect.*;
import gw.lang.reflect.gs.IGosuVarPropertyInfo;
import gw.lang.reflect.gs.IGosuClass;
import gw.lang.reflect.java.JavaTypes;

import java.util.Collections;
import java.util.List;
import java.lang.reflect.Field;

/**
 */
public class GosuVarPropertyInfo extends GosuBaseAttributedFeatureInfo implements IGosuVarPropertyInfo
{
  private String _strIdentifer;
  private IType _actualType;
  private boolean _bStatic;
  private boolean _bPublic;
  private boolean _bProtected;
  private boolean _bInternal;
  private boolean _bPrivate;
  private IPropertyAccessor _accessor;
  private String _fullDescription;
  private boolean _hasProperty;
  private boolean _isScopedField;
  private IGosuClass _gosuClass;
  private boolean _isFinal;
  private IType _symbolType;
  private String _symbolScopeString;
  private String _symbolAttributeName;
  private int _modifiers;

  GosuVarPropertyInfo( IFeatureInfo container, IVarStatement varStmt )
  {
    super( container );
    _strIdentifer = varStmt.getIdentifierName();
    _actualType = assignActualType(varStmt.getType());
    _bStatic = varStmt.isStatic();
    _bPublic = varStmt.isPublic();
    _bProtected = varStmt.isProtected();
    _bInternal = varStmt.isInternal();
    _bPrivate = varStmt.isPrivate();
    _fullDescription = varStmt.getFullDescription();
    _hasProperty = varStmt.hasProperty();
    _isScopedField = varStmt.getScope() == GlobalScope.REQUEST || varStmt.getScope() == GlobalScope.SESSION;
    _symbolType = varStmt.getSymbol().getType();
    if (_isScopedField) {
      ScopedDynamicSymbol symbol = (ScopedDynamicSymbol) varStmt.getSymbol();
      _symbolScopeString = symbol.getScope().toString();
      _symbolAttributeName = symbol.getAttributeName();
    }
    _gosuClass = varStmt.getParent() != null ? varStmt.getParent().getGosuClass() : null;
    _isFinal = varStmt.isFinal();
    ModifierInfo modifierInfo = ((VarStatement) varStmt).getModifierInfo();
    _modifiers = modifierInfo.getModifiers();
    ((GosuClassTypeInfo)getOwnersType().getTypeInfo()).setModifierInfo( this, modifierInfo );
  }

  public GosuVarPropertyInfo(GosuClassTypeInfo container, GosuVarPropertyInfo pi) {
    super( container );
    _strIdentifer = pi._strIdentifer;
    _actualType = assignActualType( pi._actualType );
    _bStatic = pi._bStatic;
    _bPublic = pi._bPublic;
    _bProtected = pi._bProtected;
    _bInternal = pi._bInternal;
    _bPrivate = pi._bPrivate;
    _fullDescription = pi._fullDescription;
    _hasProperty = pi._hasProperty;
    _isScopedField = pi._isScopedField;
    _symbolScopeString = pi._symbolScopeString;
    _symbolAttributeName = pi._symbolAttributeName;
    _symbolType = pi._symbolType;
    _gosuClass = pi._gosuClass;
    _isFinal = pi._isFinal;
    _modifiers = pi._modifiers;
    GosuClassTypeInfo ti = (GosuClassTypeInfo)getOwnersType().getTypeInfo();
    ti.setModifierInfo( this, ti.getModifierInfo( pi ) );
  }

  public String getName()
  {
    return _strIdentifer == null ? null : _strIdentifer.toString();
  }

  public IType getFeatureType()
  {
    if( _actualType == JavaTypes.pVOID() )
    {
      getOwnersType().isValid();
      _actualType = assignActualType( _actualType );
    }
    return _actualType;
  }

  public boolean isStatic()
  {
    return _bStatic;
  }

  public boolean isPrivate()
  {
    return _bPrivate;
  }

  public boolean isInternal()
  {
    return _bInternal;
  }

  public boolean isProtected()
  {
    return _bProtected;
  }

  @Override
  public boolean isFinal()
  {
    return _isFinal;
  }

  public boolean isPublic()
  {
    return _bPublic || (!isPrivate() && !isInternal() && !isProtected());
  }

  protected List<IGosuAnnotation> getGosuAnnotations()
  {
    IModifierInfo modifierInfo = ((GosuClassTypeInfo)getOwnersType().getTypeInfo()).getModifierInfo( this );
    return modifierInfo != null
           ? modifierInfo.getAnnotations()
           : Collections.<IGosuAnnotation>emptyList();
  }

  public boolean isReadable()
  {
    return true;
  }

  public boolean isWritable(IType whosAskin) {
    return !isFinal();
  }

  public boolean isWritable()
  {
    return isWritable( null ) && !isFinal();
  }

  public IPropertyAccessor getAccessor()
  {
    if( _accessor != null )
    {
      return _accessor;
    }
    return _accessor = new VarPropertyAccessor();
  }

  public IPresentationInfo getPresentationInfo()
  {
    return IPresentationInfo.Default.GET;
  }


  public IType assignActualType(IType type)
  {
    if( _actualType == null || _actualType == JavaTypes.pVOID() )
    {
      _actualType = getActualTypeInContainer( this, type );
    }
    return _actualType;
  }

  public void assignSymbolType( IType type )
  {
    if( _symbolType == null || _symbolType == JavaTypes.pVOID() )
    {
      _symbolType = type;
    }
  }

  public boolean hasDeclaredProperty()
  {
    return _hasProperty;
  }

  @Override
  public boolean isScopedField()
  {
    return _isScopedField;
  }

  @Override
  public IType getScopedSymbolType() {
    return _symbolType;
  }

  @Override
  public String getSymbolScopeString() {
    return _symbolScopeString;
  }

  @Override
  public String getSymbolAttributeName() {
    return _symbolAttributeName;
  }

  public GenericTypeVariable[] getTypeVariables()
  {
    return GenericTypeVariable.EMPTY_TYPEVARS;
  }

  public IType getParameterizedReturnType( IType... typeParams )
  {
    return null;
  }

  public IType[] getParameterizedParameterTypes( IType... typeParams )
  {
    return IType.EMPTY_ARRAY;
  }
  public IType[] getParameterizedParameterTypes2( IGosuClass ownersType, IType... typeParams )
  {
    return IType.EMPTY_ARRAY;
  }

  public TypeVarToTypeMap inferTypeParametersFromArgumentTypes( IType... argTypes )
  {
    return null;
  }

  @Override
  public TypeVarToTypeMap inferTypeParametersFromArgumentTypes2( IGosuClass owningParameterizedType, IType... argTypes )
  {
    return null;
  }

  @Override
  public boolean isCompileTimeConstantValue()
  {
    return getCompileTimeConstantExpression() != null;
  }

  @Override
  public Object doCompileTimeEvaluation()
  {
    Expression expr = getCompileTimeConstantExpression();
    return expr instanceof INewExpression
           ? getName() // Enum constant field name
           : expr.evaluate();
  }

  private Expression getCompileTimeConstantExpression()
  {
    // Must be static constant
    if( !isStatic() || !isFinal() )
    {
      return null;
    }

    IType type = getFeatureType();

    // Must be either primitive, String, or Enum constant
    if( type != JavaTypes.STRING() &&
        !type.isPrimitive() &&
        !type.isEnum() )
    {
      return null;
    }

    //
    // Field must be initialized directly
    //
    if( getOwnersType().isDeclarationsCompiled() )
    {
      getOwnersType().isValid(); // barf
    }
    for( VarStatement varStmt : getOwnersType().getParseInfo().getStaticFields().values() )
    {                                 // barf
      if( varStmt.getIdentifierName().toString().equals( getName() ) )
      {
        Expression initiazerExpr = varStmt.getAsExpression();
        if( initiazerExpr != null )
        {
          return varStmt.isEnumConstant() || initiazerExpr.isCompileTimeConstant()
                 ? initiazerExpr
                 : null;
        }
      }
    }

    return null;
  }

  private class VarPropertyAccessor implements IPropertyAccessor
  {
    public Object getValue( Object ctx )
    {
      IGosuClass gsClass = (IGosuClass)getContainer().getOwnersType();
      try
      {
        Field field = gsClass.getBackingClass().getDeclaredField( _strIdentifer );
        field.setAccessible( true );
        return field.get( ctx );
      }
      catch( Exception e )
      {
        throw new RuntimeException( e );
      }
    }

    public void setValue( Object ctx, Object value )
    {
      IGosuClass gsClass = _gosuClass;
      try
      {
        Field field = gsClass.getBackingClass().getDeclaredField( _strIdentifer );
        field.setAccessible( true );
        field.set( ctx, value );
      }
      catch( Exception e )
      {
        throw new RuntimeException( e );
      }
    }
  }

  public String getDescription()
  {
    return _fullDescription;
  }
}
