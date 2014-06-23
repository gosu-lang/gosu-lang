/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.ir.statement;

import gw.lang.UnstableAPI;
import gw.lang.ir.IRAnnotation;
import gw.lang.ir.IRExpression;
import gw.lang.ir.IRStatement;
import gw.lang.ir.IRSymbol;
import gw.lang.ir.IRType;
import gw.util.GosuStringUtil;

import java.util.Collections;
import java.util.List;

@UnstableAPI
public class IRMethodStatement extends IRStatement {
  private IRStatement _methodBody;
  private String _name;
  private int _modifiers;
  private List<IRSymbol> _parameters;
  private IRType _returnType;
  private List<IRAnnotation> _annotations;
  private Object[] _annotationDefault;

  public IRMethodStatement(IRStatement methodBody, String name, int modifiers, IRType returnType, List<IRSymbol> parameters) {
    this( methodBody, name, modifiers, returnType, parameters, null );
  }
  public IRMethodStatement(IRStatement methodBody, String name, int modifiers, IRType returnType, List<IRSymbol> parameters, Object[] annotationDefault) {
    _methodBody = methodBody;
    _name = name;
    _modifiers = modifiers;
    _returnType = maybeEraseStructuralType( returnType );
    _parameters = maybeEraseStructuralSymbolTypes( parameters );
    _annotations = Collections.emptyList();
    setParentToThis( methodBody );
    _annotationDefault = annotationDefault;
  }

  public IRStatement getMethodBody() {
    return _methodBody;
  }

  public String getName() {
    return _name;
  }

  public int getModifiers() {
    return _modifiers;
  }

  public List<IRSymbol> getParameters() {
    return _parameters;
  }

  public IRType getReturnType() {
    return _returnType;
  }

  @Override
  public IRTerminalStatement getLeastSignificantTerminalStatement() {
    return null;
  }

  @Override
  public String toString()
  {
    return _name + "(" + GosuStringUtil.join( _parameters, ", " ) + "):" + _returnType;
  }

  public void setAnnotations( List<IRAnnotation> irAnnotations )
  {
    _annotations = irAnnotations;
  }

  public List<IRAnnotation> getAnnotations()
  {
    return _annotations;
  }

  public Object[] getAnnotationDefault() {
    return _annotationDefault;
  }
}
