/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect.gs;


import gw.lang.reflect.IType;

public interface IExternalSymbolMap {

  /**
   * Returns the value for the symbol with the given name.  Throws a runtime exception if the name does not
   * correspond to a valid external symbol.
   *
   * @param name the name of the symbol
   * @return the current value of the symbol
   */
  Object getValue(String name);

  /**
   * Called via bytecode.
   * @param name
   * @param iArrayDims  the expected number of array dimensions of the the symbol's type, useful for
   *                    debugger expr evaluation for dynamically unwrapping type a single elem array (a captured var)
   * @return
   */
  Object getValue(String name, int iArrayDims);

  /**
   * Sets the value of the symbol with the given name.  Throws a runtime exception if the name does not
   * correspond to a valid external symbol.
   *
   * @param name the name of the symbol
   * @param value the new value to give that symbol
   */
  void setValue(String name, Object value);

  /**
   * Invokes the named external function with the given arguments.  The name argument should correspond to
   * the result of calling getName() on the external function symbol.
   *
   * @param name the name of the function symbol
   * @param args the arguments to the method
   * @return the result of the function invocation
   */
  Object invoke(String name, Object[] args);
}
