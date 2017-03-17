/*
 * Copyright 2014 Guidewire Software, Inc.
 */
package gw.internal.gosu.parser;

import gw.lang.function.IBlock;
import gw.lang.parser.ICapturedSymbol;
import gw.lang.parser.IExpression;
import gw.lang.parser.IFunctionSymbol;
import gw.lang.parser.IReducedSymbol;
import gw.lang.parser.IScope;
import gw.lang.parser.IScriptPartId;
import gw.lang.parser.IStackProvider;
import gw.lang.parser.ISymbol;
import gw.lang.parser.ISymbolTable;
import gw.lang.parser.Keyword;
import gw.lang.reflect.IModifierInfo;
import gw.lang.reflect.IType;
import gw.lang.reflect.Modifier;
import gw.lang.reflect.gs.IGosuClass;
import gw.util.GosuExceptionUtil;
import gw.util.MutableBoolean;

import java.lang.reflect.Method;
import java.util.List;

/**
 * Base class for all symbols in the symbol table.
 */
public class Symbol implements IFunctionSymbol
{
  public static final IStackProvider MEMBER_STACK_PROVIDER = new MemberStackProvider();

  private String _name;
  private IType _type;
  protected Object _value;
  private IExpression _defaultValue;
  protected int _iIndex;
  protected boolean _bGlobal;
  protected IStackProvider _stackProvider;
  protected ISymbolTable _symbolTable;
  private MutableBoolean _valueIsBoxed;
  private ModifierInfo _modifiers;

  public Symbol( String strName, IType type, IStackProvider stackProvider )
  {
    this( strName, type, stackProvider, null, null );
  }

  public Symbol( String strName, IType type, IStackProvider stackProvider, Object value )
  {
    this( strName, type, stackProvider, value, null );
  }

  public Symbol( String strName, IType type, IStackProvider stackProvider, Object value, IScope scope )
  {
    setName( strName );
    _modifiers = new ModifierInfo(0);
    _type = type;
    _stackProvider = stackProvider;
    _value = value;
    _iIndex = assignIndex( scope );
    _bGlobal = _stackProvider == null || !_stackProvider.hasIsolatedScope();
    _valueIsBoxed = new MutableBoolean();
  }

  public Symbol( Symbol copy )
  {
    setName( copy._name );
    _type = copy._type;
    _iIndex = copy._iIndex;
    _bGlobal = copy._bGlobal;
    _stackProvider = copy._stackProvider;
    _symbolTable = copy._symbolTable;
    // We need to use a MutableBoolean here so that modifications are set on all copies.
    _valueIsBoxed = copy._valueIsBoxed;
    _modifiers = copy._modifiers;
  }

  /*
   * Symbols created via these ctors go to the symbol table, they are not stack based.
   */
  public Symbol( String strName, IType type, Object value )
  {
    this( strName, type, null, value, null );
  }

  public void setDynamicSymbolTable( ISymbolTable symTable )
  {
    if( _stackProvider == null )
    {
      _symbolTable = symTable;
    }
  }

  public boolean hasDynamicSymbolTable()
  {
    return _symbolTable != null;
  }
  
  public ISymbolTable getDynamicSymbolTable()
  {
    return _symbolTable;
  }
  
  protected int assignIndex( IScope scope )
  {
    return assignIndexInStack( scope );
  }

  protected int assignIndexInStack( IScope scope )
  {
    if( THIS.equals( _name ) )
    {
      return IStackProvider.THIS_POS;
    }
    if( SUPER.equals( _name ) )
    {
      return IStackProvider.SUPER_POS;
    }

    if( _stackProvider == null )
    {
      return -1;
    }
    else
    {
      if( scope == null )
      {
        return _stackProvider.getNextStackIndex();
      }
      else
      {
        return _stackProvider.getNextStackIndexForScope( scope );
      }
    }
  }

  /**
   * Returns the Symbol's name.
   */
  public String getName()
  {
    return _name;
  }

  /**
   * Returns the Symbol's optional display name.  If a display name is not assigned,
   * returns the symbol's name.
   */
  public String getDisplayName()
  {
    return getName();
  }

  @Override
  public String getFullDescription()
  {
    return null;
  }

  public void renameAsErrantDuplicate( int iIndex )
  {
    setName( (String)(iIndex + "_duplicate_" + getName()) );
  }

  /**
   * Returns the Symbol's type.
   */
  public IType getType()
  {
    return _type;
  }

  /**
   * Sets the Symbol's type.
   */
  public void setType( IType type )
  {
    _type = type;
  }

  /**
   * Returns the value assigned to this Symbol.
   */
  public Object getValue()
  {
    if( _symbolTable != null )
    {
      return getValueFromSymbolTable();
    }

    return _value;
  }

  private Object getValueFromSymbolTable()
  {
    ISymbol symbol = _symbolTable.getSymbol( _name );
    if( symbol instanceof Symbol )
    {
      return ((Symbol)symbol).getValueDirectly();
    }
    if( symbol != null )
    {
      return symbol.getValue();
    }
    return _value;
  }

  /**
   * Assigns a value to this Symbol.
   */
  public void setValue( Object value )
  {
    if( _symbolTable != null )
    {
      setValueFromSymbolTable( value );
    }
    else
    {
      _value = value;
    }
  }

  public IExpression getDefaultValueExpression()
  {
    return _defaultValue;
  }
  public void setDefaultValueExpression( IExpression defaultValue )
  {
    _defaultValue = defaultValue;
  }

  public boolean isStackSymbol()
  {
    return _stackProvider != null && _stackProvider != MEMBER_STACK_PROVIDER && !_bGlobal;
  }

  private void setValueFromSymbolTable( Object value )
  {
    ISymbol symbol = _symbolTable.getSymbol( _name );
    ((Symbol)symbol).setValueDirectly( value );
  }

  public Object getValueDirectly()
  {
    return _value;
  }

  public void setValueDirectly( Object value )
  {
    _value = value;
  }


  /**
   * Invokes function.
   */
  public Object invoke( Object[] args )
  {
    Object value = getValue();
    if( value instanceof ISymbol )
    {
      return ((Symbol)value).invoke( args );
    }
    if( value instanceof IBlock )
    {
      return ((IBlock) value).invokeWithArgs( args );
    }

    Method method = (Method)value;
    Object ret;
    try
    {
      ret = method.invoke( null, args );
    }
    catch( Exception e )
    {
      throw GosuExceptionUtil.forceThrow( e );
    }
    return ret;
  }

  public ISymbol getLightWeightReference()
  {
    if( _symbolTable == null &&
        _stackProvider == null || _stackProvider == MEMBER_STACK_PROVIDER )
    {
      return this;
    }
    return new Symbol( this );
  }

  public boolean isImplicitlyInitialized() {
    return false;
  }

  public boolean isWritable()
  {
    return !isFinal();
  }

  public boolean equals( Object o )
  {
    if( this == o )
    {
      return true;
    }
    if( !(o instanceof Symbol) )
    {
      return false;
    }
    final Symbol symbol = (Symbol)o;
    if( !_name.equals( symbol._name ) )
    {
      return false;
    }
    if( _type != null ? !_type.equals( symbol._type ) : symbol._type != null )
    {
      return false;
    }
    return !(_value != null ? !_value.equals( symbol._value ) : symbol._value != null);
  }

  public String getSignatureDescription()
  {
    return null;
  }

  public int getIndex()
  {
    return _iIndex;
  }

  public boolean isClassMember()
  {
    return Modifier.isClassMember( _modifiers.getModifiers() );
  }

  public void setClassMember( boolean bClassMember )
  {
    _modifiers.setModifiers( Modifier.setClassMember( _modifiers.getModifiers(), bClassMember ) );
  }

  public boolean isStatic()
  {
    return Modifier.isStatic( _modifiers.getModifiers() );
  }

  public void setStatic( boolean bStatic )
  {
    _modifiers.setModifiers( Modifier.setStatic( _modifiers.getModifiers(), bStatic ) );
  }

  public boolean isPrivate()
  {
    return Modifier.isPrivate( _modifiers.getModifiers() );
  }

  public void setPrivate( boolean bPrivate )
  {
    _modifiers.setModifiers( Modifier.setPrivate( _modifiers.getModifiers(), bPrivate ) );
  }

  public boolean isInternal()
  {
    return Modifier.isInternal( _modifiers.getModifiers() );
  }

  public void setInternal( boolean bInternal )
  {
    _modifiers.setModifiers( Modifier.setInternal( _modifiers.getModifiers(), bInternal ) );
  }

  public boolean isProtected()
  {
    return Modifier.isProtected( _modifiers.getModifiers() );
  }

  public void setProtected( boolean bProtected )
  {
    _modifiers.setModifiers( Modifier.setProtected( _modifiers.getModifiers(), bProtected ) );
  }

  public boolean isPublic()
  {
    return Modifier.isPublic( _modifiers.getModifiers() ) || (!isPrivate() && !isProtected() && !isInternal());
  }

  public void setPublic( boolean bPublic )
  {
    _modifiers.setModifiers( Modifier.setPublic( _modifiers.getModifiers(), bPublic ) );
  }

  public boolean isAbstract()
  {
    return Modifier.isAbstract( _modifiers.getModifiers() );
  }
  public void setAbstract( boolean bAbstract )
  {
    _modifiers.setModifiers( Modifier.setAbstract( _modifiers.getModifiers(), bAbstract ) );
  }

  public boolean isFinal()
  {
    return Modifier.isFinal( _modifiers.getModifiers() );
  }
  public void setFinal( boolean bFinal )
  {
    _modifiers.setModifiers( Modifier.setFinal( _modifiers.getModifiers(), bFinal ) );
  }

  public boolean isReified()
  {
    return Modifier.isReified( _modifiers.getModifiers() );
  }
  public void setReified( boolean bReified )
  {
    _modifiers.setModifiers( Modifier.setReified( _modifiers.getModifiers(), bReified ) );
  }

  public boolean isOverride()
  {
    return Modifier.isOverride( _modifiers.getModifiers() );
  }
  public void setOverride( boolean bOverride )
  {
    _modifiers.setModifiers( Modifier.setOverride( _modifiers.getModifiers(), bOverride ) );
  }

  public boolean isHide()
  {
    return Modifier.isHide( _modifiers.getModifiers() );
  }
  public void setHide( boolean bHide )
  {
    _modifiers.setModifiers( Modifier.setHide( _modifiers.getModifiers(), bHide ) );
  }

  public ModifierInfo getModifierInfo()
  {
    return _modifiers;
  }
  public void setModifierInfo( IModifierInfo modifiers )
  {
    if( _modifiers == null )
    {
      _modifiers = (ModifierInfo) modifiers;
    }
    else
    {
      _modifiers.update((ModifierInfo) modifiers);
    }
  }
  public void replaceModifierInfo( IModifierInfo mi )
  {
    _modifiers = (ModifierInfo) mi;
  }

  public int getModifiers()
  {
    return _modifiers.getModifiers();
  }

  public List<IGosuAnnotation> getAnnotations()
  {
    return getModifierInfo().getAnnotations();
  }

  @Override
  public IScriptPartId getScriptPart() {
    return null;
  }

  @Override
  public IGosuClass getGosuClass() {
    return null;
  }

  @Override
  public boolean hasTypeVariables() {
    return false;
  }

  public void setModifiers( int modifiers )
  {
    _modifiers.setModifiers( modifiers );
  }

  public String toString()
  {
    Object value;
    try
    {
      value = getValue();
    }
    catch( Exception e )
    {
      value = "Undefined";
    }
    return getName() + " : " + getType() + ": " + " = " + value;
  }

  public boolean canBeCaptured()
  {
    return isStackSymbol() &&
           !getName().equals( Keyword.KW_this.toString() ) &&
           !getName().equals( Keyword.KW_super.toString() );
  }

  public ICapturedSymbol makeCapturedSymbol( String strName, ISymbolTable symbolTable, IScope scope )
  {
    return new CapturedSymbol( strName, this, symbolTable, scope );
  }

  public void setIndex( int i )
  {
    _iIndex = i;
  }

  /** */
  private static class MemberStackProvider implements IStackProvider
  {
    public int getNextStackIndex()
    {
      return -1;
    }

    public int getNextStackIndexForScope( IScope scope )
    {
      return -1;
    }

    public boolean hasIsolatedScope()
    {
      return true;
    }
  }

  public void setValueIsBoxed( boolean b )
  {
    _valueIsBoxed.setValue( b );
  }
  public boolean isValueBoxed()
  {
    return _valueIsBoxed.isTrue();
  }

  protected void setName( String name )
  {
    _name = name;
  }

  @Override
  public boolean isLocal()
  {
    return _iIndex >= 0;
  }

  @Override
  public boolean isFromJava()
  {
    return false;
  }

  public Class getSymbolClass()
  {
    return getClass();
  }
  
  public IReducedSymbol createReducedSymbol()
  {
    return new ReducedSymbol(this);
  }
}
