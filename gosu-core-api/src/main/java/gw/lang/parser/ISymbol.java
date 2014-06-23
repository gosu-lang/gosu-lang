/*
 * Copyright 2014 Guidewire Software, Inc.
 */
package gw.lang.parser;

import gw.lang.reflect.IType;
import gw.lang.reflect.IModifierInfo;


public interface ISymbol extends IHasType, IReducedSymbol
{
  String THIS = Keyword.KW_this.getName();
  String SUPER = Keyword.KW_super.getName();

  /**
   * Returns the Symbol's name.
   */
  public String getName();

  /**
   * Returns the Symbol's optional display name.  If a display name is not assigned,
   * returns the symbol's name.
   */
  public String getDisplayName();

  /**
   * Returns the Symbol's type.
   */
  public IType getType();

  /**
   * Sets the Symbol's type.
   */
  public void setType( IType type );

  /**
   * Returns the value assigned to this Symbol.
   */
  public Object getValue();

  /**
   * Assigns a value to this Symbol.
   */
  public void setValue( Object value );

  /**
   * The symbol's default value e.g., a default parameter value for a function.
   */
  public IExpression getDefaultValueExpression();
  public void setDefaultValueExpression( IExpression defaultValue );

  /**
   * Assigns an optional symbol table so that the symbol can get/set its value
   * dynamically e.g., via ThreadLocalSymbolTable.
   */
  public void setDynamicSymbolTable( ISymbolTable symTable );

  public boolean hasDynamicSymbolTable();
  
  public ISymbolTable getDynamicSymbolTable();

  /**
   * Creates a copy of this symbol without the value so that the empty symbol can be stored.
   */
  public ISymbol getLightWeightReference();

  /**
   * Returns true if this symbol is writable.
   * <p/>
   * An example of a symbol that is not writable is a readonly Property
   * referenced as a symbol in a Gosu class.
   */
  public boolean isWritable();

  /**
   * Indicates that this symbol should use a reference rather than storing its value directly.
   */
  void setValueIsBoxed( boolean b );

  public boolean isValueBoxed();

  int getIndex();

  boolean canBeCaptured();

  ICapturedSymbol makeCapturedSymbol( String strName, ISymbolTable symbolTable, IScope scope );

  boolean isLocal();

  IModifierInfo getModifierInfo();
  
  public IReducedSymbol createReducedSymbol();
}
