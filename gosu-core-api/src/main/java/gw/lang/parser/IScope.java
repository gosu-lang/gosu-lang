/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.parser;

import java.util.Map;

public interface IScope<K, V extends ISymbol> extends Map<K, V>
{
  /**
   * Shallow copy this scope
   */
  public IScope<K,V> copy();

  /**
   * Get the activation record context. This can be any object representing the
   * activation record e.g., a function symbol, a rule set context, whatever
   * delimits a call boundary.
   *
   * @return The activation context.
   */
  public IActivationContext getActivationCtx();

  /**
   * visit all symbols in this Scope,
   *
   * @return true if the visitor want to continue visitiong other symbol/scope,
   *         false otherwise.
   */
  public int countSymbols();

  V put( K key, V value );

  /**
   * @return the compile-time csr for this scope if it exists
   */
  int getCSR();

  public void setCSR( int csr );

}
