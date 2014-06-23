/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.psi.impl;

import com.intellij.codeInsight.completion.CompletionInitializationContext;
import gw.lang.parser.IGosuValidator;
import gw.lang.parser.ISymbolTable;
import gw.lang.parser.statements.IClassFileStatement;
import gw.lang.reflect.IType;
import gw.lang.reflect.module.IModule;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GosuClassParseData {
  private final IModule _module;
  @Nullable
  private String _source;

  @Nullable
  private IClassFileStatement _classFileStatement;

  // For code completion
  @Nullable
  private ISymbolTable _snapshotSymbols;
  @Nullable
  private IType _ctxType;

  public GosuClassParseData(IModule module) {
    this._module = module;
  }

  public void clear() {
    _snapshotSymbols = null;
    _classFileStatement = null;
    _source = null;
    _ctxType = null;
  }

  public IModule getModule() {
    return _module;
  }

  public boolean isFromCodeCompletion() {
    return _source != null && _source.contains(CompletionInitializationContext.DUMMY_IDENTIFIER);
  }

  // ContextType
  public void setContextType(IType ctxType) {
    _ctxType = ctxType;
  }

  @Nullable
  public IType getContextType() {
    return _ctxType;
  }

  // ClassFileStatement
  public void setClassFileStatement(IClassFileStatement classFileStatement) {
    _classFileStatement = classFileStatement;
  }

  @Nullable
  public IClassFileStatement getClassFileStatement() {
    return _classFileStatement;
  }

  // Source
  public void setSource(@NotNull CharSequence source) {
    _source = source.toString();
  }

  @Nullable
  public String getSource() {
    return _source;
  }

  // SnapshotSymbols
  @Nullable
  public ISymbolTable getSnapshotSymbols() {
    return _snapshotSymbols;
  }

  public void setSnapshotSymbols(ISymbolTable snapshotSymbols) {
    _snapshotSymbols = snapshotSymbols;
  }
}
