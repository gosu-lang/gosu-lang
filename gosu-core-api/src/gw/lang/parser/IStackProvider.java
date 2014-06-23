/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.parser;

public interface IStackProvider
{
  int THIS_POS = 0;
  int SUPER_POS = 1;
  int START_POS = 2;

  /**
   * For compile-time assignment of stack indexes.
   */
  public int getNextStackIndex();

  /**
   * For compile-time assignment of stack indexes at a particular scope.
   */
  public int getNextStackIndexForScope( IScope scope );

  /**
   * For compile-time use. Returns true iff an isolated scope is visible.
   */
  public boolean hasIsolatedScope();
}
