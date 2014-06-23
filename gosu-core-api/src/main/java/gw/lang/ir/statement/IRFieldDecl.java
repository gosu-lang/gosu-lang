/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.ir.statement;

import gw.lang.UnstableAPI;
import gw.lang.ir.IRAnnotation;
import gw.lang.ir.IRStatement;
import gw.lang.ir.IRType;

import java.util.Collections;
import java.util.List;

@UnstableAPI
public class IRFieldDecl extends IRStatement {
  private int _modifiers;
  private String _name;
  private IRType _type;
  private Object _value;
  private List<IRAnnotation> _annotations = Collections.emptyList();

  public IRFieldDecl( int modifiers, String name, IRType type, Object value ) {
    _modifiers = modifiers;
    _name = name;
    _type = maybeEraseStructuralType( type );
    _value = value;
  }

  public int getModifiers() {
    return _modifiers;
  }

  public String getName() {
    return _name;
  }

  public IRType getType() {
    return _type;
  }

  public Object getValue() {
    return _value;
  }

  @Override
  public IRTerminalStatement getLeastSignificantTerminalStatement() {
    return null;
  }

  public void setAnnotations( List<IRAnnotation> irAnnotations )
  {
    _annotations = irAnnotations;
  }

  public List<IRAnnotation> getAnnotations()
  {
    return _annotations;
  }
}
