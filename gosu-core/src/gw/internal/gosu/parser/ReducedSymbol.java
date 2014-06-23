/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.lang.parser.GlobalScope;
import gw.lang.parser.IExpression;
import gw.lang.parser.IReducedSymbol;
import gw.lang.parser.IScriptPartId;
import gw.lang.parser.ISymbol;
import gw.lang.reflect.IType;
import gw.lang.reflect.Modifier;
import gw.lang.reflect.gs.IGenericTypeVariable;

import java.util.ArrayList;
import java.util.List;

/**
 */
public class ReducedSymbol implements IReducedSymbol {

  private boolean _isStatic;
  private IType _type;
  private String _name;
  private String _displayName;
  protected String _fullDescription;
  private IScriptPartId _scriptPartId;
  private int _modifiers;
  private List<IGosuAnnotation> _annotations;
  private Class<?> _symClass;
  private IExpression _defValue;
  private GlobalScope _globalScope;
  private boolean _bValueBoxed;
  private int _iIndex;
  
  ReducedSymbol(AbstractDynamicSymbol sym) {
    _isStatic = sym.isStatic();
    _name = sym.getName();
    _displayName = sym.getDisplayName();
    _type = sym.getType();
    _scriptPartId = sym.getScriptPart();
    _modifiers = sym.getModifierInfo().getModifiers();
    _annotations = sym.getModifierInfo().getAnnotations();
    _symClass = sym.getClass();
    _bValueBoxed = sym.isValueBoxed();
    _iIndex = sym.getIndex();
    if( sym instanceof DynamicFunctionSymbol )
    {
      _defValue = ((DynamicFunctionSymbol)sym).getAnnotationDefault();
    }
    if( sym instanceof ScopedDynamicSymbol )
    {
      _globalScope = sym.getScope();
    }
  }

  public ReducedSymbol( ISymbol arg ) {
    _name = arg.getName();
    _displayName = _name;
    _type = arg.getType();
    _scriptPartId = arg.getScriptPart();
    _defValue = arg.getDefaultValueExpression();
    _modifiers = arg.getModifiers();
    _annotations = arg.getAnnotations();
    _symClass = arg.getClass();
    _globalScope = arg.getScope();
    _bValueBoxed = arg.isValueBoxed();
    _iIndex = arg.getIndex();
  }

  public Class<?> getSymbolClass()
  {
    return _symClass;
  }

  @Override
  public GlobalScope getScope() {
    return _globalScope;
  }

  @Override
  public boolean isValueBoxed() {
    return _bValueBoxed;
  }

  @Override
  public int getIndex() {
    return _iIndex;
  }

  public IExpression getDefaultValueExpression()
  {
    return _defValue;
  }

  public boolean isStatic() {
    return _isStatic;
  }

  public int getModifiers() {
    return _modifiers;
  }

  public List<IGosuAnnotation> getAnnotations() {
    return _annotations;
  }

  public String getName() {
    return _name;
  }

  public String getDisplayName() {
    return _displayName;
  }

  public String getFullDescription() {
    return _fullDescription;
  }

  public boolean isPrivate() {
    return Modifier.isPrivate(getModifiers());
  }

  public boolean isInternal() {
    return Modifier.isInternal(getModifiers());
  }

  public boolean isProtected() {
    return Modifier.isProtected(getModifiers());
  }

  public boolean isPublic() {
    return Modifier.isPublic(getModifiers()) || (!isPrivate() && !isProtected() && !isInternal());
  }

  public boolean isAbstract() {
    return Modifier.isAbstract( getModifiers() );
  }

  public boolean isFinal() {
    return Modifier.isFinal( getModifiers() );
  }

  public IType getType() {
    return _type;
  }
  void setType( IType type ) {
    _type = type;
  }

  public IScriptPartId getScriptPart() {
    return _scriptPartId;
  }

  public IGosuClassInternal getGosuClass()
  {
    return _scriptPartId != null
           ? _scriptPartId.getContainingType() instanceof IGosuClassInternal
             ? (IGosuClassInternal)_scriptPartId.getContainingType()
             : (IGosuClassInternal)_scriptPartId.getRuntimeType()
           : null;
  }

  public boolean hasTypeVariables() {
    IGenericTypeVariable[] tvs = getType().getGenericTypeVariables();
    return tvs != null && tvs.length != 0;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    ReducedSymbol that = (ReducedSymbol) o;

    if (_isStatic != that._isStatic) return false;
    if (_modifiers != that._modifiers) return false;
    if (_displayName != null ? !_displayName.equals(that._displayName) : that._displayName != null) return false;
    if (_fullDescription != null ? !_fullDescription.equals(that._fullDescription) : that._fullDescription != null) return false;
    if (_globalScope != that._globalScope) return false;
    if (_name != null ? !_name.equals(that._name) : that._name != null) return false;
    if (_scriptPartId != null ? !_scriptPartId.equals(that._scriptPartId) : that._scriptPartId != null) return false;
    if (_symClass != null ? !_symClass.equals(that._symClass) : that._symClass != null) return false;
    if (_type != null ? !_type.equals(that._type) : that._type != null) return false;

    return true;
  }

  public static List<ISymbol> makeArgs(List<IReducedSymbol> args) {
    List<ISymbol> newArgs = new ArrayList<ISymbol>(args.size());
    for (IReducedSymbol arg : args) {
      newArgs.add(new SyntheticSymbol( arg, arg.getName(), arg.getType(), null ) );
    }
    return newArgs;
  }

  public static class SyntheticSymbol extends Symbol {
    IReducedSymbol _reducedSym;

    public SyntheticSymbol( IReducedSymbol reducedSym, String name, IType type, IExpression defaultValue) {
      super( name, type, defaultValue );
      _reducedSym = reducedSym;
    }

    public IReducedSymbol getReducedSymbol()
    {
      return _reducedSym;
    }
  }
}
