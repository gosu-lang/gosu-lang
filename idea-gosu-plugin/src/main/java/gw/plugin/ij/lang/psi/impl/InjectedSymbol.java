/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.psi.impl;

import gw.internal.gosu.parser.IGosuAnnotation;
import gw.lang.parser.ICapturedSymbol;
import gw.lang.parser.IExpression;
import gw.lang.parser.IInjectedSymbol;
import gw.lang.parser.IReducedSymbol;
import gw.lang.parser.IScope;
import gw.lang.parser.IScriptPartId;
import gw.lang.parser.ISymbol;
import gw.lang.parser.ISymbolTable;
import gw.lang.reflect.IModifierInfo;
import gw.lang.reflect.IType;
import gw.lang.reflect.gs.IGosuClass;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class InjectedSymbol implements ISymbol, IInjectedSymbol {
  private final ISymbol symbol;

  public InjectedSymbol(ISymbol symbol) {
    this.symbol = symbol;
  }

  @NotNull
  @Override
  public ISymbol getLightWeightReference() {
    return this;
  }

  @Override
  public String getName() {
    return symbol.getName();
  }

  @Override
  public String getDisplayName() {
    return symbol.getDisplayName();
  }

  @Override
  public IType getType() {
    return symbol.getType();
  }

  @Override
  public void setType(IType type) {
    symbol.setType(type);
  }

  @Override
  public Object getValue() {
    return symbol.getValue();
  }

  @Override
  public void setValue(Object value) {
    symbol.setValue(value);
  }

  @Override
  public IExpression getDefaultValueExpression() {
    return symbol.getDefaultValueExpression();
  }

  @Override
  public void setDefaultValueExpression(IExpression defaultValue) {
    symbol.setDefaultValueExpression(defaultValue);
  }

  @Override
  public void setDynamicSymbolTable(ISymbolTable symTable) {
    symbol.setDynamicSymbolTable(symTable);
  }

  @Override
  public boolean hasDynamicSymbolTable() {
    return symbol.hasDynamicSymbolTable();
  }

  @Override
  public ISymbolTable getDynamicSymbolTable() {
    return symbol.getDynamicSymbolTable();
  }

  @Override
  public boolean isWritable() {
    return symbol.isWritable();
  }

  @Override
  public void setValueIsBoxed(boolean b) {
    symbol.setValueIsBoxed(b);
  }

  @Override
  public int getIndex() {
    return symbol.getIndex();
  }

  @Override
  public boolean canBeCaptured() {
    return symbol.canBeCaptured();
  }

  @Override
  public ICapturedSymbol makeCapturedSymbol( String strName, ISymbolTable symbolTable, IScope scope) {
    return symbol.makeCapturedSymbol(strName, symbolTable, scope);
  }

  @Override
  public boolean isValueBoxed() {
    return symbol.isValueBoxed();
  }

  @Override
  public boolean isLocal() {
    return symbol.isLocal();
  }

  @Override
  public IModifierInfo getModifierInfo() {
    return symbol.getModifierInfo();
  }

  @Override
  public IReducedSymbol createReducedSymbol() {
    return symbol.createReducedSymbol();
  }

  @Override
  public boolean isStatic() {
    return symbol.isStatic();
  }

  @Override
  public int getModifiers() {
    return symbol.getModifiers();
  }

  @Override
  public List<IGosuAnnotation> getAnnotations() {
    return symbol.getAnnotations();
  }

  @Override
  public String getFullDescription() {
    return symbol.getFullDescription();
  }

  @Override
  public boolean isPrivate() {
    return symbol.isPrivate();
  }

  @Override
  public boolean isInternal() {
    return symbol.isInternal();
  }

  @Override
  public boolean isProtected() {
    return symbol.isProtected();
  }

  @Override
  public boolean isPublic() {
    return symbol.isPublic();
  }

  @Override
  public boolean isAbstract() {
    return symbol.isAbstract();
  }

  @Override
  public boolean isFinal() {
    return symbol.isFinal();
  }

  @Override
  public IScriptPartId getScriptPart() {
    return symbol.getScriptPart();
  }

  @Override
  public IGosuClass getGosuClass() {
    return symbol.getGosuClass();
  }

  @Override
  public boolean hasTypeVariables() {
    return symbol.hasTypeVariables();
  }

  @Override
  public Class<?> getSymbolClass() {
    return symbol.getSymbolClass();
  }
}
