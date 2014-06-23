/*
 * Copyright 2014 Guidewire Software, Inc.
 */
package gw.lang.parser;


public interface IFunctionSymbol extends ISymbol
{
  /**
   * Invokes the method represented by this symbol.
   *
   * @param args An array of arguments to forward to the function.
   */
  public Object invoke( Object[] args );

  /**
   * Returns a description of arguments of the form ( < argName1 >, < argName2 >, etc. )
   * appropriate for display in a source editor ui.
   */
  public String getSignatureDescription();

  /**
   * Returns true if the value of this function symbol is stored on the stack as a variable
   */
  public boolean isStackSymbol();

  /**
   * Returns the modifiers for this function symbol
   */
  public int getModifiers();
  
  /**
   * @return true if this function symbol came from a java superclass
   */
  boolean isFromJava();
}
