/*
 * Copyright 2014 Guidewire Software, Inc.
 */
package gw.lang.parser;

import java.util.List;
import java.util.Map;


public interface ISymbolTable extends IStackProvider
{
  /**
   * @return The symbol mapped to the specified name.
   */
  public ISymbol getSymbol( CharSequence name );

  /**
   * Maps a name to a symbol in the table.
   */
  public void putSymbol( ISymbol symbol );

  /**
   * Removes a previously mapped symbol.
   *
   * @param name The name mapped to the symbol to remove.
   */
  public ISymbol removeSymbol( CharSequence name );

  /**
   * @return A list of currently mapped ISymbols e.g., the values in a hash
   *         table based implementation.
   */
  public Map getSymbols();

  /**
   * @param iScopeIndex Scopes positioned on the stack at an index greater than
   *                    this number are not included. Very useful for examining a specific scope
   *                    e.g., for a debugger. Note an index < 0 indicates that all scopes are
   *                    included.
   *
   * @return A list of currently mapped ISymbols e.g., the values in a hash
   *         table based implementation.
   */
  public Map getSymbols( int iScopeIndex, int iPrivateGlobalIndex );

  /**
   * @return The number of scopes on the stack. These include all scopes:
   *         global, isolated, and local. Useful for recording a specific offset in the
   *         symbol table e.g., a debugger needs this to jump to a position in a call
   *         stack.
   *
   * @see #getSymbols(int,int)
   */
  public int getScopeCount();

  /**
   * @return The number of scopes on the private global stack. Useful for
   *         recording a specific offset in the symbol table e.g., a debugger needs
   *         this to jump to a position in a call stack.
   *
   * @see #getSymbols(int,int)
   */
  public int getPrivateGlobalScopeCount();

  /**
   * Push a local scope context onto the symbol table.
   *
   * @return The pushed scope.
   */
  public IScope pushScope();

  /**
   * Push a local scope context onto the symbol table.
   *
   * @param scope the scope to push
   *
   * @return The pushed scope.
   */
  public IScope pushScope( IScope scope );

  /**
   * Pop a local scope context from the symbol table.
   * <p/>
   * See pushScope() for implementation suggestions.
   *
   * @return The popped scope.
   */
  public IScope popScope();

  /**
   * @return the currently active scope
   */
  public IScope peekScope();
  public IScope peekScope( int iPos );

  public IScope popScope( IScope scope );

  /**
   * Push a scope that demarcates an activation record. The behavior is nearly
   * identical to pushScope(), the [big] difference is that activation record
   * scopes cannot access symbols from other activation record scopes.
   * <p/>
   * Use popScope() to pop a scope pushed via this method.
   *
   * @param activationCtx The context for the activation record.
   *
   * @return The isolated scope (aka the activation record).
   */
  public IScope pushIsolatedScope( IActivationContext activationCtx );

  /**
   * Push a global scope you specify onto the private global scope space. Useful
   * for handling private global scopes for libraries, namespaces, etc. As this
   * functionality is primarily for Gosu runtime, you'll likely never need to
   * call this.
   * <p/>
   * If you need to push a scope with restricted visibility, consider calling
   * <code>pushIsolatedScope()</code> instead.
   *
   * @see #pushScope()
   * @see #pushIsolatedScope(IActivationContext)
   */
  public void pushPrivateGlobalScope( IScope scope );

  /**
   * Pops a global scope previously pushed via <code>pushGlobalScope( IScope )</code>
   * or <code>pushPrivateGlobalScope( IScope )</code>.
   * <p/>
   * You probably shouldn't call this method.
   *
   * @see #pushPrivateGlobalScope(IScope)
   * @see #popScope()
   */
  public void popGlobalScope( IScope scope );

  /**
   * Perform a semi-deep copy of this symbol table. Symbols need not be cloned.
   *
   * @return A semi-deep copy of this symbol table.
   */
  public ISymbolTable copy();

  /**
   * Get the 'this' symbol from either the stack or the table.
   */
  public ISymbol getThisSymbolFromStackOrMap();

  /**
   * Define symbols that are considered ubiquitous. There may be none.
   */
  public void defineCommonSymbols();

  /**
   * @return the number of symbols exist in this table.
   */
  int getTotalSymbolCount();

  /**
   * @return true if the given symbol is within the given scope
   */
  boolean isSymbolWithinScope( ISymbol sym, IScope scope );

  /**
   * @return the top-most isolated scope
   */
  IScope peekIsolatedScope();
}
